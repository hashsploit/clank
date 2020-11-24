package net.hashsploit.clank.server.common.objects;

public enum MediusAccountType {
	
	MEDIUS_CHILD_ACCOUNT(0),
	
	MEDIUS_MASTER_ACCOUNT(1);
	
	private final int value;

	private MediusAccountType(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
