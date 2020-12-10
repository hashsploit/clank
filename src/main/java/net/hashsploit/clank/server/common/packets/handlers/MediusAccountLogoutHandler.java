package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.AccountLogoutRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountLogoutHandler extends MediusPacketHandler {
	private AccountLogoutRequest reqPacket;

	public MediusAccountLogoutHandler() {
		super(MediusMessageType.AccountLogout, MediusMessageType.AccountLogoutResponse);
	}
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new AccountLogoutRequest(mm.getPayload());
		logger.finest(reqPacket.toString());	
	}

	@Override
	public void write(MediusClient client) {
	}

}
