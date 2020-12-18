package net.hashsploit.clank.server;

import java.util.ArrayList;
import java.util.logging.Logger;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.config.configs.MlsConfig;
import net.hashsploit.clank.config.objects.ChannelConfig;
import net.hashsploit.clank.config.objects.LocationConfig;
import net.hashsploit.clank.server.common.MediusServer;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;

public class MediusLogicHandler {
	private static final Logger logger = Logger.getLogger(MediusLogicHandler.class.getName());

	private GameList gameList;
	private PlayerList playerList;
	
	public MediusLogicHandler() {
		// FIXME: pass in Clank as a constructor parameters and use it locally.
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
	
	public MediusGame getGame(int worldID) {
		return gameList.getGame(worldID);
	}
	
	public int getNewGameId(CreateGameOneRequest req) {
		return gameList.getNewGameId(req);
	}

	public LocationConfig getLocation() {
		// FIXME: bad
		if (Clank.getInstance().getServer() instanceof MediusServer) {
			MediusServer mediusServer = (MediusServer) Clank.getInstance().getServer();
			switch (mediusServer.getEmulationMode()) {
			case MEDIUS_AUTHENTICATION_SERVER:
				return ((MasConfig) Clank.getInstance().getConfig()).getLocations().get(0);
			case MEDIUS_LOBBY_SERVER:
				return ((MlsConfig) Clank.getInstance().getConfig()).getLocations().get(0);
			default:
				return null;
			}
		}
		return null;
	}
	
	public ChannelConfig getChannel() {
		return ((MlsConfig) Clank.getInstance().getConfig()).getChannels().get(0);
	}
	
	public ChannelConfig getChannelById(int id) {
		for (final ChannelConfig channel : ((MlsConfig) Clank.getInstance().getConfig()).getChannels()) {
			if (channel.getId() == id) {
				return channel;
			}
		}
		return null;
	}
	
	public int getChannelActivePlayerCountById(int id) {
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
	
	public void updatePlayerStatusFromDme(String mlsToken, int worldId, MediusPlayerStatus status) {
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
		logger.info("Updating player from DME mlsToken: " + mlsToken);
		logger.info("Updating player from DME world: " + Integer.toString(worldId));
		logger.info("Updating player from DME Status: " + status.toString());
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
