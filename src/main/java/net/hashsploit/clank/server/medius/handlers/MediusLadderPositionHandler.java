package net.hashsploit.clank.server.medius.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.AccountRegistrationRequest;
import net.hashsploit.clank.server.medius.serializers.AccountRegistrationResponse;
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
	public void read(MediusMessage mm) {
		reqPacket = new LadderPositionRequest(mm.getPayload());
		
		
		messageId = reqPacket.getMessageId();
	}
	
	@Override
	public void write(MediusClient client) {
		
		respPacket = new LadderPositionResponse(messageId);
		
       	client.sendMediusMessage(respPacket);
	}
	
}
