package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.SetLocalizationParamsRequest;
import net.hashsploit.clank.server.medius.serializers.SetLocalizationParamsResponse;

public class MediusSetLocalizationParamsHandler extends MediusPacketHandler {

	private SetLocalizationParamsRequest reqPacket;
	private SetLocalizationParamsResponse respPacket;
	
	public MediusSetLocalizationParamsHandler() {
		super(MediusMessageType.SetLocalizationParams, MediusMessageType.SetLocalizationParamsResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new SetLocalizationParamsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		
		respPacket = new SetLocalizationParamsResponse(reqPacket.getMessageId(), callbackStatus);

		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}

}
