package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.packets.serializers.AccountLogoutRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountLogoutHandler extends MediusPacket {
	private AccountLogoutRequest reqPacket;

	public MediusAccountLogoutHandler() {
		super(MediusPacketType.AccountLogout, MediusPacketType.AccountLogoutResponse);
	}
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new AccountLogoutRequest(mm.getPayload());
		logger.finest(reqPacket.toString());	
	}

	@Override
	public MediusMessage write(MediusClient client) {
		return null;
	}

}
