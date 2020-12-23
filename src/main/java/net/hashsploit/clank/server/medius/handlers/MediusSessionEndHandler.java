package net.hashsploit.clank.server.medius.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;

public class MediusSessionEndHandler extends MediusPacketHandler {

	
	public MediusSessionEndHandler() {
		super(MediusMessageType.SessionEnd, MediusMessageType.SessionEndResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
	}
	
	@Override
	public void write(MediusClient client) {
	}


}
