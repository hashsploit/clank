package net.hashsploit.clank.server.medius.packets.handlers;

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
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.GameHostType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.medius.packets.serializers.GameList_ExtraInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameList_ExtraInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGameList_ExtraInfoZeroHandler extends MediusPacketHandler {
	
	private GameList_ExtraInfoZeroRequest reqPacket;
	private GameList_ExtraInfoZeroResponse respPacket;
	
	public MediusGameList_ExtraInfoZeroHandler() {
		super(MediusPacketType.GameList_ExtraInfo0,MediusPacketType.GameList_ExtraInfoResponse0);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new GameList_ExtraInfoZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {

		byte[] mediusWorldID = Utils.hexStringToByteArray("65eb0000");
		byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.MediusSuccess.getValue());
		byte[] playerCount = Utils.shortToBytesLittle((short) 1);
		byte[] minPlayers = Utils.shortToBytesLittle((short) 0);
		byte[] maxPlayers = Utils.shortToBytesLittle((short) 8);
		byte[] gameLevel = Utils.hexStringToByteArray("45070000");
		byte[] playerSkillLevel = Utils.hexStringToByteArray("00000004");
		byte[] rulesSet = Utils.intToBytesLittle(0);
		byte[] genericField1 = Utils.intToBytesLittle(28);// Generic field 1 (Location Id (city id; aquatos))
		byte[] genericField2 = Utils.intToBytesLittle(0);
		byte[] genericField3 = Utils.hexStringToByteArray("0800e411");
		byte[] worldSecurityLevelType = Utils.hexStringToByteArray("00000000");
		byte[] worldStatus = Utils.intToBytesLittle(MediusWorldStatus.WORLD_STAGING.getValue());
		byte[] gameHostType = Utils.intToBytesLittle(GameHostType.HOST_CLIENT_SERVER_AUX_UDP.getValue());
		byte[] gameName = Utils.hexStringToByteArray("31763120662e6f20646f782020202020202030303030303032383030303000000000000000000000000000000000000000000000000000000000000000000000");
		byte[] gameStats = Utils.buildByteArrayFromString("", MediusConstants.GAMESTATS_MAXLEN.getValue());
		byte[] endOfList = Utils.hexStringToByteArray("01000000");

		respPacket = new GameList_ExtraInfoZeroResponse(reqPacket.getMessageID(), mediusWorldID, callbackStatus, playerCount, minPlayers, maxPlayers,
				gameLevel, playerSkillLevel, rulesSet, genericField1, genericField2, genericField3, worldSecurityLevelType, worldStatus,
				gameHostType, gameName, gameStats, endOfList);
		return respPacket;	
		
	}

}
