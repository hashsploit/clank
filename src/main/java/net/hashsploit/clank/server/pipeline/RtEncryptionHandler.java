package net.hashsploit.clank.server.pipeline;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
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
		byte id = (byte) (msg.getId().getValue() | (byte) 0x80);
		if (msg.getId() != RtMessageId.CLIENT_DISCONNECT) {
			id = msg.getId().getValue();
		}

		PS2_RSA keyToEncryptWith = client.getPs2RsaKey();
		
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
	}
	
}
