package net.hashsploit.clank.server.medius.handlers;

import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;

public class MediusUpdateUserStateHandler extends MediusPacketHandler {

	public MediusUpdateUserStateHandler() {
		super(MediusMessageType.UpdateUserState, null);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {

	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		return null;
	}

}
