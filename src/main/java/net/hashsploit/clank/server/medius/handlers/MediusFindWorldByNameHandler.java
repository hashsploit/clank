package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusApplicationType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.serializers.FindWorldByNameRequest;
import net.hashsploit.clank.server.medius.serializers.FindWorldByNameResponse;

public class MediusFindWorldByNameHandler extends MediusPacketHandler {

	private FindWorldByNameRequest reqPacket;
	private FindWorldByNameResponse respPacket;
	
	public MediusFindWorldByNameHandler() {
		super(MediusMessageType.FindWorldByName, MediusMessageType.FindWorldByNameResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new FindWorldByNameRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		int appId = 10411;
		String appName = "Syphon Filter";
		MediusApplicationType appType = MediusApplicationType.LOBBY_CHAT_CHANNEL;
		int worldId = 40;
		String worldName = new String(reqPacket.getName());
		MediusWorldStatus worldStatus = MediusWorldStatus.WORLD_STAGING;
		boolean endOfList = true;
		
		respPacket = new FindWorldByNameResponse(reqPacket.getMessageId(), callbackStatus, appId, appName, appType, worldId, worldName, worldStatus, endOfList);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}


}
