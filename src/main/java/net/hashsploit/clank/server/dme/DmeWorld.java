package net.hashsploit.clank.server.dme;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.common.objects.DmePlayerStatus;
import net.hashsploit.clank.utils.Utils;

public class DmeWorld {

	
	private int worldId;
		
	// Lookup Player from Dme Id
	HashMap<Integer, DmePlayer> players = new HashMap<Integer, DmePlayer>();
	
	// Lookup Player from Socket
	HashMap<SocketChannel, DmePlayer> playerSocketLookup = new HashMap<SocketChannel, DmePlayer>();
	
	// Lookup Player from Udp Packet
	HashMap<InetSocketAddress, DmePlayer> playerUdpLookup = new HashMap<InetSocketAddress, DmePlayer>();

	public DmeWorld(int dmeWorldId) {
		worldId = dmeWorldId;
	}

	public String toString() {
		return "DmeWorld: \n" + 
				"worldId: " + Integer.toString(worldId) + "\n" +
				"numPlayers: " + Integer.toString(players.size()) + "\n"
				;
	}

	public void addPlayer(SocketChannel socket) {
		/*
		 * Initial connect. Player has not finished connecting yet
		 */
		int newPlayerId = this.getNewId();
		
		// Game is full
		if (newPlayerId == -1) {
			return;
		}
		
		DmePlayer player = new DmePlayer(newPlayerId, socket);
		
		// Otherwise, add the player
		players.put(newPlayerId, player);
		// Add reverse lookup
		playerSocketLookup.put(socket, player);
	}
	
	public void playerFullyConnected(SocketChannel socket) {
		/* 
		 * Player is now fully connected. Send a broadcast notify
		 */
		DmePlayer player = playerSocketLookup.get(socket);
		player.fullyConnected();
		
		// Broadcast this player is fully connected to everyone else
		sendServerNotify(player);
	}
	
	private int getNewId() {
		/*
		 * Get a new Id for the game world.
		 */
		int result = -1;
		
		HashSet<Integer> ids = new HashSet<Integer>();		
				
		for (DmePlayer player: players.values()) {
			ids.add(player.getPlayerId());
		}
				
		if (ids.size() == 0) {
			return 0;
		}
		
		for (int i = 0; i < 8; i++) {
			if (!ids.contains(i)) {
				return i;
			}
		}
		return result;
	}
	
	public void clientAppSingle(SocketChannel socket, byte[] payload) {
		DmePlayer sourcePlayer = playerSocketLookup.get(socket);
		int sourceId = sourcePlayer.getPlayerId();
		int playerTargetId = (int) payload[3];
		payload[3] = (byte) sourceId;
		
		if (sourceId == playerTargetId) {
			throw new IllegalStateException("ClientAppSingle: SourceId same as TargetId");
		}
		
		DmePlayer targetPlayer = players.get(playerTargetId);
		targetPlayer.sendData(payload);
	}
	
	public void broadcast(SocketChannel socket, byte[] payload) {
		DmePlayer sourcePlayer = playerSocketLookup.get(socket);
		int sourceId = sourcePlayer.getPlayerId();
		
		// Insert the source id
		payload = insertId(payload, (byte) sourceId);
		
		// Send to every player that is not the source id player
		for (DmePlayer player: players.values()) {
			if (player.getPlayerId() != sourceId) {
				player.sendData(payload);
			}
		}
	}
	
	private byte[] insertId(byte[] payload, byte id) {
		payload[0] = RTMessageId.CLIENT_APP_SINGLE.getValue();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(payload[0]);
			baos.write(payload[1]);
			baos.write(payload[2]);
			baos.write(id);
			baos.write((byte) 0);
			baos.write(Arrays.copyOfRange(payload, 3, payload.length));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] result = baos.toByteArray();
		// fix the length
		short curLength = Utils.bytesToShortLittle(result[1], result[2]);
		curLength += 2;
		byte[] newLen = Utils.shortToBytesLittle(curLength);
		result[1] = newLen[0];
		result[2] = newLen[1];
		return result;
	}
		
	private void sendServerNotify(DmePlayer player) {
		int playerId = player.getPlayerId();
		
		// build server notify packet
		String ipAddress = null;//((DmeConfig) Clank.getInstance().getConfig()).getUdpAddress();
		
		if (ipAddress == null /*|| ipAddress.isEmpty()*/) {
			ipAddress = Utils.getPublicIpAddress();
		}
		
		byte[] ipAddr = ipAddress.getBytes();
		int numZeros = 16 - ipAddress.length();
		String zeroString = new String(new char[numZeros]).replace("\0", "00");
		byte[] zeroTrail = Utils.hexStringToByteArray(zeroString);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try {
			baos.write(Utils.hexStringToByteArray("085200"));
			baos.write(Utils.shortToBytesLittle((short) playerId));
			
			baos.write(ipAddr);
			baos.write(zeroTrail);
			
			baos.write(Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (DmePlayer playerToReceiveData: players.values()) {
			if (player != playerToReceiveData && (player.getStatus() == DmePlayerStatus.STAGING || player.getStatus() == DmePlayerStatus.ACTIVE)) {
				playerToReceiveData.sendData(baos.toByteArray());
			}
		}
	}

	public int getPlayerId(SocketChannel socket) {
		return playerSocketLookup.get(socket).getPlayerId();
	}

	public int getPlayerCount() {
		return players.size();
	}

	
	/*
	 *  UDP Methods =================================================================
	 */
	
	public void setPlayerUdpConnection(int playerId, DatagramChannel playerUdpChannel, InetSocketAddress playerUdpAddr) {
		DmePlayer player = players.get(playerId);
		
		if (playerUdpLookup.containsKey(playerUdpAddr)) {
			throw new IllegalStateException("setPlayerUdpConnection: New connection player " + Integer.toString(playerId) + " has the same Udp channel as " + 
					Integer.toString(playerUdpLookup.get(playerUdpAddr).getPlayerId()) + " !");
		}
		
		playerUdpLookup.put(playerUdpAddr, player);
		player.setUdpConnection(playerUdpChannel, playerUdpAddr);
	}

	public void broadcastUdp(InetSocketAddress senderUdpAddr, byte[] payload) {
		DmePlayer sourcePlayer = playerUdpLookup.get(senderUdpAddr);
		int sourceId = sourcePlayer.getPlayerId();
		
		// Insert the source id
		payload = insertId(payload, (byte) sourceId);
		
		// Send to every player that is not the source id player
		for (DmePlayer player: players.values()) {
			if (player.getPlayerId() != sourceId) {
				player.sendUdpData(payload);
			}
		}		
	}

	public void clientAppSingleUdp(InetSocketAddress senderUdpAddr, byte[] payload) {
		DmePlayer sourcePlayer = playerUdpLookup.get(senderUdpAddr);
		int sourceId = sourcePlayer.getPlayerId();
		int playerTargetId = (int) payload[3];
		payload[3] = (byte) sourceId;
		
		if (sourceId == playerTargetId) {
			throw new IllegalStateException("ClientAppSingleUdp: SourceId same as TargetId");
		}
		
		DmePlayer targetPlayer = players.get(playerTargetId);
		targetPlayer.sendUdpData(payload);		
	}

	public Collection<DmePlayer> getPlayers() {
		return players.values();
	}

}
