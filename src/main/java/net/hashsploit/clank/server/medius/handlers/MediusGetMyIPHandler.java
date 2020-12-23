package net.hashsploit.clank.server.medius.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.GetMyIPRequest;
import net.hashsploit.clank.server.medius.serializers.GetMyIPResponse;

public class MediusGetMyIPHandler extends MediusPacketHandler {

	private GetMyIPRequest reqPacket;
	private GetMyIPResponse respPacket;
	
	public MediusGetMyIPHandler() {
		super(MediusMessageType.GetMyIP, MediusMessageType.GetMyIPResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new GetMyIPRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {
		
		String ipAddress = client.getIPAddress();
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		
		respPacket = new GetMyIPResponse(reqPacket.getMessageId(), ipAddress, callbackStatus);
		
		client.sendMediusMessage(respPacket);
	}


}
