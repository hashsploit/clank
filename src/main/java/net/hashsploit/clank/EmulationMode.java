package net.hashsploit.clank;

import com.google.gson.annotations.SerializedName;

/**
 * The server emulation mode in which Clank is currently running as.
 */
public enum EmulationMode {
	
	/**
	 * Handles universe information synchronization component.
	 */
	@SerializedName("MEDIUS_UNIVERSE_INFORMATION_SERVER")
	MEDIUS_UNIVERSE_INFORMATION_SERVER(1),
	
	/**
	 * Handles player login and token authentication.
	 */
	@SerializedName("MEDIUS_AUTHENTICATION_SERVER")
	MEDIUS_AUTHENTICATION_SERVER(2),
	
	/**
	 * Handles out-of-game lobby, chat, clan and player management.
	 */
	@SerializedName("MEDIUS_LOBBY_SERVER")
	MEDIUS_LOBBY_SERVER(4),
	
	/**
	 * NAT server component.
	 */
	@SerializedName("NAT_SERVER")
	NAT_SERVER(8),

	/**
	 * DME server component.
	 */
	@SerializedName("DME_SERVER")
	DME_SERVER(16),
	
	/**
	 * Monolith server component. (all servers in-one process, useful for debugging)
	 */
	@SerializedName("MONOLITH")
	MONOLITH(32);

	private final int value;

	EmulationMode(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
