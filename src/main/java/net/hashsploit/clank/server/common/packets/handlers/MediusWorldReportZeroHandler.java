package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusLobbyServer;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.server.common.packets.serializers.WorldReportZeroRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusWorldReportZeroHandler extends MediusPacketHandler {
	
	private WorldReportZeroRequest reqPacket;	
	
	public MediusWorldReportZeroHandler() {
		super(MediusMessageType.WorldReport0, null);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new WorldReportZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public void write(MediusClient client) {
		// Update the logichandler with the world
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		
		int worldId = Utils.bytesToIntLittle(reqPacket.getWorldID());
		int worldStatus = Utils.bytesToIntLittle(reqPacket.getWorldStatus());
		
		// FIXME: what does this even mean?
		if (worldStatus == 2) {
			server.updateDmeWorldStatus(worldId, MediusWorldStatus.WORLD_ACTIVE);
		}
		
		server.getGame(worldId).updateStats(reqPacket.getGameStats());
		server.getGame(worldId).setGenericField1(reqPacket.getGenField1());
		server.getGame(worldId).setGenericField2(reqPacket.getGenField2());
		server.getGame(worldId).setGenericField3(reqPacket.getGenField3());
		
	}

}
