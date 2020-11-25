package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.packets.serializers.VersionServerRequest;
import net.hashsploit.clank.server.common.packets.serializers.VersionServerResponse;

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
