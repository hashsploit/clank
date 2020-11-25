package net.hashsploit.clank;

public enum EmulationMode {
	
	/**
	 * Handles player login and token authentication.
	 */
	MEDIUS_AUTHENTICATION_SERVER(1),
	
	/**
	 * Handles out-of-game lobby, chat, clan and player management.
	 */
	MEDIUS_LOBBY_SERVER(2),
	
	/**
	 * Handles non p2p in-game communication.
	 */
	MEDIUS_PROXY_SERVER(4),
	
	/**
	 * Handles universe information synchronization component.
	 */
	MEDIUS_UNIVERSE_INFORMATION_SERVER(8),
	
	/**
	 * NAT server component.
	 */
	NAT_SERVER(16),

	/**
	 * DME server component.
	 */
	DME_SERVER(32);
	
	private final int modeId;
	
	private EmulationMode(int modeId) {
		this.modeId = modeId;
	}
	
	public final int getValue() {
		return modeId;
	}
	
}
