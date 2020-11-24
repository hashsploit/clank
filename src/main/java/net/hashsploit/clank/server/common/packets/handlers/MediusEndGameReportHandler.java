package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;

public class MediusEndGameReportHandler extends MediusPacketHandler {

	public MediusEndGameReportHandler() {
		super(MediusPacketType.EndGameReport, null);
	}

	@Override
	public MediusPacket write(MediusClient client) {
		// Process the packet
		return null;
	}

}
