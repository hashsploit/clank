package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.packets.serializers.PickLocationRequest;
import net.hashsploit.clank.server.common.packets.serializers.PickLocationResponse;

public class MediusPickLocationHandler extends MediusPacketHandler {

	private PickLocationRequest reqPacket;
	private PickLocationResponse respPacket;
	
	public MediusPickLocationHandler() {
		super(MediusMessageType.PickLocation, MediusMessageType.PickLocationResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new PickLocationRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		// TODO: return a valid callback status
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		
		respPacket = new PickLocationResponse(reqPacket.getMessageId(), callbackStatus);
		
		return respPacket;
	}


}
