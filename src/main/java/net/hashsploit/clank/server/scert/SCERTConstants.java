package net.hashsploit.clank.server.scert;

public enum SCERTConstants {
	
	NET_MAX_NETADDRESS_LENGTH(16),
	
	NET_ADDRESS_LIST_COUNT(2);
	
	private final int value;

	private SCERTConstants(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
