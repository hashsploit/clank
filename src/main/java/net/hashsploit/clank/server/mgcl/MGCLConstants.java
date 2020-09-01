package net.hashsploit.clank.server.mgcl;

public enum MGCLConstants {

	/**
	 * Current MGCL major version number.
	 */
	MGCL_VERSION_MAJOR(2),

	/**
	 * Current MGCL minor version number.
	 */
	MGCL_VERSION_MINOR(10),

	/**
	 * Current MGCL version build number.
	 */
	MGCL_VERSION_BUILD(6),

	/**
	 * This is the maximum number of clients per game world supported by this MGCL
	 * server.
	 */
	MAX_CLIENTS_PER_WORLD(256),

	/**
	 * Maximum number of bytes used to represent the game server's access key.
	 */
	MGCL_ACCESSKEY_MAXLEN(17),

	/**
	 * The maximum number of bytes used to represent the game servers IP address.
	 */
	MGCL_SERVERIP_MAXLEN(20),

	/**
	 * Maximum number of bytes in the version string literal of MGCL.
	 */
	MGCL_SERVERVERSION_MAXLEN(16),

	/**
	 * Maximum number of bytes in the server's port number string, including the
	 * NULL termination.
	 */
	MGCL_SERVERPORT_MAXLEN(8),

	/**
	 * Maximum number of bytes in the session key field, including the NULL
	 * termination.
	 */
	MGCL_SESSIONKEY_MAXLEN(17),

	/**
	 * Maximum number of bytes for the message ID field, including the NULL
	 * termination. This value is used for all MessageID fields in request
	 * structures, and can be used to match up an asynchronous request with the
	 * response value.
	 */
	MGCL_MESSAGEID_MAXLEN(21),

	/**
	 * Maximum number of bytes used for a game name, including the NULL termination.
	 */
	MGCL_GAMENAME_MAXLEN(64),
	
	/**
	 * Maximum number of bytes in the game-stats string, including the NULL termination.
	 */
	MGCL_GAMESTATS_MAXLEN(256),
	
	/**
	 * Maximum number of bytes that a game password can contain, including the NULL termination.
	 */
	MGCL_GAMEPASSWORD_MAXLEN(32),
	
	/**
	 * Maximum number of bytes used to store the MGCL version string literal, including NULL termination.
	 */
	MEDIUS_GAME_COMM_LIBRARY_VERSION_NUMBER_MAXLEN(64);
	
	
	private final short value;

	private MGCLConstants(int value) {
		this.value = (short) value;
	}

	public final short getValue() {
		return value;
	}
}
