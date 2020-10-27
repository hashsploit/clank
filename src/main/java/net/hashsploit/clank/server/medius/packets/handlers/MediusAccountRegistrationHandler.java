package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.packets.serializers.AccountRegistrationRequest;
import net.hashsploit.clank.server.medius.packets.serializers.AccountRegistrationResponse;

public class MediusAccountRegistrationHandler extends MediusPacketHandler {

	private AccountRegistrationRequest reqPacket;
	private AccountRegistrationResponse respPacket;
	
	private byte[] messageId;
	
	public MediusAccountRegistrationHandler() {
		super(MediusPacketType.AccountLogin, MediusPacketType.AccountLoginResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new AccountRegistrationRequest(mm.getPayload());
		
		
		messageId = reqPacket.getMessageID();
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		respPacket = new AccountRegistrationResponse(messageId);
		
		return respPacket;
	}


}
