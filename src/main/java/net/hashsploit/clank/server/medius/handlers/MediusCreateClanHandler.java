package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.CreateClanRequest;
import net.hashsploit.clank.server.medius.serializers.CreateClanResponse;

public class MediusCreateClanHandler extends MediusPacketHandler {

	private CreateClanRequest reqPacket;
	private CreateClanResponse respPacket;
	
	public MediusCreateClanHandler() {
		super(MediusMessageType.CreateClan, MediusMessageType.CreateClanResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new CreateClanRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.REQUEST_DENIED;
		int clanId = 0;
		
		
		
		callbackStatus = MediusCallbackStatus.SUCCESS;
		clanId = 1;
		
		
		
		respPacket = new CreateClanResponse(reqPacket.getMessageId(), callbackStatus, clanId);
		
		List<MediusMessage> responses = new ArrayList<MediusMessage>();
		responses.add(respPacket);
		return responses;
	}
}
