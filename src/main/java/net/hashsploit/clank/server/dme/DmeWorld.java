package net.hashsploit.clank.server.dme;

import java.util.List;

public class DmeWorld {
	
	private int worldId;
	public List<DmePlayer> players;
	
	
	public DmeWorld(int worldId, DmePlayer sessionMaster) {
		this.worldId = worldId;
		this.players.add(sessionMaster);
	}
	
	public synchronized void addPlayer(DmePlayer player) {
		players.add(player);
	}
	
	public synchronized void removePlayer(DmePlayer player) {
		players.remove(player);
	}
	
	public void broadcast(DmePlayer sourcePlayer, DmePacket packet) {
		
	}
	
	
}
