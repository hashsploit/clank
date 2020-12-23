package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.packets.serializers.AccountRegistrationRequest;
import net.hashsploit.clank.server.medius.packets.serializers.AccountRegistrationResponse;

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
	public void write(MediusClient client) {
		respPacket = new AccountRegistrationResponse(messageId);
		client.sendMediusMessage(respPacket);
	}


}
