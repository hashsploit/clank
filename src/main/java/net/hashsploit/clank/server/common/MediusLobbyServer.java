package net.hashsploit.clank.server.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.config.configs.MlsConfig;
import net.hashsploit.clank.server.GameList;
import net.hashsploit.clank.server.MediusGame;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.common.objects.Channel;
import net.hashsploit.clank.server.common.objects.Clan;
import net.hashsploit.clank.server.common.objects.Location;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.rpc.ClankMlsRpcServer;
import net.hashsploit.clank.server.rpc.RpcServerConfig;
import net.hashsploit.clank.utils.MediusMessageMapInitializer;
import net.hashsploit.clank.utils.Utils;

public class MediusLobbyServer extends MediusServer {

	private final HashSet<Player> players;
	private final HashSet<Clan> clans;
	private final List<Location> locations;
	private final List<Channel> channels;
	private final GameList gameList;

	public MediusLobbyServer(String address, int port, int parentThreads, int childThreads) {
		super(EmulationMode.MEDIUS_LOBBY_SERVER, address, port, parentThreads, childThreads);

		this.mediusMessageMap = MediusMessageMapInitializer.getMlsMap();

		MlsConfig config = ((MlsConfig) Clank.getInstance().getConfig());
		
		this.players = new HashSet<Player>();
		this.clans = new HashSet<Clan>();
		this.locations = new ArrayList<Location>(config.getLocations());
		this.channels = new ArrayList<Channel>(config.getChannels());
		this.gameList = new GameList();

		final RpcServerConfig rpcConfig = ((MediusConfig) Clank.getInstance().getConfig()).getRpcServerConfig();
		String rpcAddress = rpcConfig.getAddress();
		final int rpcPort = rpcConfig.getPort();

		if (rpcAddress == null) {
			rpcAddress = Utils.getPublicIpAddress();
		}

		try {
			rpcServer = new ClankMlsRpcServer(rpcAddress, rpcPort, this);
			rpcServer.start();
		} catch (IOException e) {
			logger.severe(String.format("Failed to create RPC service: %s", e.getMessage()));
			Clank.getInstance().shutdown();
		}

	}

	/**
	 * Get all players.
	 * @return
	 */
	public HashSet<Player> getPlayers() {
		return players;
	}
	
	/**
	 * Get all locations.
	 * @return
	 */
	public List<Location> getLocations() {
		return locations;
	}

	/**
	 * Get all channels.
	 * @return
	 */
	public List<Channel> getChannels() {
		return channels;
	}
	
	/**
	 * Get all clans.
	 * @return
	 */
	public HashSet<Clan> getClans() {
		return clans;
	}
	
	/**
	 * Returns a list of games.
	 * @return
	 */
	public ArrayList<MediusGame> getGames() {
		return gameList.getGames();
	}

	/**
	 * Get a MediusGame by World Id.
	 * @param worldId
	 * @return
	 */
	public MediusGame getGame(int worldId) {
		return gameList.getGame(worldId);
	}

	/**
	 * Get a new game id from a CreateGameOne request.
	 * @param req
	 * @return
	 */
	public int getNewGameId(CreateGameOneRequest req) {
		return gameList.getNewGameId(req);
	}

	/**
	 * Get a Location by its id.
	 * @param locationId
	 * @return
	 */
	public Location getLocationById(int locationId) {
		for (final Location location : ((MlsConfig) Clank.getInstance().getConfig()).getLocations()) {
			if (location.getId() == locationId) {
				return location;
			}
		}
		return null;
	}

	/**
	 * Get a Channel by its id.
	 * @param channelId
	 * @return
	 */
	public Channel getChannelById(int channelId) {
		for (final Channel channel : ((MlsConfig) Clank.getInstance().getConfig()).getChannels()) {
			if (channel.getId() == channelId) {
				return channel;
			}
		}
		return null;
	}

	/**
	 * Get the number of active players in a specific channel by its id.
	 * @param id
	 * @return
	 */
	public int getChannelActivePlayerCountById(int id) {
		int count = 0;
		for (final Player player : players) {
			if (player.getChatWorldId() == id && player.getStatus() == MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD) {
				count++;
			}
		}
		return count;
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
	 * Update a DME World status identified by the World Id.
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
