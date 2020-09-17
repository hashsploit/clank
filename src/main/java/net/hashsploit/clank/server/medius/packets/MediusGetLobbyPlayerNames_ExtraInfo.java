package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPlayerOnlineState;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.utils.Utils;

public class MediusGetLobbyPlayerNames_ExtraInfo extends MediusPacket {

	private static final Logger logger = Logger.getLogger("");

	private byte[] responsePacketType = MediusPacketType.GetLobbyPlayerNames_ExtraInfoResponse.getShortByte();
	private byte[] messageID;
	private int worldId;
	
	
	public MediusGetLobbyPlayerNames_ExtraInfo() {
		super(MediusPacketType.GetLobbyPlayerNames_ExtraInfo);
	}
	@Override
	public void read(byte[] packetData) {
		ByteBuffer buf = ByteBuffer.wrap(packetData);

		byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
		byte[] empty = new byte[3];
		byte[] worldIdBytes = new byte[4];

		buf.get(messageID);
		this.messageID = messageID;
		buf.get(empty);
		buf.get(worldIdBytes);
		
		worldId = Utils.bytesToIntLittle(worldIdBytes);
		
	}
	
	@Override
	public void write(Client client, ChannelHandlerContext ctx) {
		// RESPONSE
		
		byte[] callbackStatus = Utils.intToBytesLittle(0);
		byte[] accountID = Utils.intToBytesLittle(101);
		byte[] accountName = Utils.buildByteArrayFromString("Smily", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
		MediusPlayerOnlineState onlineState = new MediusPlayerOnlineState(MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD, 0, 0, "Aquatos v2", "Aquatos v2");
		byte[] connectState = Utils.intToBytesLittle(onlineState.getConnectionStatus().getValue());
		byte[] lobbyWorldID = Utils.intToBytesLittle(worldId);
		byte[] gameWorldID = Utils.intToBytesLittle(0);
		byte[] lobbyName = Utils.buildByteArrayFromString("CY00000000-00", MediusConstants.WORLDNAME_MAXLEN.getValue());
		byte[] gameName = Utils.buildByteArrayFromString("", MediusConstants.WORLDNAME_MAXLEN.getValue());
		byte[] endOfList = Utils.hexStringToByteArray("01000000");

		logger.finest("Message ID: " + Utils.bytesToHex(messageID));
		logger.finest("Account ID: " + Utils.bytesToHex(accountID));
		logger.finest("AccountName: " + Utils.bytesToHex(accountName));
		logger.finest("ConnectState: " + Utils.bytesToHex(connectState));
		logger.finest("lobbyWorldID: " + Utils.bytesToHex(lobbyWorldID));
		logger.finest("gameWorldID: " + Utils.bytesToHex(gameWorldID));
		logger.finest("lobbyName: " + Utils.bytesToHex(lobbyName));
		logger.finest("gameName: " + Utils.bytesToHex(gameName));
		logger.finest("endOfList: " + Utils.bytesToHex(endOfList));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(responsePacketType);
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);
			outputStream.write(accountID);
			outputStream.write(accountName);
			outputStream.write(connectState);
			outputStream.write(lobbyWorldID);
			outputStream.write(gameWorldID);
			outputStream.write(lobbyName);
			outputStream.write(gameName);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Combine RT id and len
		byte[] data = outputStream.toByteArray();
		logger.finest("Final Payload Length: " + Integer.toString(data.length));
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, data);

		byte[] finalPayload = packet.toData().array();
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
		ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
		ctx.write(msg);
		ctx.flush();
	}
	
	
	@Override
	public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) {
		read(packetData);
		write(client, ctx);
		return;




	}

}
