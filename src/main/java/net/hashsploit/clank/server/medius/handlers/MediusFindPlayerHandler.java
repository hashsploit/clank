package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusApplicationType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.FindPlayerRequest;
import net.hashsploit.clank.server.medius.serializers.FindPlayerResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusFindPlayerHandler extends MediusPacketHandler {

	private FindPlayerRequest reqPacket;
	private FindPlayerResponse respPacket;

	public MediusFindPlayerHandler() {
		super(MediusMessageType.FindPlayer, MediusMessageType.FindPlayerResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage message) {
		reqPacket = new FindPlayerRequest(message.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
		byte[] applicationId = Utils.hexStringToByteArray("00000000");
		byte[] applicationName = Utils.buildByteArrayFromString("", MediusConstants.APPNAME_MAXLEN.value);
		byte[] applicationType = Utils.intToBytesLittle(MediusApplicationType.MEDIUS_APP_TYPE_GAME.value);
		byte[] worldId = Utils.intToBytesLittle(0);
		byte[] accountID = reqPacket.getAccountId();
		byte[] accountName = Utils.buildByteArrayFromString("Test name", MediusConstants.ACCOUNTNAME_MAXLEN.value);
		byte[] endOfList = Utils.intToBytesLittle(1);

		respPacket = new FindPlayerResponse(reqPacket.getMessageID(), callbackStatus, applicationId, applicationName, applicationType, worldId, accountID,
				reqPacket.getAccountName(), endOfList);
		
		List<MediusMessage> responses = new ArrayList<MediusMessage>();
		responses.add(respPacket);
		return responses;
	}

}
