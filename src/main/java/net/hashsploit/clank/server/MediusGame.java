package net.hashsploit.clank.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.serializers.CreateGameOneRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusGame {

	private static final Logger logger = Logger.getLogger(MediusGame.class.getName());

	private int worldId;
	private CreateGameOneRequest req;
	private int playerCount = 1; // TODO: Update this when DME sends a "PlayerConnected" gRPC
	
//	WORLD_INACTIVE(0)
//	WORLD_STAGING(1)
//	WORLD_ACTIVE(2)
//	WORLD_CLOSED(3)
//	WORLD_PENDING_CREATION(4)
//	WORLD_PENDING_CONNECT_TO_GAME(5)
	private MediusWorldStatus worldStatus;
	
	private ArrayList<String> players = new ArrayList<String>();

	public MediusGame(int worldId, CreateGameOneRequest req) {
		this.worldId = worldId;
		this.req = req;
		this.worldStatus = MediusWorldStatus.WORLD_PENDING_CREATION;
	}
	
	public CreateGameOneRequest getReqPacket() {
		return req;
	}

	public byte[] getPlayerCount() {
		return Utils.intToBytesLittle(playerCount);
	}

	public byte[] getStats() {
		return Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
	}

	public byte[] getWorldStatusBytes() {
		return Utils.intToBytesLittle(worldStatus.getValue());
	}
	
	public MediusWorldStatus getWorldStatus() {
		return worldStatus;
	}

	public byte[] getWorldId() {
		return Utils.intToBytesLittle(worldId);
	}

	public void updateStatus(MediusWorldStatus worldStatus) {
		logger.info("Updating world status: [dmeWorldId: " + worldId + ", newWorldStatus: " + worldStatus.toString() + ", playercount: " + players.size() + "]");
		this.worldStatus = worldStatus;
	}
	
	
}
