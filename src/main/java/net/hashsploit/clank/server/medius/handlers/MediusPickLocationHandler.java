package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.PickLocationRequest;
import net.hashsploit.clank.server.medius.serializers.PickLocationResponse;

public class MediusPickLocationHandler extends MediusPacketHandler {

	private PickLocationRequest reqPacket;
	private PickLocationResponse respPacket;
	
	public MediusPickLocationHandler() {
		super(MediusMessageType.PickLocation, MediusMessageType.PickLocationResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new PickLocationRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		// TODO: return a valid callback status
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		
		respPacket = new PickLocationResponse(reqPacket.getMessageId(), callbackStatus);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}


}
