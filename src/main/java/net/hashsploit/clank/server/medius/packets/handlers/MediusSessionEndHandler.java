package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;

public class MediusSessionEndHandler extends MediusPacketHandler {

	
	public MediusSessionEndHandler() {
		super(MediusPacketType.SessionEnd, MediusPacketType.SessionEndResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		return null;
	}


}
