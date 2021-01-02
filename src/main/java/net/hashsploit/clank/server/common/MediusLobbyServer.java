package net.hashsploit.clank.server.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.config.configs.MlsConfig;
import net.hashsploit.clank.server.GameList;
import net.hashsploit.clank.server.MediusGame;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.common.objects.ChannelConfig;
import net.hashsploit.clank.server.common.objects.Clan;
import net.hashsploit.clank.server.common.objects.LocationConfig;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.rpc.ClankMlsRpcServer;
import net.hashsploit.clank.server.rpc.RpcServerConfig;
import net.hashsploit.clank.utils.MediusMessageMapInitializer;
import net.hashsploit.clank.utils.Utils;

public class MediusLobbyServer extends MediusServer {

	private final GameList gameList;
	private final HashSet<Location> locations;
	private final HashSet<Channel> channels;
	private final HashSet<Player> players;
	private final HashSet<Clan> clans;

	public MediusLobbyServer(String address, int port, int parentThreads, int childThreads) {
		super(EmulationMode.MEDIUS_LOBBY_SERVER, address, port, parentThreads, childThreads);

		this.mediusMessageMap = MediusMessageMapInitializer.getMlsMap();

		this.gameList = new GameList();
		this.locations = new HashSet<Location>();
		this.channels = new HashSet<Channel>();
		this.players = new HashSet<Player>();
		this.clans = new HashSet<Clan>();

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

	/**
	 * Update a player's status by their Account Id.
	 * 
	 * @param accountId
	 * @param status
	 */
	public synchronized void updatePlayerStatus(int accountId, MediusPlayerStatus status) {
		for (final Player player : players) {
			if (player.getAccountId() == accountId) {
				player.updateStatus(status);
				return;
			}
		}
	}

	/**
	 * Update a player's status by their player object.
	 * 
	 * @param player
	 * @param status
	 */
	public synchronized void updatePlayerStatus(Player player, MediusPlayerStatus status) {

		// Check if player is disconnecting, remove player from list
		if (status == MediusPlayerStatus.MEDIUS_PLAYER_DISCONNECTED) {
			players.remove(player);
			return;
		}

		// Make sure player has logged in
		// FIXME: ???
		if (player.getAccountId() == null) {
			throw new IllegalStateException("Player did not login yet!");
		}

		// If player isn't in the players, add it
		if (!players.contains(player)) {
			players.add(player);
		}

		player.updateStatus(status);
	}

	/**
	 * Update a DME World status identified by the world Id.
	 * 
	 * @param worldId
	 * @param worldStatus
	 */
	public synchronized void updateDmeWorldStatus(int worldId, MediusWorldStatus worldStatus) {
		logger.finest("Updating world from DME worldId: " + Integer.toString(worldId));
		logger.finest("Updating world from DME World Status: " + worldStatus.toString());
		gameList.updateGameWorldStatus(worldId, worldStatus);
	}

	/**
	 * Update the player status from DME.
	 * 
	 * @param mlsToken
	 * @param worldId
	 * @param status
	 */
	public synchronized void updatePlayerStatusFromDme(String mlsToken, int worldId, MediusPlayerStatus status) {
		// PlayerStatus is from gRPC
		// This method is called from gRPC DME -> MLS
		// PlayerStatus: DISCONNECTED(0), CONNECTED(1), STAGING(2), ACTIVE(3), UNRECOGNIZED(-1)

		logger.finest("Updating player from DME mlsToken: " + mlsToken);
		logger.finest("Updating player from DME world: " + Integer.toString(worldId));
		logger.finest("Updating player from DME Status: " + status.toString());
		int accountId = Clank.getInstance().getDatabase().getAccountIdFromMlsToken(mlsToken);

		this.updatePlayerStatus(accountId, status);
	}

	/**
	 * Get a list of players in staging or in-game.
	 * 
	 * @param worldId
	 * @return
	 */
	public HashSet<Player> getGameWorldPlayers(int worldId) {
		final HashSet<Player> result = new HashSet<Player>();
		for (final Player player : players) {
			if (player.getGameWorldId() == worldId) {
				result.add(player);
			}
		}
		return result;
	}

	/**
	 * Get a list of players in a lobby World Id.
	 * 
	 * @param worldId
	 * @return
	 */
	public HashSet<Player> getLobbyWorldPlayers(int worldId) {
		final HashSet<Player> result = new HashSet<Player>();
		for (final Player player : players) {
			if (player.getChatWorldId() == worldId) {
				result.add(player);
			}
		}
		return result;
	}

	/**
	 * Get a player's status via their Account Id, this will return null if the
	 * Account Id is not found.
	 * 
	 * @param accountId
	 * @return
	 */
	public MediusPlayerStatus getPlayerStatus(int accountId) {
		for (final Player player : players) {
			if (player.getAccountId() == accountId) {
				return player.getStatus();
			}
		}
		return null;
	}

}
