package net.hashsploit.clank.server;

public enum ClientState {
	
	/**
	 * Client is not authenticated yet.
	 */
	UNAUTHENTICATED_STAGE_1,
	
	/**
	 * Client is not authenticated yet.
	 */
	UNAUTHENTICATED_STAGE_2,
	
	/**
	 * Client is authenticating.
	 */
	AUTHENTICATING,
	
	/**
	 * Client has successfully authenticated.
	 */
	AUTHENTICATED,
	
	/**
	 * Client is connected and logged in.
	 */
	LOGGED_IN
	
}
