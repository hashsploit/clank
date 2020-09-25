package net.hashsploit.clank.server.medius.packets;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;

public class MediusUpdateUserState extends MediusPacket {

	public MediusUpdateUserState() {
		super(MediusPacketType.UpdateUserState, null);
	}

	@Override
	public MediusMessage write(MediusClient client) {
		// Process the packet
		return null;
	}

}
