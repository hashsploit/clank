package net.hashsploit.clank.server.dme;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import io.netty.buffer.Unpooled;
import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.common.objects.DmePlayerStatus;
import net.hashsploit.clank.utils.Utils;

public class DmeWorld {
		
	// Lookup Player from Dme Id
	HashMap<Integer, DmePlayer> players = new HashMap<Integer, DmePlayer>();
	
	// Lookup Player from Socket
	HashMap<SocketChannel, DmePlayer> playerSocketLookup = new HashMap<SocketChannel, DmePlayer>();


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
		String p1 = "085200";
		String p2 = Utils.bytesToHex(Utils.shortToBytesLittle((short) playerId));
		String p3 = "3139322e3136382e302e39390000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		String res = p1 + p2 + p3;
		
		for (DmePlayer playerToReceiveData: players.values()) {
			if (player != playerToReceiveData && player.getStatus() == DmePlayerStatus.CONNECTED) {
				playerToReceiveData.sendData(Utils.hexStringToByteArray(res));
			}
		}
	}

	public int getPlayerId(SocketChannel socket) {
		return playerSocketLookup.get(socket).getPlayerId();
	}

	public int getPlayerCount() {
		return players.size();
	}



}
