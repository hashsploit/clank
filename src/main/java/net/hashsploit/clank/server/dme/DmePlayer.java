package net.hashsploit.clank.server.dme;

public class DmePlayer {
	
	private final DmeClient client;
	
	public DmePlayer(DmeClient client) {
		this.client = client;
	}
	
	
	
	public DmeClient getClient() {
		return client;
	}
	
}
