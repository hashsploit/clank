package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.packets.serializers.AccountRegistrationRequest;
import net.hashsploit.clank.server.common.packets.serializers.AccountRegistrationResponse;

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
