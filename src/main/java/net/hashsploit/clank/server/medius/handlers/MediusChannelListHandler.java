package net.hashsploit.clank.server.medius.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.ChannelListRequest;
import net.hashsploit.clank.server.medius.serializers.ChannelListResponse;

public class MediusChannelListHandler extends MediusPacketHandler {

	private ChannelListRequest reqPacket;
	private ChannelListResponse respPacket;
	
	public MediusChannelListHandler() {
		super(MediusMessageType.ChannelList, MediusMessageType.ChannelListResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new ChannelListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		int mediusWorldId = 1;
		String lobbyName = "AEQLITUDE";
		int playerCount = 3;
		boolean endOfList = true;
		
		respPacket = new ChannelListResponse(reqPacket.getMessageId(), callbackStatus, mediusWorldId, lobbyName, playerCount, endOfList);
		
		client.sendMediusMessage(respPacket);
	}


}