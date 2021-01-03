package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

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
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new ChannelListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		int mediusWorldId = 1;
		String lobbyName = "AEQLITUDE";
		int playerCount = 3;
		boolean endOfList = true;
		
		respPacket = new ChannelListResponse(reqPacket.getMessageId(), callbackStatus, mediusWorldId, lobbyName, playerCount, endOfList);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}


}