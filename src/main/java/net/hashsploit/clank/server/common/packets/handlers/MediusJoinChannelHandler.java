package net.hashsploit.clank.server.common.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.objects.NetAddressType;
import net.hashsploit.clank.server.common.objects.NetConnectionType;
import net.hashsploit.clank.utils.Utils;

public class MediusJoinChannelHandler extends MediusPacketHandler {

	byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	byte[] buffer = new byte[2];
	byte[] worldId = new byte[4];
	byte[] lobbyChannelPassword = new byte[MediusConstants.LOBBYPASSWORD_MAXLEN.getValue()];

	public MediusJoinChannelHandler() {
		super(MediusPacketType.JoinChannel, MediusPacketType.JoinChannelResponse);
	}

	@Override
	public void read(MediusPacket mm) {
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());

		buf.get(messageId);
		buf.get(sessionKey);
		buf.get(buffer);
		buf.get(worldId);
		buf.get(lobbyChannelPassword);
	}

	@Override
	public MediusPacket write(MediusClient client) {
		// RESPONSE

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] callbackStatus = Utils.intToBytesLittle(0);
		final String ipAddrStr = ((MediusConfig) Clank.getInstance().getConfig()).getAddress();
		
		byte[] ipAddr = ipAddrStr.getBytes();
		int numZeros = 16 - ipAddrStr.length();
		String zeroString = new String(new char[numZeros]).replace("\0", "00");
		byte[] zeroTrail = Utils.hexStringToByteArray(zeroString);

		try {
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);

			outputStream.write(Utils.intToBytesLittle(NetConnectionType.NET_CONNECTION_TYPE_CLIENT_SERVER_TCP.getValue())); // net connection type (int/little endian)

			outputStream.write(Utils.intToBytesLittle(NetAddressType.NET_ADDRESS_TYPE_EXTERNAL.getValue())); // net address type (int/little endian)

			outputStream.write(ipAddr); // ip address
			outputStream.write(zeroTrail); // zero padding for ip address

			outputStream.write(Utils.intToBytesLittle(10078)); // port

			outputStream.write(worldId); // world id

			// outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000FFFFFFFF0B000000"));
			// // ???
			outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000FFFFFFFF0B000000")); // ???
			// outputStream.write(Utils.hexStringToByteArray("000000000000000000000000000000000000000000000000"));
			// // ???

			// outputStream.write(Utils.hexStringToByteArray("CF09ADE3EA5551113BBB519AC5BCB0CB45CD22DD399257E74E886684A12A3E68A865F487CE86777545D6CBFD90C2C6186F7D05E82419DB2E2230E7F73CCF8BB33333323837"));
			// // RSA_KEY 64

			outputStream.write(Utils.hexStringToByteArray("000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")); // RSA_KEY 64

			outputStream.write(Utils.hexStringToByteArray("0000000000000000000000000000000000")); // aSessionKey

			outputStream.write(Utils.hexStringToByteArray("782B6F2F532F71443453633243364B4E00")); // aAccessKey

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			outputStream.write(messageID);
//			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
//			outputStream.write(callbackStatus);
//
//			outputStream.write(Utils.hexStringToByteArray("01000000")); // net connection type (int/little endian)
//
//			outputStream.write(Utils.hexStringToByteArray("01000000")); // net address type (int/little endian)
//
//			outputStream.write(ipAddr); // ip address
//			outputStream.write(zeroTrail); // zero padding for ip address
//
//			outputStream.write(Utils.intToBytesLittle(10078)); // port
//
//			outputStream.write(worldId); // world id
//			
//			outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000FFFFFFFF0B000000")); // ???
//
//			//outputStream.write(Utils.hexStringToByteArray("CF09ADE3EA5551113BBB519AC5BCB0CB45CD22DD399257E74E886684A12A3E68A865F487CE86777545D6CBFD90C2C6186F7D05E82419DB2E2230E7F73CCF8BB33333323837")); // RSA_KEY 64
//			
//			outputStream.write(Utils.hexStringToByteArray("000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")); // RSA_KEY 64
//
//			outputStream.write(Utils.hexStringToByteArray("0000000000000000000000000000000000")); // aSessionKey
//
//			outputStream.write(Utils.hexStringToByteArray("782B6F2F532F71443453633243364B4E00")); // aAccessKey
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return new MediusPacket(responseType, outputStream.toByteArray());
	}

}
