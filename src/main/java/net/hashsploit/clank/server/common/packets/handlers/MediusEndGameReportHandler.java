package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;

public class MediusEndGameReportHandler extends MediusPacketHandler {

	public MediusEndGameReportHandler() {
		super(MediusMessageType.EndGameReport, null);
	}

	@Override
	public MediusMessage write(MediusClient client) {
		// Process the packet
		return null;
	}

}
