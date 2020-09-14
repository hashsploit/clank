package net.hashsploit.clank.server.medius.objects;

public enum MediusPlayerStatus {
	
	/**
	 * Player is not connected.
	 */
	MEDIUS_PLAYER_DISCONNECTED(0),
	
	/**
	 * Player is currently on an authentication world.
	 * @return
	 */
	MEDIUS_PLAYER_IN_AUTH_WORLD(1),
	
	/**
	 * Player is currently on a chat channel.
	 */
	MEDIUS_PLAYER_IN_CHAT_WORLD(2),
	
	/**
	 * Player is currently on a game world.
	 */
	MEDIUS_PLAYER_IN_GAME_WORLD(3),
	
	/**
	 * Player is online in another universe.
	 */
	MEDIUS_PLAYER_IN_OTHER_UNIVERSE(4);
	
	private final int value;

	private MediusPlayerStatus(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
