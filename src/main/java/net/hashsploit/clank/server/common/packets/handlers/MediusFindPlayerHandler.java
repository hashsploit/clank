package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusApplicationType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.FindPlayerRequest;
import net.hashsploit.clank.server.common.packets.serializers.FindPlayerResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusFindPlayerHandler extends MediusPacketHandler {

	private FindPlayerRequest reqPacket;
	private FindPlayerResponse respPacket;

	public MediusFindPlayerHandler() {
		super(MediusMessageType.FindPlayer, MediusMessageType.FindPlayerResponse);
	}

	@Override
	public void read(MediusMessage mm) {
		reqPacket = new FindPlayerRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public void write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
		byte[] applicationId = Utils.hexStringToByteArray("00000000");
		byte[] applicationName = Utils.buildByteArrayFromString("", MediusConstants.APPNAME_MAXLEN.getValue());
		byte[] applicationType = Utils.intToBytesLittle(MediusApplicationType.MEDIUS_APP_TYPE_GAME.getValue());
		byte[] worldId = Utils.intToBytesLittle(0);
		byte[] accountId = reqPacket.getAccountId();
		//byte[] accountName = Utils.buildByteArrayFromString("Test name", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
		byte[] endOfList = Utils.intToBytesLittle(1);

		respPacket = new FindPlayerResponse(reqPacket.getMessageID(), callbackStatus, applicationId, applicationName, applicationType, worldId, accountId, reqPacket.getAccountName(), endOfList);
		client.sendMediusMessage(respPacket);
	}

}
