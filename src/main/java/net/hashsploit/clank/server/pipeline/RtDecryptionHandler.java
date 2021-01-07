package net.hashsploit.clank.server.pipeline;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;
import net.hashsploit.medius.crypto.CipherContext;
import net.hashsploit.medius.crypto.SCERTDecryptedData;
import net.hashsploit.medius.crypto.SCERTEncryptedData;
import net.hashsploit.medius.crypto.rsa.PS2_RSA;

public class RtDecryptionHandler extends MessageToMessageDecoder<ByteBuf> {

	private static final Logger logger = Logger.getLogger(RtDecryptionHandler.class.getName());
	private MediusClient client;
	private final BigInteger N = new BigInteger("10315955513017997681600210131013411322695824559688299373570246338038100843097466504032586443986679280716603540690692615875074465586629501752500179100369237",10);
	private final BigInteger E = new BigInteger("17",10);
	private final BigInteger D = new BigInteger("4854567300243763614870687120476899445974505675147434999327174747312047455575182761195687859800492317495944895566174677168271650454805328075020357360662513",10);

	public RtDecryptionHandler(MediusClient client) {
		super();
		this.client = client;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> output) throws Exception {
		logger.info("Got pipeline!");
		final Object decoded = this.decode(ctx, input);
		if (decoded != null) {
			output.add(decoded);
		}
	}

	private Object decode(ChannelHandlerContext ctx, ByteBuf input) {
		
		// Convert the current id (-0x80 if it's encrypted)
		byte id = (byte) (input.getByte(input.readerIndex()) & (byte) 0x7F);
		byte[] hash = null;
		long frameLength = input.getShortLE(input.readerIndex() + 1);
		int totalLength = 3;
		
		RTMessage msg = null;

		logger.severe(RtDecryptionHandler.class.getName() + ": " + Utils.bytesToHex(Utils.byteBufToNioBuffer(input).array()));

		// add mapping
		RtMessageId rtid = null;
		for (final RtMessageId p : RtMessageId.values()) {
			if (p.getValue() == id) {
				rtid = p;
				break;
			}
		}

		if (frameLength <= 0) {
			// return BaseScertMessage.Instantiate((RT_MSG_TYPE) (id & 0x7F), null, new byte[0], _getCipher);
			// use mapping
			msg = process(rtid, hash, new byte[0]);
		}

		if (id >= (byte) 0x80) {
			hash = new byte[4];
			input.getBytes(input.readerIndex() + 3, hash);
			totalLength += 4;
			id &= (byte) 0x7F;
		}

		if (frameLength < 0) {
			throw new CorruptedFrameException("negative pre-adjustment length field: " + frameLength);
		}

		// never overflows because it's less than maxFrameLength
		int frameLengthInt = (int) frameLength;
		if (input.readableBytes() < frameLengthInt) {
			input.resetReaderIndex();
			return null;
		}

		// extract frame
		byte[] messageContents = new byte[frameLengthInt];
		input.getBytes(input.readerIndex() + totalLength, messageContents);

		input.readerIndex(input.readerIndex() + totalLength + frameLengthInt);
		// return BaseScertMessage.Instantiate((RT_MSG_TYPE) id, hash, messageContents, _getCipher);
		msg = process(rtid, hash, messageContents);
		
		return msg;
	}

	private RTMessage process(RtMessageId id, byte[] hash, byte[] message) {
		
		if (hash != null) {
			CipherContext context = null;
			
			logger.info("Incoming hash: " + Utils.bytesToHex(hash));			
			logger.info("Incoming hash context: " + (hash[3] & 0xff >> 5));
			
			for (final CipherContext ctx : CipherContext.values()) {
				if (ctx.id == (hash[3] & 0xff >> 5)) {
					context = ctx;
					break;
				}
			}
			
			SCERTDecryptedData decryptedData = null;
			
			logger.info("Pre decryption message: " + Utils.bytesToHex(message));
			//Utils.flipByteArray(message);
			
			switch (context) {
				case RSA_AUTH:
					PS2_RSA rsa = new PS2_RSA(N, E, D);
					//rsa.setContext(CipherContext.ID_00);
					rsa.setContext(context);
					decryptedData = rsa.decrypt(message, hash);
					logger.info("Post decryption: " + Utils.bytesToHex(decryptedData.getData()));
					logger.info("Post decryption status: " + decryptedData.isSuccessful());
					
					SCERTEncryptedData enc = rsa.encrypt(decryptedData.getData());
					logger.info("Reencrypted: " + Utils.bytesToHex(enc.getData()));
					
					break;
				case RC_CLIENT_SESSION:
					break;
				case RC_SERVER_SESSION:
					break;
				default:
					return null;
			}
			
		}
		
		
		return null;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		logger.warning(String.format("Exception in %s from %s: %s", RtDecryptionHandler.class.getName(), ctx.channel().remoteAddress().toString(), cause.getMessage()));
		cause.printStackTrace();
		ctx.close();
	}

}
