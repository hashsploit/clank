package net.hashsploit.clank;

public enum EventType {
	
	/**
	 * Server initialization event.
	 */
	SERVER_INIT_EVENT,
	
	/**
	 * Plugin initialization event.
	 */
	PLUGIN_INIT_EVENT,
	
	/**
	 * A normal update event.
	 */
	TICK_EVENT,
	
	/**
	 * A client connected.
	 */
	CONNECT_EVENT,
	
	/**
	 * A client disconnected or timed out.
	 */
	DISCONNECT_EVENT,
	
	/**
	 * Plugin shutdown event.
	 */
	PLUGIN_SHUTDOWN_EVENT,
	
	/**
	 * Server shutdown.
	 */
	SERVER_SHUTDOWN_EVENT
	
}
