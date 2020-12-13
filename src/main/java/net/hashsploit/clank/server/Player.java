package net.hashsploit.clank.server;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;

public class Player {
	
	private final MediusClient client;
	private MediusPlayerStatus playerStatus;
	private String username;
	private int accountId;
	
	private int gameWorldId;
	
	private int chatWorldId;
	
	public Player(MediusClient client, MediusPlayerStatus status) {
		this.client = client;
		this.playerStatus = status;
		this.gameWorldId = 0;
		this.chatWorldId = 0;
	}
	
	public String toString() {
		return "Player: \n" + 
				"Username: " + username + "\n" + 
				"State: " + playerStatus.name() + "\n" + 
				"AccountID: " + Integer.toString(accountId) + "\n" + 
				"ChatWorld: " + Integer.toString(chatWorldId) + "\n" + 
				"GameWorld: " + Integer.toString(gameWorldId);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		this.accountId = Clank.getInstance().getDatabase().getAccountId(username);
	}

	public MediusClient getClient() {
		return client;
	}

	public void setAccountId(int playerAccountId) {
		this.accountId = playerAccountId;
		this.username = Clank.getInstance().getDatabase().getUsername(accountId);
	}
	
	public void setChatWorld(int chatWorldId) {
		this.chatWorldId = chatWorldId;
	}

	public void updateStatus(MediusPlayerStatus status) {
		if (status != MediusPlayerStatus.MEDIUS_PLAYER_IN_GAME_WORLD) {
			this.gameWorldId = 0;
		}
		this.playerStatus = status;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public MediusPlayerStatus getStatus() {
		return playerStatus;
	}

	public int getGameWorldId() {
		return gameWorldId;
	}

	public int getChatWorldId() {
		return chatWorldId;
	}

	public void setGameWorldId(int gameWorldId) {
		this.gameWorldId = gameWorldId;
	}
		
}
