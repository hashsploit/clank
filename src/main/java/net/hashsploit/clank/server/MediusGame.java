package net.hashsploit.clank.server;

import java.util.ArrayList;

import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.medius.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGame {
	
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
		this.worldStatus = worldStatus;
	}
	
	
}
