package net.hashsploit.clank.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;

public class PlayerList {
	
	// AccountId -> Player
	HashMap<Integer, Player> players;
	
	public PlayerList() {
		this.players = new HashMap<Integer, Player>();
	}
	
	public String toString() {
		String result = "PlayerList ----- \n";
		for (Player player: players.values()) {
			result += player.toString() + "\n";
		}
		return result;
	}

	public void updatePlayerStatus(Player player, MediusPlayerStatus status) {
		// 1. Check if player is disconnecting, remove player from list
		if (status == MediusPlayerStatus.MEDIUS_PLAYER_DISCONNECTED) {
			players.remove(player.getAccountId());
			return;
		}
		
		// 2. Make sure player has logged in
		if (player.getAccountId() == null) {
			throw new IllegalStateException("Player did not login yet!");
		}
		
		// 3. Update player status
		int accountId = player.getAccountId();

		// If player isn't in the players, add it
		if (!players.containsKey(accountId)) {
			player.updateStatus(status);
			players.put(accountId, player);
		}
		else {
			players.get(accountId).updateStatus(status);
		}
	}
	
	public void updatePlayerStatus(int accountId, MediusPlayerStatus status) {
		Player p = players.get(accountId);
		updatePlayerStatus(p, status);
	}

	public ArrayList<Player> getPlayersByLobbyWorld(int worldId) {
		ArrayList<Player> result = new ArrayList<Player>();
		for (Player player: players.values()) {
			if (player.getChatWorldId() == worldId) {
				result.add(player);
			}
		}
		return result;
	}

	public ArrayList<Player> getPlayersByGameWorld(int worldId) {
		ArrayList<Player> result = new ArrayList<Player>();
		for (Player player: players.values()) {
			if (player.getGameWorldId() == worldId) {
				result.add(player);
			}
		}
		return result;	}

	public MediusPlayerStatus getPlayerStatus(int accountId) {
		Player p = players.get(accountId);
		if (p == null) {
			return MediusPlayerStatus.MEDIUS_PLAYER_DISCONNECTED;
		}
		return p.getStatus();
	}


}
