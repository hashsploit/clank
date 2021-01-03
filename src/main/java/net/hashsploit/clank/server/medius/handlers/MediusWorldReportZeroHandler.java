package net.hashsploit.clank.server.medius.handlers;

import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.serializers.WorldReportZeroRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusWorldReportZeroHandler extends MediusPacketHandler {

	private WorldReportZeroRequest reqPacket;

	public MediusWorldReportZeroHandler() {
		super(MediusMessageType.WorldReport0, null);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new WorldReportZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		// Update the logichandler with the world
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();

		int worldId = Utils.bytesToIntLittle(reqPacket.getWorldID());
		int worldStatus = Utils.bytesToIntLittle(reqPacket.getWorldStatus());
		if (worldStatus == 2) {
			server.updateDmeWorldStatus(worldId, MediusWorldStatus.WORLD_ACTIVE);
		}

		return null;
	}

}
