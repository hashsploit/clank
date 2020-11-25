package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;

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
