package net.hashsploit.clank.server;

public class Player {
	
	private final MediusClient client;
	private String username;
	
	public Player(MediusClient client) {
		this.client = client;
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
	
}
