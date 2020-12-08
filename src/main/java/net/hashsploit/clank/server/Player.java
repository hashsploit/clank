package net.hashsploit.clank.server;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;

public class Player {
	
	private final MediusClient client;
	private MediusPlayerStatus playerStatus;
	private String username;
	private int accountId;
	
	private int chatWorldId;
	
	public Player(MediusClient client, MediusPlayerStatus status) {
		this.client = client;
		this.playerStatus = status;
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
		accountId = playerAccountId;
	}
	
	public void setChatWorld(int chatWorldId) {
		this.chatWorldId = chatWorldId;
	}

	public void updateStatus(MediusPlayerStatus status) {
		this.playerStatus = status;
	}

	public Integer getAccountId() {
		return accountId;
	}
		
}
