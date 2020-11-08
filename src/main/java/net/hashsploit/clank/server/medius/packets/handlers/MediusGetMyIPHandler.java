package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.packets.serializers.GetMyIPRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GetMyIPResponse;

public class MediusGetMyIPHandler extends MediusPacketHandler {

	private GetMyIPRequest reqPacket;
	private GetMyIPResponse respPacket;
	
	public MediusGetMyIPHandler() {
		super(MediusPacketType.GetMyIP, MediusPacketType.GetMyIPResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new GetMyIPRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		String ipAddress = client.getIPAddress();
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.MediusSuccess;
		
		respPacket = new GetMyIPResponse(reqPacket.getMessageId(), ipAddress, callbackStatus);
		
		return respPacket;
	}


}
