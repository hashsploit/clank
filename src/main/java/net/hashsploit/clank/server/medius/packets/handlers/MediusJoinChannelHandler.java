package net.hashsploit.clank.server.medius.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class MediusJoinChannelHandler extends MediusPacketHandler {

	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
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

		buf.get(messageID);
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

			outputStream.write(Utils.intToBytesLittle(10078)); // port

			outputStream.write(worldId); // world id
			
			//outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000FFFFFFFF0B000000")); // ???
			outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000FFFFFFFF0B000000")); // ???
			//outputStream.write(Utils.hexStringToByteArray("000000000000000000000000000000000000000000000000")); // ???

			//outputStream.write(Utils.hexStringToByteArray("CF09ADE3EA5551113BBB519AC5BCB0CB45CD22DD399257E74E886684A12A3E68A865F487CE86777545D6CBFD90C2C6186F7D05E82419DB2E2230E7F73CCF8BB33333323837")); // RSA_KEY 64
			
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
