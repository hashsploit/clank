package net.hashsploit.clank.server;

import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusGame {

	private int worldId;
	private CreateGameOneRequest req;
	private short playerCount = 1; // TODO: Update this when DME sends a "PlayerConnected" gRPC
	private MediusWorldStatus worldStatus;

	public MediusGame(int worldId, CreateGameOneRequest req) {
		this.worldId = worldId;
		this.req = req;
		this.worldStatus = MediusWorldStatus.WORLD_PENDING_CREATION;
	}

	public CreateGameOneRequest getReqPacket() {
		return req;
	}

	public short getPlayerCount() {
		return playerCount;
	}

	public byte[] getStats() {
		return Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
	}

	public MediusWorldStatus getWorldStatus() {
		return worldStatus;
	}

	public int getWorldId() {
		return worldId;
	}

	public synchronized void updateStatus(MediusWorldStatus worldStatus) {
		this.worldStatus = worldStatus;
	}

}
