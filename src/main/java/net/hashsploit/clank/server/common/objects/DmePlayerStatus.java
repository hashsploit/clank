package net.hashsploit.clank.server.common.objects;

public enum DmePlayerStatus {
	
	DISCONNECTED(0),
	
	CONNECTING(1),
	
	CONNECTED(2);
	
	private final int value;

	private DmePlayerStatus(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
