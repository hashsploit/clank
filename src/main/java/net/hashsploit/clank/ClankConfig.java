package net.hashsploit.clank;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public class ClankConfig {
	
	private Properties properties;
	
	public ClankConfig(String filename) throws IOException {
		this.properties = new Properties();
		this.properties.load(new FileReader(new File(filename)));
	}
	
	/**
	 * Get the raw properties object.
	 * @return
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * Get the logging level.
	 * @return
	 */
	public Level getLogLevel() {
		return Level.parse(properties.getProperty("LogLevel"));
	}
	
	/**
	 * Get the MediusComponent
	 * @return
	 */
	public ServerComponent getServerComponent() {
		return ServerComponent.valueOf(properties.getProperty("EmulationMode"));
	}
	
	/**
	 * Get the server max capacity.
	 * @return
	 */
	public int getCapacity() {
		return Integer.parseInt(properties.getProperty("Capacity"));
	}
	
	/**
	 * Get the client timeout.
	 * @return
	 */
	public int getClientTimeout() {
		return Integer.parseInt(properties.getProperty("ClientTimeout"));
	}
	
	/**
	 * Get the API URL.
	 * @return
	 */
	public String getApiUrl() {
		return properties.getProperty("ApiUrl");
	}
	
	/**
	 * Get the API Key.
	 * @return
	 */
	public String getApiKey() {
		return properties.getProperty("ApiKey");
	}
	
	/**
	 * Get the DB Host.
	 * @return
	 */
	public String getDbHost() {
		return properties.getProperty("DbHost");
	}
	
	/**
	 * Get the DB Name.
	 * @return
	 */
	public String getDbDatabase() {
		return properties.getProperty("DbDatabase");
	}
	
	/**
	 * Get the DB Username.
	 * @return
	 */
	public String getDbUsername() {
		return properties.getProperty("DbUsername");
	}
	
	/**
	 * Get the DB Password.
	 * @return
	 */
	public String getDbPassword() {
		return properties.getProperty("DbPassword");
	}
	
	/**
	 * Get the Discord Webhook for server start.
	 * @return
	 */
	public String getDiscordWebhook_start() {
		return properties.getProperty("DiscordWebhook_shart");
	}

	/**
	 * Get the Discord Webhook for server stop.
	 * @return
	 */
	public String getDiscordWebhook_shutdown() {
		return properties.getProperty("DiscordWebhook_shutdown");
	}
	
	//
	// Component specific configurations
	//
	
	/**
	 * Get whether SCE-RT encryption should be enabled.
	 * Used by the MAS, MLS, MPS, and MUIS.
	 * @return
	 */
	public boolean isEncryptionEnabled() {
		return Boolean.parseBoolean(properties.getProperty("Encryption"));
	}

	/**
	 * Get the address of which the server should bind to.
	 * @return
	 */
	public String getAddress() {
		return properties.getProperty("Address");
	}
	
	/**
	 * Get the port of which the server should listen on.
	 * @return
	 */
	public int getPort() {
		return Integer.parseInt(properties.getProperty("Port"));
	}
	
	/**
	 * Get the number of parent threads the server should use.
	 * @return
	 */
	public int getParentThreads() {
		return Integer.parseInt(properties.getProperty("ServerParentThreads"));
	}
	
	/**
	 * Get the number of child threads the server should use.
	 * @return
	 */
	public int getChildThreads() {
		return Integer.parseInt(properties.getProperty("ServerChildThreads"));
	}

	/**
	 * Check if the MAS whitelist is enabled.
	 * @return
	 */
	public boolean isWhitelistEnabled() {
		return Boolean.parseBoolean(properties.getProperty("WhitelistEnabled"));
	}
	
	/**
	 * Get an array of whitelisted players.
	 * @return
	 */
	public String[] getWhitelistedPlayers() {
		return properties.getProperty("WhitelistPlayers").split(",");
	}

}
