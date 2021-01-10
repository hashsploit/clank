package net.hashsploit.clank.server.pipeline;

import java.math.BigInteger;
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
import net.hashsploit.medius.crypto.rc.PS2_RC4;
import net.hashsploit.medius.crypto.rsa.PS2_RSA;

public class RtDecryptionHandler extends MessageToMessageDecoder<ByteBuf> {

	private static final Logger logger = Logger.getLogger(RtDecryptionHandler.class.getName());
	private MediusClient client;

	public RtDecryptionHandler(MediusClient client) {
		super();
		this.client = client;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> output) throws Exception {
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

		ByteBuf msg = null;

		// add mapping
		RtMessageId rtid = null;
		for (final RtMessageId p : RtMessageId.values()) {
			if (p.getValue() == id) {
				rtid = p;
				break;
			}
		}

		if (frameLength <= 0) {
			msg = process(rtid, hash, new byte[0]);
		}

		// Convert the RT ID if encrypted to a normal RT ID -0x80
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

		// If the hash is not null, this packet can be decrypted, so decrypt it
		if (hash != null) {
			msg = process(rtid, hash, messageContents);
		}

		return msg;
	}

	private ByteBuf process(RtMessageId id, byte[] hash, byte[] message) {

		logger.finest("DECRYPT HANDLER DATA IN: " + Utils.bytesToHex(message));
		logger.finest("DECRYPT HANDLER hash IN: " + Utils.bytesToHex(hash));
		
		if (hash != null) {
			CipherContext context = null;

			int hashCtx = hash[3] & 0xFF;
			int hashCtxAdjusted = hashCtx >>> 0x05;

			for (final CipherContext ctx : CipherContext.values()) {
				if (ctx.id == (byte) hashCtxAdjusted) {
					context = ctx;
					break;
				}
			}

			SCERTDecryptedData decryptedData = null;

			switch (context) {
				case RSA_AUTH:
					{
						final PS2_RSA rsa = new PS2_RSA();
						rsa.setContext(context);
						decryptedData = rsa.decrypt(message, hash);
						
						logger.finest("RSA Post decryption: " + Utils.bytesToHex(decryptedData.getData()));
						logger.finest("RSA Post decryption status: " + decryptedData.isSuccessful());
						
						if (!decryptedData.isSuccessful()) {
							throw new IllegalStateException("Decryption failure!");
						}
						
						// TODO: Remove in production
						// This is a test to re-encrypt the data
						SCERTEncryptedData enc = rsa.encrypt(decryptedData.getData());
						byte[] newEnc = enc.getData();
						
						if (!Utils.sequenceEquals(newEnc, message)) {
							//throw new IllegalStateException("RSA_AUTH: Re-encryption does not match original encryption for: \nOriginal message: " + Utils.bytesToHex(message) + "\nRe-encrypted    : " + Utils.bytesToHex(newEnc));
							throw new IllegalStateException(String.format("Client RSA re-encryption test failure! {Original: %s, Re-Encrypted: %s} %s:%d", client.getIPAddress(), client.getPort()));
						}
						
						
						ByteBuf data = new RTMessage(id, decryptedData.getData()).getFullMessage();
						logger.finest("DECRYPT HANDLER OUT: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(data)));
						return data;
					}
				case RC_CLIENT_SESSION:
					{
						PS2_RC4 clientSessionKey = client.getRC4ClientSessionKey();
						
						if (clientSessionKey == null) {
							throw new IllegalStateException(String.format("Client RC4 session key not initialized! %s:%d", client.getIPAddress(), client.getPort()));
						}
						
						SCERTDecryptedData scertData = clientSessionKey.decrypt(message, hash);
	
						byte[] decryptedBytes = scertData.getData();
	
//						logger.finest("RC4 Post decryption: " + Utils.bytesToHex(decryptedBytes));
//						logger.finest("RC4 Post decryption status: " + scertData.isSuccessful());
			
						if (!scertData.isSuccessful()) {
							throw new IllegalStateException("RC4 Post decryption status is FALSE: Decrypted message: \nDecrypted: " + Utils.bytesToHex(decryptedBytes));
						}
						
						
						SCERTEncryptedData scertReEncrypted = clientSessionKey.encrypt(decryptedBytes);
	
						// TODO: REMOVE THIS IN PRODUCTION
						byte[] toReEncrypt = scertReEncrypted.getData();
						
						//logger.finest("Rencrypted hash: " + Utils.bytesToHex(scertReEncrypted.getHash()));
						//logger.finest("Reencrypted data: " + Utils.bytesToHex(scertReEncrypted.getData()));
						
						if (!Utils.sequenceEquals(message, toReEncrypt)) {
							throw new IllegalStateException("RC_CLIENT_SESSION: Re-encryption does not match original encryption for: \nOriginal encrypted: " + Utils.bytesToHex(message) + "\nRe-encrypted      : " + Utils.bytesToHex(toReEncrypt));
						}
						
						ByteBuf data2 = new RTMessage(id, scertData.getData()).getFullMessage();
						logger.finest("DECRYPT HANDLER OUT: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(data2)));
						return data2;
					}
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
