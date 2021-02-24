package net.hashsploit.clank.server.medius.handlers;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusAuthenticationServer;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.AccountLoginRequest;
import net.hashsploit.clank.server.medius.serializers.AccountLoginResponse;
import net.hashsploit.clank.server.medius.serializers.LadderList_ExtraInfoRequest;
import net.hashsploit.clank.server.medius.serializers.LadderList_ExtraInfoResponse;
import net.hashsploit.clank.server.rpc.PlayerLoginResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusLadderList_ExtraInfoHandler extends MediusPacketHandler {
	
	private static final Logger logger = Logger.getLogger(MediusLadderList_ExtraInfoHandler.class.getName());

	private LadderList_ExtraInfoRequest reqPacket;
	private LadderList_ExtraInfoResponse respPacket;

	public MediusLadderList_ExtraInfoHandler() {
		super(MediusMessageType.LadderList_ExtraInfo, MediusMessageType.LadderList_ExtraInfoResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new LadderList_ExtraInfoRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		byte[] messageID = reqPacket.getMessageId();
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));
		byte[] ladderPosition = Utils.intToBytesLittle(1);
		byte[] ladderStat = Utils.intToBytesLittle(1);
		byte[] accountId = Utils.intToBytes(1);
		byte[] accountName = Utils.buildByteArrayFromString("Test", MediusConstants.ACCOUNTNAME_MAXLEN.value);
		byte[] accountStats = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTSTATS_MAXLEN.value);
		byte[] onlineStateFields = Utils.hexStringToByteArray("00000000" + "00000000" + "00000000");
		byte[] onlineStateLobbyName = Utils.buildByteArrayFromString("", MediusConstants.WORLDNAME_MAXLEN.value);
		byte[] onlineStateGameName = Utils.buildByteArrayFromString("", MediusConstants.WORLDNAME_MAXLEN.value);
		byte[] allByteArray = new byte[onlineStateFields.length + onlineStateLobbyName.length + onlineStateGameName.length];
		ByteBuffer buff = ByteBuffer.wrap(allByteArray);
		buff.put(onlineStateFields);
		buff.put(onlineStateLobbyName);
		buff.put(onlineStateGameName);
		byte[] onlineState = buff.array();
		byte[] endOfList = Utils.hexStringToByteArray("01000000");
		respPacket = new LadderList_ExtraInfoResponse(messageID, callbackStatus, ladderPosition, ladderStat, accountId, accountName, accountStats, onlineState, endOfList);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}

}
