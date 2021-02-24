package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.AccountRegistrationRequest;
import net.hashsploit.clank.server.medius.serializers.AccountRegistrationResponse;

public class MediusAccountRegistrationHandler extends MediusPacketHandler {

	private AccountRegistrationRequest reqPacket;
	private AccountRegistrationResponse respPacket;
	
	private byte[] messageId;
	
	public MediusAccountRegistrationHandler() {
		super(MediusMessageType.AccountLogin, MediusMessageType.AccountLoginResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new AccountRegistrationRequest(mm.getPayload());
		messageId = reqPacket.getMessageID();
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		respPacket = new AccountRegistrationResponse(messageId);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}


}
