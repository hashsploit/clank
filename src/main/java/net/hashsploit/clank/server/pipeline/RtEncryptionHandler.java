package net.hashsploit.clank.server.pipeline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;
import net.hashsploit.medius.crypto.SCERTEncryptedData;
import net.hashsploit.medius.crypto.rsa.PS2_RSA;

public class RtEncryptionHandler extends MessageToByteEncoder<List<RTMessage>> {

	private static final Logger logger = Logger.getLogger(RtEncryptionHandler.class.getName());
	private final MediusClient client;

	public RtEncryptionHandler(final MediusClient client) {
		super();
		this.client = client;
	}


	@Override
	protected void encode(ChannelHandlerContext ctx, List<RTMessage> msg, ByteBuf out) throws Exception {
		for (RTMessage m: msg) {
			logger.info("Encryption packet detected:");
			logger.info(m.toString());
			sendSinglePacket(ctx, m);
		}		
	}
	
	
	private void sendSinglePacket(ChannelHandlerContext ctx, RTMessage msg) {
		
		// Add 0x80 to the RT ID if the packet is encrypted.
		byte id = (byte) (msg.getId().getValue() | (byte) 0x80);
		
		// If the RT ID is a CLIENT DISCONNECT, do not add 0x80 to the ID.
		if (msg.getId() == RtMessageId.CLIENT_DISCONNECT) {
			id = msg.getId().getValue();
		}

		PS2_RSA keyToEncryptWith = client.getRSAKey();
		
		BigInteger n = keyToEncryptWith.getN();
		BigInteger e = keyToEncryptWith.getE();
		
		logger.info("N: " + n);
		logger.info("E: " + e);

		byte[] length = Utils.shortToBytesLittle((short) msg.getLength());

		byte[] key = msg.getPayload();
		//key = Utils.flipByteArray(key);

		logger.info("RC4 key to encrypt [hex]: " + Utils.bytesToHex(key));
		SCERTEncryptedData scertData = keyToEncryptWith.encrypt(key);
		byte[] hash = scertData.getHash();

		byte[] encryptedData = scertData.getData();
		
		logger.info("ID: " + Utils.byteToHex(id));
		logger.info("length: " + Utils.bytesToHex(length));
		logger.info("hash: " + Utils.bytesToHex(hash));
		logger.info("encryptedData: " + Utils.bytesToHex(encryptedData));
		
		// Creates an output stream
		ByteArrayOutputStream out = new ByteArrayOutputStream();		
		// Writes data to the output stream
		try {
			out.write(id);
			out.write(length);
			out.write(hash);
			out.write(encryptedData);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		logger.info("FINAL OUT: " + Utils.bytesToHex(out.toByteArray()));
		ByteBuf bb = Unpooled.copiedBuffer(out.toByteArray());
		ctx.channel().pipeline().writeAndFlush(bb);
	}
	
}
