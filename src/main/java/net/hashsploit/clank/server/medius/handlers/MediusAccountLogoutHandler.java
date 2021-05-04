package net.hashsploit.clank.server.medius.handlers;

import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.AccountLogoutRequest;

public class MediusAccountLogoutHandler extends MediusPacketHandler {
	private AccountLogoutRequest reqPacket;

	public MediusAccountLogoutHandler() {
		super(MediusMessageType.AccountLogout, MediusMessageType.AccountLogoutResponse);
	}
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new AccountLogoutRequest(mm.getPayload());
		logger.finest(reqPacket.toString());	
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		//client.sendMessage(new RTMessage(RtMessageId.CLIENT_DISCONNECT, new byte[] {0x00}));
		
		return null;
	}

}
