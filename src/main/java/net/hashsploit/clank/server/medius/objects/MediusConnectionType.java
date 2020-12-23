package net.hashsploit.clank.server.medius.objects;

public enum MediusConnectionType {
	
	MODEM(0),
	
	ETHERNET(1),
	
	WIRELESS(2);
	
	private final int value;

	private MediusConnectionType(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}

}
