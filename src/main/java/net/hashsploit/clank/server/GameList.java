package net.hashsploit.clank.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;

public class GameList {
	
	private int gameIdCounter;
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

	public ArrayList<MediusGame> getGames() {
		// get games in staging
		ArrayList<MediusGame> result = new ArrayList<MediusGame>();
		
		for (MediusGame game : gameSet.values()) {
			if (game.getWorldStatusInt() == 1) // Staging
		    result.add(game);
		}
		
		return result;
	}
	
}
