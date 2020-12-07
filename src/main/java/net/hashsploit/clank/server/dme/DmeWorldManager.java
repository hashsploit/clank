package net.hashsploit.clank.server.dme;

import java.util.HashMap;

import io.netty.channel.socket.SocketChannel;

public class DmeWorldManager {
	
	private HashMap<Integer, DmeWorld> dmeWorlds = new HashMap<Integer, DmeWorld>();
	
	private HashMap<SocketChannel, DmeWorld> dmeWorldLookup = new HashMap<SocketChannel, DmeWorld>();
	
	public void addPlayer(short dmeWorldIdShort, SocketChannel socket) {
		int dmeWorldId = (int) dmeWorldIdShort;
		DmeWorld dmeWorld = dmeWorlds.get(dmeWorldId);
		
		if (dmeWorld == null) {
			// Initialize the world
			dmeWorld = new DmeWorld();
			dmeWorlds.put(dmeWorldId, dmeWorld);
		}
		
		// Add the player
		dmeWorld.addPlayer(socket);
		
		// Add the reverse lookup
		dmeWorldLookup.put(socket, dmeWorld);
	}

	public void playerFullyConnected(SocketChannel socket) {
		// Get the world that this player is connected to
		DmeWorld dmeWorld = dmeWorldLookup.get(socket);
		
		// Set that player to fully connected
		dmeWorld.playerFullyConnected(socket);
	}

	public int getPlayerId(SocketChannel socket) {
		return dmeWorldLookup.get(socket).getPlayerId(socket);
	}

	public int getPlayerCount(SocketChannel socket) {
		return dmeWorldLookup.get(socket).getPlayerCount();
	}

	public void broadcast(SocketChannel socket, byte[] bytes) {
		DmeWorld worldToBroadcast = dmeWorldLookup.get(socket);
		worldToBroadcast.broadcast(socket, bytes);
	}

	public void clientAppSingle(SocketChannel socket, byte[] bytes) {
		DmeWorld worldToSend = dmeWorldLookup.get(socket);
		worldToSend.clientAppSingle(socket, bytes);
	}
	
	

}
