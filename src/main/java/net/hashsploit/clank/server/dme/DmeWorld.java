package net.hashsploit.clank.server.dme;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.objects.DmePlayerStatus;
import net.hashsploit.clank.utils.Utils;

public class DmeWorld {
	
	private static final Logger logger = Logger.getLogger(DmeWorld.class.getName());
	
	private int worldId;
		
	// Lookup Player from Dme Id
	HashMap<Integer, DmePlayer> players = new HashMap<Integer, DmePlayer>();
	
	// Lookup Player from Udp Packet
	HashMap<InetSocketAddress, DmePlayer> playerUdpLookup = new HashMap<InetSocketAddress, DmePlayer>();

	public DmeWorld(int dmeWorldId) {
		worldId = dmeWorldId;
	}

	public String toString() {
		String result = " ====== DmeWorld: \n" + 
				"--- worldId: " + Integer.toString(worldId) + "\n" +
				"--- numPlayers: " + Integer.toString(players.size()) + "\n";
		for (DmePlayer player: players.values()) {
			result += player.toString();
		}
		return result;
	}

	public void addPlayer(DmePlayer player) {
		/*
		 * Initial connect. Player has not finished connecting yet
		 */
		int newPlayerId = this.getNewId();
		
		// Game is full
		if (newPlayerId == -1) {
			return;
		}
		
		// Set the players ID
		player.setPlayerId(newPlayerId);
				
		// Add this player ID + player to this world
		players.put(newPlayerId, player);
	}
	
	public void playerFullyConnected(DmePlayer player) {
		/* 
		 * Player is now fully connected. Send a broadcast notify
		 */
		player.fullyConnected();
		
		// Broadcast this player is fully connected to everyone else
		sendServerNotify(player, true);
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
	
	public void clientAppSingle(DmePlayer sourcePlayer, byte[] payload) {
		int sourceId = sourcePlayer.getPlayerId();
		int playerTargetId = (int) payload[3];
		payload[3] = (byte) sourceId;
		
		if (sourceId == playerTargetId) {
			throw new IllegalStateException("ClientAppSingle: SourceId same as TargetId");
		}
		
		DmePlayer targetPlayer = players.get(playerTargetId);
		if (targetPlayer == null) 
			return;
		targetPlayer.sendData(payload);
	}
	
	public void broadcast(DmePlayer sourcePlayer, byte[] payload) {
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
		payload[0] = RtMessageId.CLIENT_APP_SINGLE.getValue();

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
		
	private void sendServerNotify(DmePlayer player, boolean connecting) {
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
			if (connecting) {
				baos.write(Utils.hexStringToByteArray("085200"));
			}
			else {
				baos.write(Utils.hexStringToByteArray("091200"));
			}
			baos.write(Utils.shortToBytesLittle((short) playerId));
			
			baos.write(ipAddr);
			baos.write(zeroTrail);
			if (connecting) {
				baos.write(Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (DmePlayer playerToReceiveData: players.values()) {
			if (player != playerToReceiveData && (player.getStatus() == DmePlayerStatus.STAGING || player.getStatus() == DmePlayerStatus.ACTIVE)) {
				playerToReceiveData.sendData(baos.toByteArray());
			}
		}
	}

	public int getPlayerCount() {
		return players.size();
	}

	
	/*
	 *  UDP Methods =================================================================
	 */
	public void setPlayerUdpConnection(DmePlayer player, InetSocketAddress playerUdpAddr) {
		if (playerUdpLookup.containsKey(playerUdpAddr)) {
			throw new IllegalStateException("setPlayerUdpConnection: New connection player " + Integer.toString(player.getPlayerId()) + " has the same Udp channel as " + 
					Integer.toString(playerUdpLookup.get(playerUdpAddr).getPlayerId()) + " !");
		}
		playerUdpLookup.put(playerUdpAddr, player);
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
		if (targetPlayer == null) 
			return;
		targetPlayer.sendUdpData(payload);		
	}

	public Collection<DmePlayer> getPlayers() {
		return players.values();
	}

	public boolean hasPlayer(String mlsToken) {
		for (DmePlayer player: players.values()) {
			if (mlsToken == player.getMlsToken()) {
				return true;
			}
		}
		return false;
	}

	public void playerDisconnected(DmePlayer player) {
		players.remove(player.getPlayerId());
		playerUdpLookup.remove(player.getUdpAddr());
		sendServerNotify(player, false);
	}

	public boolean isEmpty() {
		return players.size() == 0;
	}

	public DmePlayer getPlayerFromPlayerId(int playerId) {
		return players.get(playerId);
	}

	public int getWorldId() {
		return worldId;
	}

}
