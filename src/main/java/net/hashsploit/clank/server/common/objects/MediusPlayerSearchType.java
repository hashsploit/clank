package net.hashsploit.clank.server.common.objects;

public enum MediusPlayerSearchType {
	
	PLAYER_ACCOUNT_ID(0),
	
	PLAYER_ACCOUNT_NAME(1);
	
	private final int value;

	private MediusPlayerSearchType(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
