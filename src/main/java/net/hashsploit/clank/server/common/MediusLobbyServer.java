package net.hashsploit.clank.server.common;

import java.io.IOException;
import java.util.ArrayList;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.config.configs.MlsConfig;
import net.hashsploit.clank.config.objects.ChannelConfig;
import net.hashsploit.clank.config.objects.LocationConfig;
import net.hashsploit.clank.server.GameList;
import net.hashsploit.clank.server.MediusGame;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.PlayerList;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.rpc.ClankMlsRpcServer;
import net.hashsploit.clank.server.rpc.RpcServerConfig;
import net.hashsploit.clank.utils.MediusMessageMapInitializer;
import net.hashsploit.clank.utils.Utils;

public class MediusLobbyServer extends MediusServer{
	
	private final GameList gameList;
	private final PlayerList playerList;
	
	public MediusLobbyServer(EmulationMode emulationMode, String address, int port, int parentThreads,
			int childThreads) {
		super(emulationMode, address, port, parentThreads, childThreads);
		
		this.mediusMessageMap = MediusMessageMapInitializer.getMlsMap();
		
		this.gameList = new GameList();
		this.playerList = new PlayerList();
		
		final RpcServerConfig rpcConfig = ((MediusConfig) Clank.getInstance().getConfig()).getRpcServerConfig();
		String rpcAddress = rpcConfig.getAddress();
		final int rpcPort = rpcConfig.getPort();
		
		if (rpcAddress == null) {
			Utils.getPublicIpAddress();
		}
		
		try {
			rpcServer = new ClankMlsRpcServer(rpcAddress, rpcPort, this);
			rpcServer.start();
		} catch (IOException e) {
			logger.severe(String.format("Failed to create RPC service: %s", e.getMessage()));
			Clank.getInstance().shutdown();
		}
	
	}
	
	
	/*
	 * 
	 * Logic
	 *  
	 */
	
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
		return ((MlsConfig) Clank.getInstance().getConfig()).getLocations().get(0);
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

	public synchronized void updatePlayerStatus(Player player, MediusPlayerStatus status) {
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
	public synchronized void updateDmeWorldStatus(int worldId, MediusWorldStatus worldStatus) {
		logger.info("Updating world from DME worldId: " + Integer.toString(worldId));
		logger.info("Updating world from DME World Status: " + worldStatus.toString());
		gameList.updateGameWorldStatus(worldId, worldStatus);
	}
	
	public synchronized void updatePlayerStatusFromDme(String mlsToken, int worldId, MediusPlayerStatus status) {
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
