package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.packets.serializers.AccountLogoutRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountLogoutHandler extends MediusPacketHandler {
	private AccountLogoutRequest reqPacket;

	public MediusAccountLogoutHandler() {
		super(MediusMessageType.AccountLogout, MediusMessageType.AccountLogoutResponse);
	}
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new AccountLogoutRequest(mm.getPayload());
		logger.finest(reqPacket.toString());	
	}

	@Override
	public MediusPacket write(MediusClient client) {
		return null;
	}

}