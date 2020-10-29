package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusApplicationType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.packets.serializers.FindWorldByNameRequest;
import net.hashsploit.clank.server.medius.packets.serializers.FindWorldByNameResponse;

public class MediusFindWorldByNameHandler extends MediusPacketHandler {

	private FindWorldByNameRequest reqPacket;
	private FindWorldByNameResponse respPacket;
	
	public MediusFindWorldByNameHandler() {
		super(MediusPacketType.FindWorldByName, MediusPacketType.FindWorldByNameResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new FindWorldByNameRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.MediusSuccess;
		int appId = 0;
		String appName = "App Name";
		MediusApplicationType appType = MediusApplicationType.LOBBY_CHAT_CHANNEL;
		int worldId = 2;
		String worldName = "hash's world";
		MediusWorldStatus worldStatus = MediusWorldStatus.WORLD_STAGING;
		boolean endOfList = true;
		
		respPacket = new FindWorldByNameResponse(reqPacket.getMessageId(), callbackStatus, appId, appName, appType, worldId, worldName, worldStatus, endOfList);
		
		return respPacket;
	}


}