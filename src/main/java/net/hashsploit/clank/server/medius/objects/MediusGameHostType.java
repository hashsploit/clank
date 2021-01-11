package net.hashsploit.clank.server.medius.objects;

public enum MediusGameHostType {
	
	HOST_CLIENT_SERVER(0),
	
	HOST_INTEGRATED_SERVER(1),
	
	HOST_PEER_TO_PEER(2),
	
	HOST_LAN_PLAY(3),
	
	HOST_CLIENT_SERVER_AUX_UDP(4);
	
	private final int value;

	private MediusGameHostType(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
