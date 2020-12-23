package net.hashsploit.clank;

/**
 * The server mode in which Clank is currently running as.
 * 
 * @author hashsploit
 *
 */
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
	 * Handles universe information synchronization component.
	 */
	MEDIUS_UNIVERSE_INFORMATION_SERVER(4),
	
	/**
	 * NAT server component.
	 */
	NAT_SERVER(8),

	/**
	 * DME server component.
	 */
	DME_SERVER(16);
	
	private final int modeId;
	
	private EmulationMode(int modeId) {
		this.modeId = modeId;
	}
	
	/**
	 * Get the integer of this bitmask.
	 * @return
	 */
	public final int getValue() {
		return modeId;
	}
	
}
