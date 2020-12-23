package net.hashsploit.clank.server.medius.objects;

public enum DmePlayerStatus {
	
	DISCONNECTED(0),
	
	CONNECTING(1),
		
	STAGING(2),
	
	ACTIVE(3);
	
	private final int value;

	private DmePlayerStatus(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
