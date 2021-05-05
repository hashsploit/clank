package net.hashsploit.clank.server;

import java.util.logging.Logger;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;

public class Player {
	private static final Logger logger = Logger.getLogger(Player.class.getName());

	private final MediusClient client;
	private MediusPlayerStatus playerStatus;
	private String username;
	private int accountId;
	private String sessionKey;
	private int chatWorldId;
	private int clanId;	
	

	public Player(MediusClient client, MediusPlayerStatus status) {
		this.client = client;
		this.playerStatus = status;
		this.chatWorldId = 0;
	}

	public String toString() {
		String s = "Player[Username: '" + username + "', SessionKey: " + sessionKey + ", Status: " + playerStatus.toString() + ", accountId: " + accountId + ", ChatWorld: " + chatWorldId + "]";
		return s;

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
		this.playerStatus = status;
		logger.info("PlayerStatus updated: " + this.toString());
	}

	public Integer getAccountId() {
		return accountId;
	}

	public MediusPlayerStatus getStatus() {
		return playerStatus;
	}

	public int getChatWorldId() {
		return chatWorldId;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}


}
