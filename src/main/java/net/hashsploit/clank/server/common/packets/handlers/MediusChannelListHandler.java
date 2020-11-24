package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.packets.serializers.ChannelListRequest;
import net.hashsploit.clank.server.common.packets.serializers.ChannelListResponse;

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