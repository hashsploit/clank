package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.AccountRegistrationRequest;
import net.hashsploit.clank.server.common.packets.serializers.AccountRegistrationResponse;

public class MediusAccountRegistrationHandler extends MediusPacketHandler {

	private AccountRegistrationRequest reqPacket;
	private AccountRegistrationResponse respPacket;
	
	private byte[] messageId;
	
	public MediusAccountRegistrationHandler() {
		super(MediusMessageType.AccountLogin, MediusMessageType.AccountLoginResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new AccountRegistrationRequest(mm.getPayload());
		
		
		messageId = reqPacket.getMessageID();
	}
	
	@Override
	public MediusMessage write(MediusClient client) {
		
		respPacket = new AccountRegistrationResponse(messageId);
		
		return respPacket;
	}


}
