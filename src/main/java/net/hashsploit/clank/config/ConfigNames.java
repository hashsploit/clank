package net.hashsploit.clank.config;

public enum ConfigNames {

	/**
	 * The server's emulation mode.
	 */
	EMULATION_MODE("mode"),

	/**
	 * The server's logger level.
	 */
	LOG_LEVEL("log_level"),

	/**
	 * The server's IP Address it should bind to.
	 */
	ADDRESS("address"),

	/**
	 * The server's port it should bind to.
	 */
	PORT("port"),

	/**
	 * The TCP server's parent threads.
	 */
	PARENT_THREADS("parent_threads"),

	/**
	 * The (TCP/UDP) server's child threads.
	 */
	CHILD_THREADS("child_threads"),

	/**
	 * The (UDP) server's UDP server listener threads.
	 */
	UDP_THREADS("udp_threads"),

	/**
	 * The (TCP) server's max connection capacity.
	 */
	CAPACITY("capacity"),

	/**
	 * The (TCP) server's socket timeout.
	 */
	TIMEOUT("timeout"),

	/**
	 * The (TCP/UDP) server's encryption.
	 */
	ENCRYPTION("encryption"),

	/**
	 * Used in MUIS to define the universe objects.
	 */
	UNIVERSES("universes"),

	/**
	 * Game application id.
	 */
	APPLICATION_ID("application_id"),

	/**
	 * The (UDP) DME server's address to bind to.
	 */
	UDP_ADDRESS("udp_address"),

	/**
	 * The (UDP) DME server's starting port (will incrementally increase according to the # of players joining).
	 */
	UDP_PORT("udp_port"),
	
	/**
	 * Whitelist object.
	 */
	WHITELIST("whitelist"),
	
	/**
	 * Whitelist is enabled.
	 */
	WHITELIST_ENABLED("enabled"),
	
	/**
	 * Whitelisted players.
	 */
	WHITELIST_PLAYERS("players"),
	
	/**
	 * Database object.
	 */
	DATABASE("database"),

	/**
	 * Database hostname/address.
	 */
	DATABASE_HOST("host"),
	
	/**
	 * The DBMS's database for the server.
	 */
	DATABASE_DATABASE("database"),
	
	/**
	 * Database username.
	 */
	DATABASE_USERNAME("username"),
	
	/**
	 * Database password.
	 */
	DATABASE_PASSWORD("password"),
	
	/**
	 * Medius Lobby Server object.
	 */
	MLS("mls"),
	
	/**
	 * Medius Lobby Server IPv4 address.
	 */
	MLS_ADDRESS("address"),
	
	/**
	 * Medius Lobby Server port.
	 */
	MLS_PORT("port"),
	
	/**
	 * NAT Server object.
	 */
	NAT("nat"),
	
	/**
	 * NAT Server IPv4 address.
	 */
	NAT_ADDRESS("address"),
	
	/**
	 * NAT Server port.
	 */
	NAT_PORT("port"),

	/**
	 * DME Server object.
	 */
	DME("dme"),
	
	/**
	 * DME TCP Server IPv4 address.
	 */
	DME_ADDRESS("address"),
	
	/**
	 * DME TCP Server port.
	 */
	DME_PORT("port"),
	
	/**
	 * RPC JSON object.
	 */
	RPC("rpc"),
	
	/**
	 * RPC Server JSON object.
	 */
	RPC_SERVER("rpc_server"),
	
	/**
	 * RPC Client Address.
	 */
	RPC_ADDRESS("address"),
	
	/**
	 * RPC Server Address.
	 */
	RPC_SERVER_ADDRESS("address"),
	
	/**
	 * RPC Client Port.
	 */
	RPC_PORT("port"),
	
	/**
	 * RPC Server Port.
	 */
	RPC_SERVER_PORT("port"),
	
	/**
	 * RPC Server Encryption JSON object.
	 */
	RPC_SERVER_ENCRYPTION("encryption"),

	/**
	 * RPC Server Encryption JSON object enabled.
	 */
	RPC_SERVER_ENCRYPTION_ENABLED("enabled"),

	/**
	 * RPC Server Encryption JSON object cert chain.
	 */
	RPC_SERVER_ENCRYPTION_CERT_CHAIN("cert_chain"),

	/**
	 * RPC Server Encryption JSON object private key.
	 */
	RPC_SERVER_ENCRYPTION_PRIVATE_KEY("private_key"),
	
	/**
	 * EULA/Policy agreement.
	 */
	POLICY("policy"),
	
	/**
	 * Announcements text.
	 */
	ANNOUNCEMENTS("announcements"),
	
	/**
	 * Player operators string array.
	 */
	OPERATORS("operators"),
	
	/**
	 * Array list of channel objects.
	 */
	CHANNELS("channels"),
	
	/**
	 * Channel object id.
	 */
	CHANNELS_ID("id"),
	
	/**
	 * Channel object name.
	 */
	CHANNELS_NAME("name"),
	
	/**
	 * Channel object capacity.
	 */
	CHANNELS_CAPACITY("capacity"),
	
	/**
	 * Array list of location objects.
	 */
	LOCATIONS("locations"),
	
	/**
	 * Location object id.
	 */
	LOCATIONS_ID("id"),
	
	/**
	 * Location object name.
	 */
	LOCATIONS_NAME("name"),
	
	/**
	 * Discord Webhook URLs.
	 */
	DISCORD_WEBHOOKS("discord_webhooks");

	private final String name;

	private ConfigNames(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
