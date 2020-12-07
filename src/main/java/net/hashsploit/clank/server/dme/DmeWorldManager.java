package net.hashsploit.clank.server.dme;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

public class DmeWorldManager {
	private static final Logger logger = Logger.getLogger(DmeWorld.class.getName());

	private HashMap<Integer, DmeWorld> dmeWorlds = new HashMap<Integer, DmeWorld>();
	
	private HashMap<SocketChannel, DmeWorld> dmeWorldLookup = new HashMap<SocketChannel, DmeWorld>();
	
	private HashMap<InetSocketAddress, DmeWorld> dmeUdpWorldLookup = new HashMap<InetSocketAddress, DmeWorld>();
	
	
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
	public void broadcastUdp(ChannelHandlerContext ctx, InetSocketAddress sender, byte[] bytes) {
		DmeWorld worldToBroadcast = dmeUdpWorldLookup.get(sender);
		worldToBroadcast.broadcastUdp(ctx, sender, bytes);
	}
	
	public void clientAppSingleUdp(ChannelHandlerContext ctx, InetSocketAddress sender, byte[] bytes) {
		DmeWorld worldToSend = dmeUdpWorldLookup.get(sender);
		worldToSend.clientAppSingleUdp(ctx, sender, bytes);		
	}

	public void playerUdpConnected(int dmeWorldId, int playerId, InetSocketAddress inetSocketAddress) {
		DmeWorld dmeWorld = dmeWorlds.get(dmeWorldId);
		dmeUdpWorldLookup.put(inetSocketAddress, dmeWorld);
		logger.info(dmeWorld.toString());
		dmeWorld.setPlayerUdpConnection(playerId, inetSocketAddress);
	}




	

}
