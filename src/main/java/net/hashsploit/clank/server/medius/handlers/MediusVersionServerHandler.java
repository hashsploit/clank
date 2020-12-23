package net.hashsploit.clank.server.medius.handlers;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.VersionServerRequest;
import net.hashsploit.clank.server.medius.serializers.VersionServerResponse;

public class MediusVersionServerHandler extends MediusPacketHandler {

	private VersionServerRequest reqPacket;
	private VersionServerResponse respPacket;
	
	public MediusVersionServerHandler() {
		super(MediusMessageType.ChannelList, MediusMessageType.ChannelListResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new VersionServerRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {
		
		String serverVersion = Clank.NAME + " v" + Clank.VERSION;
		
		respPacket = new VersionServerResponse(reqPacket.getMessageId(), serverVersion);
		
		client.sendMediusMessage(respPacket);
	}
}
