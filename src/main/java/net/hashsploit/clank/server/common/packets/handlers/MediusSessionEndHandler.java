package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.SessionEndRequest;
import net.hashsploit.clank.server.common.packets.serializers.SessionEndResponse;

public class MediusSessionEndHandler extends MediusPacketHandler {

	private SessionEndRequest reqPacket;
	private SessionEndResponse respPacket;
	
	public MediusSessionEndHandler() {
		super(MediusMessageType.SessionEnd, MediusMessageType.SessionEndResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new SessionEndRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		
		respPacket = new SessionEndResponse(reqPacket.getMessageId(), callbackStatus);
		
		client.sendMediusMessage(respPacket);
	}


}