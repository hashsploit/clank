package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

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
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new GetMyIPRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		String ipAddress = client.getIPAddress();
		int port = client.getPort();
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		
		respPacket = new GetMyIPResponse(reqPacket.getMessageId(), ipAddress, port, callbackStatus);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}


}
