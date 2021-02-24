package net.hashsploit.clank.server;

import java.util.HashSet;
import java.util.logging.Logger;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.serializers.CreateGameOneRequest;

public class MediusGame {

	private static final Logger logger = Logger.getLogger(MediusGame.class.getName());

	private int worldId;
	private HashSet<Player> players;
	private CreateGameOneRequest req;
	
	private byte[] stats = new byte[MediusConstants.GAMESTATS_MAXLEN.value];
	private MediusWorldStatus worldStatus;

	public MediusGame(int worldId, CreateGameOneRequest req) {
		this.worldId = worldId;
		this.req = req;
		this.worldStatus = MediusWorldStatus.WORLD_PENDING_CREATION;
		this.players = new HashSet<Player>();
	}

	public CreateGameOneRequest getReqPacket() {
		return req;
	}

	public byte[] getStats() {
		//return Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
		return stats;
	}
	
	public synchronized void updateStats(byte[] stats) {
		this.stats = stats;
	}

	public MediusWorldStatus getWorldStatus() {
		return worldStatus;
	}
	
	public synchronized void addPlayer(Player player) {
		this.players.add(player);
	}
	
	public synchronized void removePlayer(Player player) {
		this.players.remove(player);
	}

	public int getPlayerCount() {
		return players.size();
	}
	
	public int getWorldId() {
		return worldId;
	}

	public synchronized void updateStatus(MediusWorldStatus worldStatus) {
		logger.info("Updating world status: [dmeWorldId: " + worldId + ", newWorldStatus: " + worldStatus.toString() + ", playercount: " + getPlayerCount() + "]");
		this.worldStatus = worldStatus;
	}

}
