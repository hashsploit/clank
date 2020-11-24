package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;

public class MediusUpdateUserStateHandler extends MediusPacketHandler {

	public MediusUpdateUserStateHandler() {
		super(MediusPacketType.UpdateUserState, null);
	}

	@Override
	public MediusPacket write(MediusClient client) {
		// Process the packet
		return null;
	}

}
