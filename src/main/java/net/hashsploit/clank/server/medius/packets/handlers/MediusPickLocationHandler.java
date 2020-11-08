package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.packets.serializers.PickLocationRequest;
import net.hashsploit.clank.server.medius.packets.serializers.PickLocationResponse;

public class MediusPickLocationHandler extends MediusPacketHandler {

	private PickLocationRequest reqPacket;
	private PickLocationResponse respPacket;
	
	public MediusPickLocationHandler() {
		super(MediusPacketType.PickLocation, MediusPacketType.PickLocationResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new PickLocationRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		// TODO: return a valid callback status
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.MediusSuccess;
		
		respPacket = new PickLocationResponse(reqPacket.getMessageId(), callbackStatus);
		
		return respPacket;
	}


}
