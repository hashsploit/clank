package net.hashsploit.clank.server.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;

public class GameList {

	
	private HashMap<Integer, MediusGame> gameSet = new HashMap<Integer, MediusGame>();
	
	public int getNewGameId(CreateGameOneRequest req) {
		int id = 9;
		gameSet.put(id, new MediusGame(id, req));
		return id;
	}

	public MediusGame getGame(int worldId) {
		// TODO Auto-generated method stub
		return gameSet.get(worldId);
	}

	public ArrayList<MediusGame> getGames() {
		ArrayList<MediusGame> result = new ArrayList<MediusGame>();
		
		for (MediusGame game : gameSet.values()) {
		    result.add(game);
		}
		
		return result;
	}
	
}
