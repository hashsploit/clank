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
import net.hashsploit.clank.server.medius.objects.MediusGameHostType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.utils.Utils;

public class MediusGameList_ExtraInfoZero extends MediusPacket {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] pageID = new byte[2];
	private byte[] pageSize = new byte[2];
	
	public MediusGameList_ExtraInfoZero() {
		super(MediusPacketType.GameList_ExtraInfo0,MediusPacketType.GameList_ExtraInfoResponse0);
	}
	
	@Override
	public void read(MediusMessage mm) {
		// Process the packet
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(pageID);
		buf.get(pageSize);
	}
	
	@Override
	public MediusMessage write(MediusClient client) {
		byte[] mediusWorldID = Utils.intToBytesLittle(40);
		byte[] playerCount = Utils.shortToBytesLittle((short) 1);
		byte[] minPlayers = Utils.shortToBytesLittle((short) 1);
		byte[] maxPlayers = Utils.shortToBytesLittle((short) 8);
		byte[] gameLevel = Utils.intToBytesLittle(1);
		byte[] playerSkillLevel = Utils.intToBytesLittle(1);
		byte[] rulesSet = Utils.intToBytesLittle(0);
		byte[] genericField1 = Utils.intToBytesLittle(0);
		byte[] genericField2 = Utils.intToBytesLittle(0);
		byte[] genericField3 = Utils.intToBytesLittle(0);
		byte[] genericField4 = Utils.intToBytesLittle(0);
		byte[] genericField5 = Utils.intToBytesLittle(0);
		byte[] genericField6 = Utils.intToBytesLittle(0);
		byte[] genericField7 = Utils.intToBytesLittle(0);
		byte[] genericField8 = Utils.intToBytesLittle(0);
		byte[] worldSecurityLevelType = Utils.intToBytesLittle(1);
		byte[] worldStatus = Utils.intToBytesLittle(1);
		byte[] gameHostType = Utils.intToBytesLittle(0);
		byte[] gameName = Utils.buildByteArrayFromString("1v1 f.o dox", MediusConstants.GAMENAME_MAXLEN.getValue());
		byte[] gameStats = Utils.buildByteArrayFromString("game stats", MediusConstants.GAMESTATS_MAXLEN.getValue());
		byte endOfList = 0x01;

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			
			outputStream.write(Utils.intToBytesLittle(MediusCallbackStatus.MediusSuccess.getValue())); // Callback status (int)
			outputStream.write(Utils.hexStringToByteArray("65eb0000")); // MediusWorldId (lobby/channel Id)
			
			outputStream.write(Utils.shortToBytesLittle((short) 2)); // Player count
			outputStream.write(Utils.shortToBytesLittle((short) 0)); // Min players
			outputStream.write(Utils.shortToBytesLittle((short) 8)); // Max players
			outputStream.write(Utils.hexStringToByteArray("0000")); // Padding
			
			outputStream.write(Utils.hexStringToByteArray("45070000")); // Game level
			outputStream.write(Utils.hexStringToByteArray("00000004")); // Player skill level (first 3 bytes are disabled weapons, last byte = bolt skill)
			
			outputStream.write(Utils.intToBytesLittle(0)); // Rules set (might be used as team colors or skins)
			outputStream.write(Utils.intToBytesLittle(28)); // Generic field 1 (Location Id (city id; aquatos))
			outputStream.write(Utils.intToBytesLittle(0)); // Generic field 2
			outputStream.write(Utils.hexStringToByteArray("0800e411")); // Generic field 3 (map type, game settings)
			outputStream.write(Utils.hexStringToByteArray("00000000")); // Security level
			outputStream.write(Utils.intToBytesLittle(MediusWorldStatus.WORLD_STAGING.getValue())); // World status
			outputStream.write(Utils.intToBytesLittle(MediusGameHostType.HOST_CLIENT_SERVER_AUX_UDP.getValue())); // Game Host Type
			
			// GameName (contains some ID in it, remainder of the name string buffer is 0x20 [space character]) 
			outputStream.write(Utils.hexStringToByteArray("31763120662e6f20646f782020202020202030303030303032383030303000000000000000000000000000000000000000000000000000000000000000000000"));
			
			// GameStats
			outputStream.write(Utils.buildByteArrayFromString("", MediusConstants.GAMESTATS_MAXLEN.getValue()));
			
			// End of list
			outputStream.write(Utils.intToBytesLittle(1));
		} catch (IOException e) {
			
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new MediusMessage(responseType, outputStream.toByteArray());
	}

}
