package net.hashsploit.clank.server;

import java.util.ArrayList;

import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGame {
	
	private int worldId;
	private CreateGameOneRequest req;
	private int playerCount = 0;
	
	// 0 -> PENDING_CREATION
	// 1 -> STAGING
	// 2 -> ACTIVE
	// 3 -> DISCONNECTED
	private int worldStatus = 0;
	
	private ArrayList<String> players = new ArrayList<String>();

	public MediusGame(int worldId, CreateGameOneRequest req) {
		this.worldId = worldId;
		this.req = req;
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

	public byte[] getWorldStatus() {
		return Utils.intToBytesLittle(worldStatus);
	}
	
	public int getWorldStatusInt() {
		return worldStatus;
	}

	public byte[] getWorldId() {
		return Utils.intToBytesLittle(worldId);
	}
	
	
}
