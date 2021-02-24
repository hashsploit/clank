package net.hashsploit.clank.server.medius.handlers;

import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;

public class MediusEndGameReportHandler extends MediusPacketHandler {

	public MediusEndGameReportHandler() {
		super(MediusMessageType.EndGameReport, null);
	}

	@Override
	public void read(MediusClient client, MediusMessage message) {

	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		return null;
	}

}
