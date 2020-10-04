package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;

public class MediusUpdateUserStateHandler extends MediusPacket {

	public MediusUpdateUserStateHandler() {
		super(MediusPacketType.UpdateUserState, null);
	}

	@Override
	public MediusMessage write(MediusClient client) {
		// Process the packet
		return null;
	}

}
