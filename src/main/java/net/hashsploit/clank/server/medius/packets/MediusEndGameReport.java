package net.hashsploit.clank.server.medius.packets;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;

public class MediusEndGameReport extends MediusPacket {

	public MediusEndGameReport() {
		super(MediusPacketType.EndGameReport, null);
	}

	@Override
	public MediusMessage write(MediusClient client) {
		// Process the packet
		return null;
	}

}
