package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.packets.serializers.AccountRegistrationRequest;
import net.hashsploit.clank.server.common.packets.serializers.AccountRegistrationResponse;
import net.hashsploit.clank.server.common.packets.serializers.LadderPositionRequest;
import net.hashsploit.clank.server.common.packets.serializers.LadderPositionResponse;

public class MediusLadderPositionHandler extends MediusPacketHandler {
	
	private LadderPositionRequest reqPacket;
	private LadderPositionResponse respPacket;
	
	private byte[] messageId;
	
	public MediusLadderPositionHandler() {
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
