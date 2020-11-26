package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusApplicationType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.server.common.packets.serializers.FindWorldByNameRequest;
import net.hashsploit.clank.server.common.packets.serializers.FindWorldByNameResponse;

public class MediusFindWorldByNameHandler extends MediusPacketHandler {

	private FindWorldByNameRequest reqPacket;
	private FindWorldByNameResponse respPacket;
	
	public MediusFindWorldByNameHandler() {
		super(MediusMessageType.FindWorldByName, MediusMessageType.FindWorldByNameResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new FindWorldByNameRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
		int appId = 10411;
		String appName = "Syphon Filter";
		MediusApplicationType appType = MediusApplicationType.LOBBY_CHAT_CHANNEL;
		int worldId = 40;
		String worldName = new String(reqPacket.getName());
		MediusWorldStatus worldStatus = MediusWorldStatus.WORLD_STAGING;
		boolean endOfList = true;
		
		respPacket = new FindWorldByNameResponse(reqPacket.getMessageId(), callbackStatus, appId, appName, appType, worldId, worldName, worldStatus, endOfList);
		
		return respPacket;
	}


}
