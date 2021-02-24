package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

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
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new AccountGetIdRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));
		byte[] mlsToken = Utils.hexStringToByteArray("00000000000000000000000000000000000000");

		respPacket = new AccountGetIdResponse(reqPacket.getMessageID(), Utils.intToBytesLittle(9), callbackStatus);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}

}
