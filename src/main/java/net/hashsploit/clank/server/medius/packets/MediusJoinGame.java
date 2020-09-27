package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusJoinGame extends MediusPacket {

	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	byte[] worldIdToJoin = new byte[4];
	byte[] joinType = new byte[4];
	byte[] gamePassword = new byte[MediusConstants.GAMEPASSWORD_MAXLEN.getValue()];
	byte[] gameHostType = new byte[4];
	byte[] RSApubKey = new byte[10];
	
	public MediusJoinGame() {
		super(MediusPacketType.JoinGame, MediusPacketType.JoinGameResponse);
		
	}
	
	public void read(MediusMessage mm) {
		// Process the packet
		logger.fine("Get my clans: " + Utils.bytesToHex(mm.getPayload()));

		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(sessionKey);
		buf.get(new byte[2]);//padding
		buf.get(worldIdToJoin);
		buf.get(joinType);
		buf.get(gamePassword);
		buf.get(gameHostType);
		logger.fine("Message ID : " + Utils.bytesToHex(messageID));
		logger.fine("worldIdToJoin: " + Utils.bytesToHex(worldIdToJoin));
		logger.fine("joinType: " + Utils.bytesToHex(joinType));
		logger.fine("gamePassword: " + Utils.bytesToHex(gamePassword));
		logger.fine("gameHostType: " + Utils.bytesToHex(gameHostType));
		logger.fine("Data: " + Utils.bytesToHex(mm.getPayload()));
	}

	@Override
	public MediusMessage write(MediusClient client) {
		// RESPONSE

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] callbackStatus = Utils.intToBytesLittle(0);

		byte[] ipAddr = Clank.getInstance().getConfig().getAddress().getBytes();
		int numZeros = 16 - Clank.getInstance().getConfig().getAddress().toString().length();
		String zeroString = new String(new char[numZeros]).replace("\0", "00");
		byte[] zeroTrail = Utils.hexStringToByteArray(zeroString);

		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);

			outputStream.write(Utils.hexStringToByteArray("01000000")); // net connection type (int/little endian)

			outputStream.write(Utils.hexStringToByteArray("01000000")); // net address type (int/little endian)

			outputStream.write(ipAddr); // ip address
			outputStream.write(zeroTrail); // zero padding for ip address

			outputStream.write(Utils.intToBytesLittle(10079)); // port

			outputStream.write(worldIdToJoin); // world id
			
			outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000FFFFFFFF0B000000")); // ???

			//outputStream.write(Utils.hexStringToByteArray("CF09ADE3EA5551113BBB519AC5BCB0CB45CD22DD399257E74E886684A12A3E68A865F487CE86777545D6CBFD90C2C6186F7D05E82419DB2E2230E7F73CCF8BB33333323837")); // RSA_KEY 64
			
			outputStream.write(Utils.hexStringToByteArray("000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")); // RSA_KEY 64

			outputStream.write(Utils.hexStringToByteArray("0000000000000000000000000000000000")); // aSessionKey

			outputStream.write(Utils.hexStringToByteArray("782B6F2F532F71443453633243364B4E00")); // aAccessKey

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new MediusMessage(responseType, outputStream.toByteArray());

	}

}
