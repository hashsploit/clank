package net.hashsploit.clank.server.medius.objects;

public enum MediusApplicationType {
	
	MEDIUS_APP_TYPE_GAME(0),
	
	LOBBY_CHAT_CHANNEL(1);
	
	private final int value;
	
	private MediusApplicationType(int value) {
		this.value = value;
	}
	
	public final int getValue() {
		return value;
	}
	
}
