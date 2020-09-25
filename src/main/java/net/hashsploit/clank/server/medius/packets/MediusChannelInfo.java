package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusChannelInfo extends MediusPacket {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] worldID = new byte[4];
	
	public MediusChannelInfo() {
		super(MediusPacketType.ChannelInfo, MediusPacketType.ChannelInfoResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(sessionKey);
		buf.getShort(); // buffer
		buf.get(worldID);
		
		logger.finest("ChannelInfo data read:");
		logger.finest("Message ID : " + Utils.bytesToHex(messageID) + " | Length: " + Integer.toString(messageID.length));
		logger.finest("Session Key: " + Utils.bytesToHex(sessionKey) + " | Length: " + Integer.toString(sessionKey.length));
		logger.finest("LobbyChannelWorldID: " + Utils.bytesToHex(worldID));
	}
	
	@Override
	public MediusMessage write(MediusClient client) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.MediusSuccess.getValue()));

    	byte[] lobbyName = Utils.buildByteArrayFromString("CY00000000-00", MediusConstants.LOBBYNAME_MAXLEN.getValue());
		byte[] activePlayerCount = Utils.intToBytesLittle(1);
		byte[] maxPlayers = Utils.intToBytesLittle(224);
		
		logger.finest("Writing ChannelInfo OUT:");
		logger.finest("CallbackStatus : " + Utils.bytesToHex(callbackStatus) + " | Length: " + Integer.toString(callbackStatus.length));
		logger.finest("lobbyNamePart1 : " + Utils.bytesToHex(lobbyName) + " | Length: " + Integer.toString(lobbyName.length));
		logger.finest("activePlayerCount : " + Utils.bytesToHex(activePlayerCount) + " | Length: " + Integer.toString(activePlayerCount.length));
		logger.finest("maxPlayers : " + Utils.bytesToHex(maxPlayers) + " | Length: " + Integer.toString(maxPlayers.length));
		
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);
			outputStream.write(lobbyName);
			outputStream.write(activePlayerCount);
			outputStream.write(maxPlayers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return new MediusMessage(responseType, outputStream.toByteArray());
	}


}
