package net.hashsploit.clank.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import net.hashsploit.clank.server.dme.DmeTcpClient;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.serializers.CreateGameOneRequest;

public class GameList {
	private static final Logger logger = Logger.getLogger(GameList.class.getName());

	private int gameIdCounter;
	// DME World ID -> MediusGame
	private HashMap<Integer, MediusGame> gameSet = new HashMap<Integer, MediusGame>();
	
	public GameList() {
		gameIdCounter = 1;
	}
	
	public int getNewGameId(CreateGameOneRequest req) {
		gameSet.put(gameIdCounter, new MediusGame(gameIdCounter, req));
		gameIdCounter++;
		return gameIdCounter-1;
	}

	public MediusGame getGame(int worldId) {
		return gameSet.get(worldId);
	}
	
	public void updateGameWorldStatus(int worldId, MediusWorldStatus worldStatus) {
		MediusGame game = gameSet.get(worldId);
		
		if (game == null) {		
			if (worldStatus == MediusWorldStatus.WORLD_CLOSED) {
				logger.info("DmeWorldId: " + worldId + " has already been destroyed!");
			}
			else {
				throw new IllegalStateException("No world with id " + worldId + " exists! Cannot update status to: " + worldStatus.toString());
			}
		}
		game.updateStatus(worldStatus);

		if (worldStatus == MediusWorldStatus.WORLD_CLOSED) {
			gameSet.remove(worldId);
		}		
	}

	public ArrayList<MediusGame> getGames() {
		// get games in staging
		ArrayList<MediusGame> result = new ArrayList<MediusGame>();
		
		for (MediusGame game : gameSet.values()) {
			if (game.getWorldStatus() == MediusWorldStatus.WORLD_STAGING) // Staging TODO: make this an enum
		    result.add(game);
		}
		
		return result;
	}
	
}
