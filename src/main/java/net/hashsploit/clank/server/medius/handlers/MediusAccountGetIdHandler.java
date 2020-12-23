package net.hashsploit.clank.server.medius.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.AccountGetIdRequest;
import net.hashsploit.clank.server.medius.serializers.AccountGetIdResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountGetIdHandler extends MediusPacketHandler {

	private AccountGetIdRequest reqPacket;
	private AccountGetIdResponse respPacket;

	public MediusAccountGetIdHandler() {
		super(MediusMessageType.AccountGetId, MediusMessageType.AccountGetIdResponse);
	}

	@Override
	public void read(MediusMessage mm) {
		reqPacket = new AccountGetIdRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public void write(MediusClient client) {

		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));
		byte[] mlsToken = Utils.hexStringToByteArray("00000000000000000000000000000000000000");

		respPacket = new AccountGetIdResponse(reqPacket.getMessageID(), Utils.intToBytesLittle(9), callbackStatus);
		client.sendMediusMessage(respPacket);
	}

}
