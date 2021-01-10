package net.hashsploit.clank.server.pipeline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;
import net.hashsploit.medius.crypto.CipherContext;
import net.hashsploit.medius.crypto.SCERTEncryptedData;
import net.hashsploit.medius.crypto.rc.PS2_RC4;
import net.hashsploit.medius.crypto.rsa.PS2_RSA;

public class RtEncryptionHandler extends MessageToMessageEncoder<List<ByteBuf>> {

	private static final Logger logger = Logger.getLogger(RtEncryptionHandler.class.getName());
	private final MediusClient client;

	public RtEncryptionHandler(final MediusClient client) {
		super();
		this.client = client;
	}

	// TODO: don't write directly to the pipeline, use ByteBuf out \/
	@Override
	protected void encode(ChannelHandlerContext ctx, List<ByteBuf> msg, List<Object> out) throws Exception {
		
		for (ByteBuf m : msg) {
			logger.finest("ENCRYPTION PIPELINE IN: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(m)));

			RTMessage rtMsg = new RTMessage(m);

			
			ByteBuf bb = encryptSinglePacket(ctx, rtMsg);
			
			if (bb != null) {
				out.add(bb);
			}
		}
		
	}

	private ByteBuf encryptSinglePacket(ChannelHandlerContext ctx, RTMessage msg) {

		switch (msg.getId()) {
			case SERVER_CRYPTKEY_PEER:
				return encryptRsa(ctx, msg);
			case SERVER_CRYPTKEY_GAME:
				return encryptRc4client(ctx, msg);
			case SERVER_CONNECT_ACCEPT_TCP:
				return encryptRc4client(ctx, msg);
			case SERVER_CONNECT_COMPLETE:
				return encryptRc4client(ctx, msg);
			case SERVER_APP:
				return encryptRc4client(ctx, msg);
			case CLIENT_APP_TOSERVER:
				return encryptRc4client(ctx, msg);
			default:
				break;
		}
		
		return null;
	}

	private ByteBuf encryptRsa(ChannelHandlerContext ctx, RTMessage msg) {

		// Add 0x80 to the RT ID if the packet is encrypted.
		byte id = (byte) (msg.getId().getValue() | (byte) 0x80);

		// If the RT ID is a CLIENT DISCONNECT, do not add 0x80 to the ID.
		if (msg.getId() == RtMessageId.CLIENT_DISCONNECT) {
			id = msg.getId().getValue();
		}
		PS2_RSA keyToEncryptWith = client.getRSAKey();

		if (keyToEncryptWith == null) {
			logger.severe("ENCRYPT_RSA FUNCTION RSA KEY IS NULL!");
		}

		byte[] length = Utils.shortToBytesLittle((short) msg.getLength());

		byte[] key = Utils.nettyByteBufToByteArray(msg.getPayload());

		keyToEncryptWith.setContext(CipherContext.RSA_AUTH);
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

		return Unpooled.wrappedBuffer(out.toByteArray());
	}

	private ByteBuf encryptRc4client(ChannelHandlerContext ctx, RTMessage msg) {
		byte id = (byte) (msg.getId().getValue() | (byte) 0x80);

		// If the RT ID is a CLIENT DISCONNECT, do not add 0x80 to the ID.
		if (msg.getId() == RtMessageId.CLIENT_DISCONNECT) {
			id = msg.getId().getValue();
		}

		PS2_RC4 keyToEncryptWith = client.getRC4ClientSessionKey();

		byte[] length = Utils.shortToBytesLittle((short) msg.getLength());

		byte[] key = Utils.nettyByteBufToByteArray(msg.getPayload());

		SCERTEncryptedData scertData = keyToEncryptWith.encrypt(key);
		byte[] hash = scertData.getHash();
		byte[] encryptedData = scertData.getData();

		if (!scertData.isSuccessful()) {
			logger.severe("Encryption not successful!");
		}

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

		return Unpooled.wrappedBuffer(out.toByteArray());
	}

	private ByteBuf encryptRc4server(ChannelHandlerContext ctx, RTMessage msg) {
		byte id = (byte) (msg.getId().getValue() | (byte) 0x80);

		// If the RT ID is a CLIENT DISCONNECT, do not add 0x80 to the ID.
		if (msg.getId() == RtMessageId.CLIENT_DISCONNECT) {
			id = msg.getId().getValue();
		}

		PS2_RC4 keyToEncryptWith = client.getRC4ServerSessionKey();

		byte[] length = Utils.shortToBytesLittle((short) msg.getLength());

		byte[] key = Utils.nettyByteBufToByteArray(msg.getPayload());

		SCERTEncryptedData scertData = keyToEncryptWith.encrypt(key);
		byte[] hash = scertData.getHash();
		byte[] encryptedData = scertData.getData();

		if (!scertData.isSuccessful()) {
			logger.severe("Encryption not successful!");
		}

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

		return Unpooled.wrappedBuffer(out.toByteArray());
	}

}
