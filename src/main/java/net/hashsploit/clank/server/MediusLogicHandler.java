package net.hashsploit.clank.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.rpc.PlayerStatus;
import net.hashsploit.clank.server.rpc.WorldUpdateRequest.WorldStatus;

public class MediusLogicHandler {
	private static final Logger logger = Logger.getLogger(MediusLogicHandler.class.getName());

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
		// Get games in staging
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
	 * gRPC Update methods from DME
     * ==================================================================

	 */
	
	/*
	 * Update Dme World status in the MLS GameList
	 */
	public void updateDmeWorldStatus(int worldId, MediusWorldStatus worldStatus) {
		logger.info("Updating world from DME worldId: " + Integer.toString(worldId));
		logger.info("Updating world from DME World Status: " + worldStatus.toString());
		gameList.updateGameWorldStatus(worldId, worldStatus);
	}
	public void updatePlayerStatusFromDme(String mlsToken, int worldId, PlayerStatus playerStatus) {
		// PlayerStatus is from gRPC
		// This method is called from gRPC DME -> MLS
		/* 
		 * PlayerStatus:
		 *    DISCONNECTED(0),
		 *    CONNECTED(1),
		 *    STAGING(2),
		 *    ACTIVE(3),
		 *    UNRECOGNIZED(-1),
		 */
		// Update the gameWorld. Update the playerList
		MediusPlayerStatus status;
		logger.info("Updating player from DME mlsToken: " + mlsToken);
		logger.info("Updating player from DME Status: " + playerStatus.toString());
		switch (playerStatus) {
		case DISCONNECTED:
			status = MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD;
			break;
		case CONNECTED:
			status = MediusPlayerStatus.MEDIUS_PLAYER_IN_GAME_WORLD;
			break;
		case STAGING:
			status = MediusPlayerStatus.MEDIUS_PLAYER_IN_GAME_WORLD;
			break;
		case ACTIVE:
			status = MediusPlayerStatus.MEDIUS_PLAYER_IN_GAME_WORLD;
			break;
		default:
			status = null;
		}
		int accountId = Clank.getInstance().getDatabase().getAccountIdFromMlsToken(mlsToken);
		playerList.updatePlayerStatus(accountId, status);
	}


	
	/* 
	 * ==================================================================
	 *                  Get Players in game/lobby
	 * ==================================================================
	 */
	public ArrayList<Player> getGameWorldPlayers(int worldIdRequested) {
		return playerList.getPlayersByGameWorld(worldIdRequested);
	}

	public ArrayList<Player> getLobbyWorldPlayers(int worldId) {
		ArrayList<Player> result = playerList.getPlayersByLobbyWorld(worldId);
		return result;
	}


}
