package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.SessionEndRequest;
import net.hashsploit.clank.server.medius.serializers.SessionEndResponse;

public class MediusSessionEndHandler extends MediusPacketHandler {

	private SessionEndRequest request;
	
	public MediusSessionEndHandler() {
		super(MediusMessageType.SessionEnd, MediusMessageType.SessionEndResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		request = new SessionEndRequest(mm.getPayload());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		
		List<MediusMessage> responses = new ArrayList<MediusMessage>();
		
		SessionEndResponse response = new SessionEndResponse(request.getMessageId(), callbackStatus);
		
		responses.add(response);
		
		return responses;
	}


}
