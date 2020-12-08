package net.hashsploit.clank.server.dme;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;

public class DmeWorldManager {
	
	private static final Logger logger = Logger.getLogger(DmeWorldManager.class.getName());

	private HashMap<Integer, DmeWorld> dmeWorlds = new HashMap<Integer, DmeWorld>();
	
	private HashMap<SocketChannel, DmeWorld> dmeWorldLookup = new HashMap<SocketChannel, DmeWorld>();
	
	// UDP
	private HashMap<InetSocketAddress, DmeWorld> dmeUdpWorldLookup = new HashMap<InetSocketAddress, DmeWorld>();
	
	public String toString() {
		String result = "++++++++++ DmeWorld Manager ++++++++++\n";
		for (DmeWorld dmeWorld: dmeWorlds.values()) {
			result += dmeWorld.toString();
		}
		result += "=== End DmeWorldManagerToString ===";
		return result;
	}
	
	public void addPlayer(short dmeWorldIdShort, SocketChannel socket) {
		int dmeWorldId = (int) dmeWorldIdShort;
		DmeWorld dmeWorld = dmeWorlds.get(dmeWorldId);
		
		if (dmeWorld == null) {
			// Initialize the world
			dmeWorld = new DmeWorld(dmeWorldId);
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
	
	public int getPlayerCount(int dmeWorldId) {
		return dmeWorlds.get(dmeWorldId).getPlayerCount();
	}

	/*
	 *  TCP Methods =================================================================
	 */
	public void broadcast(SocketChannel socket, byte[] bytes) {
		DmeWorld worldToBroadcast = dmeWorldLookup.get(socket);
		worldToBroadcast.broadcast(socket, bytes);
	}

	public void clientAppSingle(SocketChannel socket, byte[] bytes) {
		DmeWorld worldToSend = dmeWorldLookup.get(socket);
		worldToSend.clientAppSingle(socket, bytes);
	}
	
	/*
	 *  UDP Methods =================================================================
	 */
	public void broadcastUdp(InetSocketAddress senderUdpAddr, byte[] bytes) {
		DmeWorld worldToBroadcast = dmeUdpWorldLookup.get(senderUdpAddr);
		worldToBroadcast.broadcastUdp(senderUdpAddr, bytes);
	}
	
	public void clientAppSingleUdp(InetSocketAddress senderUdpAddr, byte[] bytes) {
		DmeWorld worldToSend = dmeUdpWorldLookup.get(senderUdpAddr);
		worldToSend.clientAppSingleUdp(senderUdpAddr, bytes);		
	}

	public void playerUdpConnected(int dmeWorldId, int playerId, DatagramChannel playerUdpChannel, InetSocketAddress playerUdpAddr) {
		logger.info(this.toString());
		DmeWorld dmeWorld = dmeWorlds.get(dmeWorldId);
		dmeUdpWorldLookup.put(playerUdpAddr, dmeWorld);
		logger.info(dmeWorld.toString());
		dmeWorld.setPlayerUdpConnection(playerId, playerUdpChannel, playerUdpAddr);
	}

	public Collection<DmeWorld> getWorlds() {
		return dmeWorlds.values();
	}

}
