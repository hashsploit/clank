package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.CreateClanRequest;
import net.hashsploit.clank.server.common.packets.serializers.CreateClanResponse;
import net.hashsploit.clank.server.common.packets.serializers.VersionServerRequest;
import net.hashsploit.clank.server.common.packets.serializers.VersionServerResponse;

public class MediusCreateClanHandler extends MediusPacketHandler {

	private CreateClanRequest reqPacket;
	private CreateClanResponse respPacket;
	
	public MediusCreateClanHandler() {
		super(MediusMessageType.CreateClan, MediusMessageType.CreateClanResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new CreateClanRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.REQUEST_DENIED;
		int clanId = 0;
		
		
		
		
		
		
		callbackStatus = MediusCallbackStatus.SUCCESS;
		clanId = 1;
		
		
		
		respPacket = new CreateClanResponse(reqPacket.getMessageId(), callbackStatus, clanId);
		client.sendMediusMessage(respPacket);
	}
}
