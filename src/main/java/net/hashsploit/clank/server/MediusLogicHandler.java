package net.hashsploit.clank.server;

import java.util.ArrayList;
import java.util.HashMap;

import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;

public class MediusLogicHandler {
	
	private GameList gameList;
	private PlayerList playerList;
	
	private final String location = "Aquatos";
	private final int locationId = 40;
	
	public MediusLogicHandler() {
		this.gameList = new GameList();
		this.playerList = new PlayerList();
	}
	
	public String playersToString() {
		return playerList.toString();
	}
	
	public ArrayList<MediusGame> getGames() {		
		return gameList.getGames();
	}
	
	public synchronized MediusGame getGame(int worldID) {
		return gameList.getGame(worldID);
	}
	
	public synchronized int getNewGameId(CreateGameOneRequest req) {
		return gameList.getNewGameId(req);
	}
	
	public synchronized int getLocationId() {
		return locationId;
	}

	public synchronized String getLocation() {
		return location;
	}
		
	public synchronized int getCityWorldId() {
		return 123;
	}
	
	public synchronized short getCityPlayerCount(int cityWorldId) {
		return 1;
	}
	
	public synchronized String getChannelLobbyName(int cityWorldId) {
		if (cityWorldId == 123) {
			return "CY00000000-00";
		}
		
		return "ERROR!";
	}
	
	public synchronized int getChannelActivePlayerCount(int cityWorldId) {
		return 1;
	}

	/* 
	 * ==================================================================
	 *                  Player update methods
	 * ==================================================================
	 */

	public void updatePlayerStatus(Player player, MediusPlayerStatus status) {
		playerList.updatePlayerStatus(player, status);	
	}

	
	/* 
	 * ==================================================================
	 *                  Get Players in game/lobby
	 * ==================================================================
	 */
	public ArrayList<Player> getGameWorldPlayers(int worldIdRequested) {
		ArrayList<Player> result = new ArrayList<Player>();

		return result;
	}

	public ArrayList<Player> getLobbyWorldPlayers(int worldId) {
		ArrayList<Player> result = playerList.getPlayersByWorld(worldId);
		return result;
	}


}
