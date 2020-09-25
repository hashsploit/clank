package net.hashsploit.clank.server.medius.packets;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountLogout extends MediusPacket {

	public MediusAccountLogout() {
		super(MediusPacketType.AccountLogout, MediusPacketType.AccountLogoutResponse);
	}
	@Override
	public void read(MediusMessage mm) {
		logger.fine("Logout data: " + Utils.bytesToHex(mm.getPayload()));
	}

	@Override
	public MediusMessage write(MediusClient client) {
		return null;
	}

}
