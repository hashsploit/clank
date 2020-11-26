package net.hashsploit.clank.server.common.packets.handlers;

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
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGameInfoZeroHandler extends MediusPacketHandler {

	private GameInfoZeroRequest reqPacket;
	private GameInfoZeroResponse respPacket;
	
	public MediusGameInfoZeroHandler() {
		super(MediusMessageType.GameInfo0, MediusMessageType.GameInfoResponse0);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new GameInfoZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));
		byte[] appID = Utils.hexStringToByteArray("bc290000");
		byte[] minPlayers = Utils.hexStringToByteArray("00000000");
		byte[] maxPlayers = Utils.hexStringToByteArray("08000000");
		//byte[] gameLevel = Utils.hexStringToByteArray("45070000");
		byte[] gameLevel = Utils.hexStringToByteArray("45070000");
		byte[] playerSkillLevel = Utils.hexStringToByteArray("00000000");
		byte[] playerCount = Utils.hexStringToByteArray("00000000");
		byte[] gameStats = Utils.buildByteArrayFromString("", MediusConstants.GAMESTATS_MAXLEN.getValue());
		byte[] gameName = Utils.hexStringToByteArray("536d696c792773202020202020202020202030303030303032383030303000000000000000000000000000000000000000000000000000000000000000000000");
		byte[] rulesSet = Utils.hexStringToByteArray("00000000");
		byte[] genField1 = Utils.hexStringToByteArray("28000000");
		byte[] genField2 = Utils.hexStringToByteArray("00000000");
		byte[] genField3 = Utils.hexStringToByteArray("0800e411");
		byte[] worldStatus = Utils.hexStringToByteArray("01000000");
		byte[] gameHostType = Utils.hexStringToByteArray("04000000");
		
		respPacket = new GameInfoZeroResponse(reqPacket.getMessageID(), callbackStatus, appID, minPlayers, maxPlayers, gameLevel, 
				playerSkillLevel, playerCount, gameStats, gameName, rulesSet, genField1, genField2, genField3,
				worldStatus, gameHostType);
		
		return respPacket;
	}

}