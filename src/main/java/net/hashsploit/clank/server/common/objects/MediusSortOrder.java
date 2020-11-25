package net.hashsploit.clank.server.common.objects;

public enum MediusSortOrder {

	MEDIUS_ASCENDING(0),
	
	MEDIUS_DESCENDING(1);
	
	private final int value;

	private MediusSortOrder(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
