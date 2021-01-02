package net.hashsploit.clank.server.common.objects;

public enum MediusWorldSecurityLevelType {

	/**
	 * No security on world.
	 */
	WORLD_SECURITY_NONE(0),

	/**
	 * Password required to join as a player
	 */
	WORLD_SECURITY_PLAYER_PASSWORD(1),

	/**
	 * World is closed to new players
	 */
	WORLD_SECURITY_CLOSED(2),

	/**
	 * Password is required to join as a spectator
	 */
	WORLD_SECURITY_SPECTATOR_PASSWORD(4);

	private final int value;

	private MediusWorldSecurityLevelType(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}

}
