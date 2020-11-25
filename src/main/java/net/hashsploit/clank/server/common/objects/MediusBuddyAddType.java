package net.hashsploit.clank.server.common.objects;

public enum MediusBuddyAddType {

	ADD_SINGLE(0),
	
	ADD_SYMMETRIC(1);
	
	private final int value;

	private MediusBuddyAddType(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
