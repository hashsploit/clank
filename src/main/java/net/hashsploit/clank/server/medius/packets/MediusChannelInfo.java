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
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.utils.Utils;

public class MediusChannelInfo extends MediusPacket {

	private static final Logger logger = Logger.getLogger("");

	private byte[] responsePacketType = MediusPacketType.ChannelInfoResponse.getShortByte();

	private byte[] messageID;
	private byte[] sessionKey;
	private int worldID;
	
	
	public MediusChannelInfo() {
		super(MediusPacketType.ChannelInfo);
	}
	
	@Override
	public void read(byte[] packetData) {
		ByteBuffer buf = ByteBuffer.wrap(packetData);

		messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
		sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
		byte[] buffer = new byte[2];
		byte[] worldIdBuf = new byte[4];

		buf.get(messageID);
		buf.get(sessionKey);
		buf.get(buffer);
		buf.get(worldIdBuf);
		worldID = Utils.bytesToIntLittle(worldIdBuf);
		
		logger.finest("ChannelInfo data read:");
		logger.finest("Message ID : " + Utils.bytesToHex(messageID) + " | Length: " + Integer.toString(messageID.length));
		logger.finest("Session Key: " + Utils.bytesToHex(sessionKey) + " | Length: " + Integer.toString(sessionKey.length));
		logger.finest("LobbyChannelWorldID: " + Utils.bytesToHex(Utils.intToBytesLittle(worldID)));
	}
	
	@Override
	public void write(Client client, ChannelHandlerContext ctx) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.MediusSuccess.getValue()));
		byte[] lobbyName = Utils.hexStringToByteArray("435930303030303030332D30300000000000000000000000000000000000000020000000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000010100000000");
		byte[] activePlayerCount = Utils.intToBytesLittle(1);
		byte[] maxPlayers = Utils.intToBytesLittle(224);
		
		logger.finest("Writing ChannelInfo OUT:");
		logger.finest("CallbackStatus : " + Utils.bytesToHex(callbackStatus) + " | Length: " + Integer.toString(callbackStatus.length));
		logger.finest("lobbyName : " + Utils.bytesToHex(lobbyName) + " | Length: " + Integer.toString(lobbyName.length));
		logger.finest("activePlayerCount : " + Utils.bytesToHex(activePlayerCount) + " | Length: " + Integer.toString(activePlayerCount.length));
		logger.finest("maxPlayers : " + Utils.bytesToHex(maxPlayers) + " | Length: " + Integer.toString(maxPlayers.length));
		
		try {
			outputStream.write(responsePacketType);
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
	
		byte[] data = outputStream.toByteArray();
		logger.finest("Final Payload Length: " + Integer.toString(data.length));
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, data);

		byte[] finalPayload = packet.toData().array();
		logger.finest("Final payload: " + Utils.bytesToHex(finalPayload));
		ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
		ctx.write(msg);
		ctx.flush();	
	}

	@Override
	public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) {
		read(packetData);
		write(client, ctx);
	}

}
