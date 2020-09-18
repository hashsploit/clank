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
import net.hashsploit.clank.server.medius.objects.MediusMessage;
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
	public MediusMessage write(Client client) {
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
//			outputStream.write(Utils.hexStringToByteArray("000000"));
//			outputStream.write(Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue()));
//			outputStream.write(mediusWorldID);
//			outputStream.write(playerCount);
//			outputStream.write(minPlayers);
//			outputStream.write(maxPlayers);
//			outputStream.write(gameLevel);
//			outputStream.write(playerSkillLevel);
//			outputStream.write(rulesSet);
//			outputStream.write(genericField1);
//			outputStream.write(genericField2);
//			outputStream.write(genericField3);
//			outputStream.write(genericField4);
//			outputStream.write(genericField5);
//			outputStream.write(genericField6);
//			outputStream.write(genericField7);
//			outputStream.write(genericField8);
//			outputStream.write(worldSecurityLevelType);
//			outputStream.write(worldStatus);
//			outputStream.write(gameHostType);
//			outputStream.write(gameName);
//			outputStream.write(gameStats);
//			outputStream.write(endOfList);
			//outputStream.write(Utils.hexStringToByteArray("00000001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000"));
			outputStream.write(Utils.hexStringToByteArray("0000000000000065EB0000020000000800000045070000000000040000000028000000000000000800E41100000000010000000400000031763120662e6f20646f7820202020202020303030303030323830303030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000"));
		} catch (IOException e) {
			
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new MediusMessage(responseType, outputStream.toByteArray());
	}

}
