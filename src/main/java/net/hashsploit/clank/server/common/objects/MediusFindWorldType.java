package net.hashsploit.clank.server.common.objects;

public enum MediusFindWorldType {
	
	FIND_GAME_WORLD(0),
	
	FIND_LOBBY_WORLD(1),
	
	FIND_ALL_WORLDS(2);
	
	private final int value;

	private MediusFindWorldType(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
