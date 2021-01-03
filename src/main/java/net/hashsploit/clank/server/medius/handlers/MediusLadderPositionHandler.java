package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.LadderPositionRequest;
import net.hashsploit.clank.server.medius.serializers.LadderPositionResponse;

public class MediusLadderPositionHandler extends MediusPacketHandler {
	
	private LadderPositionRequest reqPacket;
	private LadderPositionResponse respPacket;
	
	private byte[] messageId;
	
	public MediusLadderPositionHandler() {
		super(MediusMessageType.LadderPosition, MediusMessageType.LadderPositionResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new LadderPositionRequest(mm.getPayload());
		
		
		messageId = reqPacket.getMessageId();
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		respPacket = new LadderPositionResponse(messageId);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}
	
}
