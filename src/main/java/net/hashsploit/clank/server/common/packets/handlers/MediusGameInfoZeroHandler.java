package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusMessage;
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
	public void read(MediusMessage mm) {
		reqPacket = new GameInfoZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusMessage write(MediusClient client) {		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));
		byte[] appID = Utils.hexStringToByteArray("bc290000");
		byte[] minPlayers = Utils.hexStringToByteArray("00000000");
		byte[] maxPlayers = Utils.hexStringToByteArray("08000000");
		//byte[] gameLevel = Utils.hexStringToByteArray("45070000");
		byte[] gameLevel = Utils.hexStringToByteArray("7b000000");
		byte[] playerSkillLevel = Utils.hexStringToByteArray("00000000");
		byte[] playerCount = Utils.hexStringToByteArray("10000000");
		byte[] gameStats = Utils.buildByteArrayFromString("", MediusConstants.GAMESTATS_MAXLEN.getValue());
		// Contains "username's" game string in this case, 'hashsploits' with 0x20 for spaces
		byte[] gameName = Utils.hexStringToByteArray("6861736873706c6f6974277320202020202030303030303032383030303000000000000000000000000000000000000000000000000000000000000000000000");
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
