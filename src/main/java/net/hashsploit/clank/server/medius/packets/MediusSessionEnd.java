package net.hashsploit.clank.server.medius.packets;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;

public class MediusSessionEnd extends MediusPacket {

	
	public MediusSessionEnd() {
		super(MediusPacketType.SessionEnd, MediusPacketType.SessionEndResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
	}
	
	@Override
	public MediusMessage write(MediusClient client) {
		return null;
	}


}
