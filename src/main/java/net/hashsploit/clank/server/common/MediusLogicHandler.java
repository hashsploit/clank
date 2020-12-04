package net.hashsploit.clank.server.common;

import java.util.ArrayList;
import java.util.HashMap;

import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;

public class MediusLogicHandler {
	
	private GameList gameList = new GameList();
	
	
	private final String location = "Aquatos";
	private final int locationId = 40;
	
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
	
	public synchronized int getAccountId(String username) {
		if (username.equals("Smily")) {
			return 50;
		}
		else if (username.equals("hashsploit")) {
			return 51;
		}
		else if (username.equals("Test clan username")) { // Used in MediusGetMyClansHandler
			return 3;
		}
		
		return 0;
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


}
