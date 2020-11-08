package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.packets.serializers.VersionServerRequest;
import net.hashsploit.clank.server.medius.packets.serializers.VersionServerResponse;

public class MediusVersionServerHandler extends MediusPacketHandler {

	private VersionServerRequest reqPacket;
	private VersionServerResponse respPacket;
	
	public MediusVersionServerHandler() {
		super(MediusPacketType.ChannelList, MediusPacketType.ChannelListResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new VersionServerRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		String serverVersion = Clank.NAME + " v" + Clank.VERSION;
		
		respPacket = new VersionServerResponse(reqPacket.getMessageId(), serverVersion);
		
		return respPacket;
	}
}
