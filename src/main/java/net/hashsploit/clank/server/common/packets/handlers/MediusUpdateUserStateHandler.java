package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;

public class MediusUpdateUserStateHandler extends MediusPacketHandler {

	public MediusUpdateUserStateHandler() {
		super(MediusMessageType.UpdateUserState, null);
	}

	@Override
	public void write(MediusClient client) {
	}

}
