package net.hashsploit.clank.server.dme;

public class DmePlayer {
	
	private final DmeClient client;
	private DmeUdpClient udpClient;
	
	public DmePlayer(DmeClient client) {
		this.client = client;
	}
	
	public DmeClient getClient() {
		return client;
	}
	
	public void setUdpClient(DmeUdpClient udpClient) {
		this.udpClient = udpClient;
	}
	
	public DmeUdpClient getUdpClient() {
		return udpClient;
	}
	
}
