package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.packets.serializers.AccountRegistrationRequest;
import net.hashsploit.clank.server.medius.packets.serializers.AccountRegistrationResponse;
import net.hashsploit.clank.server.medius.packets.serializers.LadderPositionRequest;
import net.hashsploit.clank.server.medius.packets.serializers.LadderPositionResponse;

public class MediusLadderPosition extends MediusPacketHandler {
	
	private LadderPositionRequest reqPacket;
	private LadderPositionResponse respPacket;
	
	private byte[] messageId;
	
	public MediusLadderPosition() {
		super(MediusPacketType.LadderPosition, MediusPacketType.LadderPositionResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new LadderPositionRequest(mm.getPayload());
		
		
		messageId = reqPacket.getMessageId();
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		respPacket = new LadderPositionResponse(messageId);
		
		return respPacket;
	}
	
}
