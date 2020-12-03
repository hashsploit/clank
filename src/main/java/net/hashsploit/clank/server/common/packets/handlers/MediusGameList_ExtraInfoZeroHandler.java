package net.hashsploit.clank.server.common.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.GameHostType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.common.packets.serializers.GameList_ExtraInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameList_ExtraInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGameList_ExtraInfoZeroHandler extends MediusPacketHandler {
	
	private GameList_ExtraInfoZeroRequest reqPacket;
	private GameList_ExtraInfoZeroResponse respPacket;
	
	public MediusGameList_ExtraInfoZeroHandler() {
		super(MediusMessageType.GameList_ExtraInfo0,MediusMessageType.GameList_ExtraInfoResponse0);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new GameList_ExtraInfoZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusMessage write(MediusClient client) {

		byte[] mediusWorldID = Utils.hexStringToByteArray("65eb0000");
		byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
		byte[] playerCount = Utils.shortToBytesLittle((short) 1);
		byte[] minPlayers = Utils.shortToBytesLittle((short) 0);
		byte[] maxPlayers = Utils.shortToBytesLittle((short) 8);
		byte[] gameLevel = Utils.intToBytesLittle(client.getServer().getLogicHandler().getCityWorldId());
		byte[] playerSkillLevel = Utils.hexStringToByteArray("00000004");
		byte[] rulesSet = Utils.intToBytesLittle(0);
		byte[] genericField1 = Utils.intToBytesLittle(28);// Generic field 1 (Location Id (city id; aquatos))
		byte[] genericField2 = Utils.intToBytesLittle(0);
		byte[] genericField3 = Utils.hexStringToByteArray("0800e411");
		byte[] worldSecurityLevelType = Utils.hexStringToByteArray("00000000");
		byte[] worldStatus = Utils.intToBytesLittle(MediusWorldStatus.WORLD_STAGING.getValue());
		byte[] gameHostType = Utils.intToBytesLittle(GameHostType.HOST_CLIENT_SERVER_AUX_UDP.getValue());
		//byte[] gameName = Utils.hexStringToByteArray("31763120662e6f20646f782020202020202030303030303032383030303000000000000000000000000000000000000000000000000000000000000000000000");
		byte[] gameName = Utils.buildByteArrayFromString("1v1 f.o dox       000000280000",MediusConstants.GAMENAME_MAXLEN.getValue());
		byte[] gameStats = Utils.buildByteArrayFromString("", MediusConstants.GAMESTATS_MAXLEN.getValue());
		byte[] endOfList = Utils.hexStringToByteArray("01000000");

		respPacket = new GameList_ExtraInfoZeroResponse(reqPacket.getMessageID(), mediusWorldID, callbackStatus, playerCount, minPlayers, maxPlayers,
				gameLevel, playerSkillLevel, rulesSet, genericField1, genericField2, genericField3, worldSecurityLevelType, worldStatus,
				gameHostType, gameName, gameStats, endOfList);
		return respPacket;	
		
	}

}
