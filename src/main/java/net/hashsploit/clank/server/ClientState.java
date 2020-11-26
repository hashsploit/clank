package net.hashsploit.clank.server;

/**
 * A bitmask of possible "flags" or "states" a client has on the server-side.
 * For example, a client can be AUTHENTICATED and THROTTLED at the same time.
 * 
 * @author hashsploit
 *
 */
public enum ClientState {
	
	/**
	 * Client is not authenticated and is not in the process of authenticating.
	 */
	UNAUTHENTICATED(1),
	
	/**
	 * Client is in the process of authenticating.
	 */
	AUTHENTICATING(2),
	
	/**
	 * Client has successfully authenticated.
	 */
	AUTHENTICATED(4),
	
	/**
	 * Client is being throttled.
	 */
	THROTTLED(8),
	
	/**
	 * Client has operator control over the server.
	 */
	OPERATOR(16),
	
	/**
	 * Client is currently in spectator mode.
	 */
	SPECTATE(32);
	
	private final int clientState;
	
	private ClientState(int clientState) {
		this.clientState = clientState;
	}
	
	/**
	 * Get the integer value of the bitmask.
	 * @return
	 */
	public final int getValue() {
		return clientState;
	}
	
}
