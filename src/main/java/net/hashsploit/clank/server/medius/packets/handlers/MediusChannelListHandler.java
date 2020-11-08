package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.packets.serializers.ChannelListRequest;
import net.hashsploit.clank.server.medius.packets.serializers.ChannelListResponse;

public class MediusChannelListHandler extends MediusPacketHandler {

	private ChannelListRequest reqPacket;
	private ChannelListResponse respPacket;
	
	public MediusChannelListHandler() {
		super(MediusPacketType.ChannelList, MediusPacketType.ChannelListResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new ChannelListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.MediusSuccess;
		int mediusWorldId = 1;
		String lobbyName = "AEQLITUDE";
		int playerCount = 3;
		boolean endOfList = true;
		
		respPacket = new ChannelListResponse(reqPacket.getMessageId(), callbackStatus, mediusWorldId, lobbyName, playerCount, endOfList);
		
		return respPacket;
	}


}