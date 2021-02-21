package net.hashsploit.clank.server.dme;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;

import io.netty.channel.socket.DatagramChannel;
import net.hashsploit.clank.server.rpc.ClankDmeRpcClient;
import net.hashsploit.clank.server.rpc.PlayerStatus;
import net.hashsploit.clank.server.rpc.WorldUpdateRequest.WorldStatus;

public class DmeWorldManager {
	
	private static final Logger logger = Logger.getLogger(DmeWorldManager.class.getName());

	// DmeWorldId -> DmeWorld
	private HashMap<Integer, DmeWorld> dmeWorlds = new HashMap<Integer, DmeWorld>();
	
	// DmePlayer -> DmeWorld lookup
	private HashMap<DmePlayer, DmeWorld> dmeWorldLookup = new HashMap<DmePlayer, DmeWorld>();
	
	// UDP
	private HashMap<InetSocketAddress, DmeWorld> dmeUdpWorldLookup = new HashMap<InetSocketAddress, DmeWorld>();
	
	public String toString() {
		String result = "++++++++++ DmeWorld Manager ++++++++++\n";
		for (DmeWorld dmeWorld: dmeWorlds.values()) {
			result += dmeWorld.toString();
		}
		result += "+++++++++++++++++++++++++++++++++++++++++";
		return result;
	}
	
	public synchronized void addPlayer(short dmeWorldIdShort, DmePlayer player) {
		int dmeWorldId = (int) dmeWorldIdShort;
		DmeWorld dmeWorld = dmeWorlds.get(dmeWorldId);
		
		if (dmeWorld == null) {
			// Initialize the world
			dmeWorld = new DmeWorld(dmeWorldId);
			dmeWorlds.put(dmeWorldId, dmeWorld);
			
			// Send world creation
			ClankDmeRpcClient rpcClient = ((DmeServer) player.getClient().getServer()).getRpcClient();
			rpcClient.updateWorld(dmeWorldId, WorldStatus.CREATED);
		}
		
		// Add the player
		dmeWorld.addPlayer(player);
		
		player.setWorldId(dmeWorldId);
		
		// Add the reverse lookup
		dmeWorldLookup.put(player, dmeWorld);
	}

	public void playerFullyConnected(DmePlayer dmePlayer) {
		// Get the world that this player is connected to
		DmeWorld dmeWorld = dmeWorldLookup.get(dmePlayer);
		
		// Set that player to fully connected
		dmeWorld.playerFullyConnected(dmePlayer);
		
		// Update MLS that player is fully connected
		dmePlayer.getClient().updateDmePlayer(dmePlayer.getMlsToken(), this.getWorldId(dmePlayer.getMlsToken()), PlayerStatus.STAGING);
		
		if (dmeWorld.getPlayerCount() == 1) {
			ClankDmeRpcClient rpcClient = ((DmeServer) dmePlayer.getClient().getServer()).getRpcClient();
			rpcClient.updateWorld(dmeWorld.getWorldId(), WorldStatus.STAGING);
		}
	}

	public int getPlayerCount(DmePlayer player) {
		return dmeWorldLookup.get(player).getPlayerCount();
	}
	
	public int getPlayerCount(int dmeWorldId) {
		return dmeWorlds.get(dmeWorldId).getPlayerCount();
	}

	/*
	 *  TCP Methods =================================================================
	 */
	public void broadcast(DmePlayer dmePlayer, byte[] bytes) {
		DmeWorld worldToBroadcast = dmeWorldLookup.get(dmePlayer);
		worldToBroadcast.broadcast(dmePlayer, bytes);
	}

	public void clientAppSingle(DmePlayer dmePlayer, byte[] bytes) {
		DmeWorld worldToSend = dmeWorldLookup.get(dmePlayer);
		worldToSend.clientAppSingle(dmePlayer, bytes);
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

	public synchronized void playerUdpConnected(int dmeWorldId, int playerId, DatagramChannel playerUdpChannel, InetSocketAddress playerUdpAddr) {
		// Get the DmeWorld
		DmeWorld dmeWorld = dmeWorlds.get(dmeWorldId);
		
		// Get the DmePlayer that this UDP connection belongs to
		DmePlayer player = dmeWorld.getPlayerFromPlayerId(playerId);
		
		// Set playerUdpAddr for lookup later
		dmeWorld.setPlayerUdpConnection(player, playerUdpAddr);
		
		// Set Udp connection to this player
		player.setUdpConnection(playerUdpChannel, playerUdpAddr);
		
		// Set hashmap for this individual player -> world
		dmeUdpWorldLookup.put(playerUdpAddr, dmeWorld);
	}

	public Collection<DmeWorld> getWorlds() {
		return dmeWorlds.values();
	}

	public int getWorldId(String mlsToken) {
		for (int worldId: dmeWorlds.keySet()) {
			if (dmeWorlds.get(worldId).hasPlayer(mlsToken)) {
				return worldId;
			}
		}
		return -1;
	}

	public int playerDisconnected(DmePlayer player) {
		DmeWorld world = dmeWorldLookup.get(player);
		world.playerDisconnected(player);
		dmeWorldLookup.remove(player);
		dmeUdpWorldLookup.remove(player.getUdpAddr());
				
		// Return the world where player was disconnected
		return world.getWorldId();
	}

	public boolean worldIsEmpty(int worldId) {
		DmeWorld world = dmeWorlds.get(worldId);
		if (world == null) {
			return true;
		}
		return world.isEmpty();
	}

	public void deleteWorld(int worldId) {
		dmeWorlds.remove(worldId);
	}


}
