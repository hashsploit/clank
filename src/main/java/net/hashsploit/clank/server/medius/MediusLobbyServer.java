package net.hashsploit.clank.server.medius;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.config.configs.MlsConfig;
import net.hashsploit.clank.config.objects.ChannelConfig;
import net.hashsploit.clank.config.objects.LocationConfig;
import net.hashsploit.clank.rt.RtPacketMap;
import net.hashsploit.clank.server.GameList;
import net.hashsploit.clank.server.MediusGame;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.PlayerList;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.rpc.ClankMlsRpcServer;
import net.hashsploit.clank.server.rpc.RpcServerConfig;
import net.hashsploit.clank.utils.MediusMessageMapInitializer;
import net.hashsploit.clank.utils.Utils;

public class MediusLobbyServer extends MediusServer {

	private final GameList gameList;
	private final PlayerList playerList;

	public MediusLobbyServer(EmulationMode emulationMode, String address, int port, int parentThreads, int childThreads) {
		super(emulationMode, address, port, parentThreads, childThreads);
		
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
	 * ================================================================== Player
	 * update methods
	 * ==================================================================
	 */

	public synchronized void updatePlayerStatus(Player player, MediusPlayerStatus status) {
		playerList.updatePlayerStatus(player, status);
	}

	/*
	 * ================================================================== gRPC
	 * Update methods from DME
	 * ==================================================================
	 * 
	 */

	/**
	 * Update a DME World status identified by the world Id.
	 * @param worldId
	 * @param worldStatus
	 */
	public synchronized void updateDmeWorldStatus(int worldId, MediusWorldStatus worldStatus) {
		gameList.updateGameWorldStatus(worldId, worldStatus);
	}

	/**
	 * Update the player status from DME.
	 * @param mlsToken
	 * @param worldId
	 * @param status
	 */
	public synchronized void updatePlayerStatusFromDme(String mlsToken, int worldId, MediusPlayerStatus status) {
		// PlayerStatus is from gRPC
		// This method is called from gRPC DME -> MLS
		/*
		 * PlayerStatus: DISCONNECTED(0), CONNECTED(1), STAGING(2), ACTIVE(3),
		 * UNRECOGNIZED(-1),
		 */
		// Update the gameWorld. Update the playerList
		int accountId = Clank.getInstance().getDatabase().getAccountIdFromSessionKey(mlsToken);
		playerList.updatePlayerStatus(accountId, status);
	}

	/**
	 * Get players in-game/staging.
	 * 
	 * @param worldIdRequested
	 * @return
	 */
	public List<Player> getGameWorldPlayers(int worldIdRequested) {
		return playerList.getPlayersByGameWorld(worldIdRequested);
	}

	/**
	 * Get players in lobby.
	 * 
	 * @param worldId
	 * @return
	 */
	public List<Player> getLobbyWorldPlayers(int worldId) {
		ArrayList<Player> result = playerList.getPlayersByLobbyWorld(worldId);
		return result;
	}


	public MediusPlayerStatus getPlayerStatus(int accountId) {
		return playerList.getPlayerStatus(accountId);
	}

}
