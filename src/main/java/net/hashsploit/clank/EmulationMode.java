package net.hashsploit.clank;

/**
 * The server mode in which Clank is currently running as.
 * 
 * @author hashsploit
 *
 */
public enum EmulationMode {
	
	/**
	 * Handles universe information synchronization component.
	 */
	MEDIUS_UNIVERSE_INFORMATION_SERVER(1),
	
	/**
	 * Handles player login and token authentication.
	 */
	MEDIUS_AUTHENTICATION_SERVER(2),
	
	/**
	 * Handles out-of-game lobby, chat, clan and player management.
	 */
	MEDIUS_LOBBY_SERVER(4),
	
	/**
	 * NAT server component.
	 */
	NAT_SERVER(8),

	/**
	 * DME server component.
	 */
	DME_SERVER(16);
	
	public final int value;
	
	private EmulationMode(int value) {
		this.value = value;
	}
	
}
