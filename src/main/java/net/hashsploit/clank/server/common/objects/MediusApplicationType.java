package net.hashsploit.clank.server.common.objects;

public enum MediusApplicationType {
	
	MEDIUS_APP_TYPE_GAME(0),
	
	LOBBY_CHAT_CHANNEL(1);
	
	public final int value;
	
	private MediusApplicationType(int value) {
		this.value = value;
	}
	
}
