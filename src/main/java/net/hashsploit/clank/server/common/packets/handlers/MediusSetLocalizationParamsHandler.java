package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.SetLocalizationParamsRequest;
import net.hashsploit.clank.server.common.packets.serializers.SetLocalizationParamsResponse;

public class MediusSetLocalizationParamsHandler extends MediusPacketHandler {

	private SetLocalizationParamsRequest reqPacket;
	private SetLocalizationParamsResponse respPacket;
	
	public MediusSetLocalizationParamsHandler() {
		super(MediusMessageType.SetLocalizationParams, MediusMessageType.SetLocalizationParamsResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new SetLocalizationParamsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		
		respPacket = new SetLocalizationParamsResponse(reqPacket.getMessageId(), callbackStatus);
		
		client.sendMediusMessage(respPacket);
	}


}
