package net.hashsploit.clank.config;

public enum DiscordWebhookNames {
	
	/**
	 * When the server starts.
	 */
	STARTUP("start"),
	
	/**
	 * When the server is shutdown.
	 */
	SHUTDOWN("shutdown");
	
	private final String name;
	
	private DiscordWebhookNames(final String name) {
		this.name = name;
	}
	
}
