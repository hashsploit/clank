package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;

public class MediusUpdateUserStateHandler extends MediusPacketHandler {

	public MediusUpdateUserStateHandler() {
		super(MediusMessageType.UpdateUserState, null);
	}

	@Override
	public void write(MediusClient client) {
	}

}
