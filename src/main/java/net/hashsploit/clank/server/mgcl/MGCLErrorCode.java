package net.hashsploit.clank.server.mgcl;

public enum MGCLErrorCode {

	/**
	 * Successful response.
	 * 
	 * @return
	 */
	MGCL_SUCCESS(0),

	/**
	 * Connect terminated.
	 */
	MGCL_CONNECTION_ERROR(-1),

	/**
	 * Unable to connect to a target host.
	 */
	MGCL_CONNECTION_FAILED(-2),

	/**
	 * Unable to disconnect from a target host.
	 */
	MGCL_DISCONNECT_FAILED(-3),

	/**
	 * Attempt to use an API call that requires a connection - without a connection.
	 */
	MGCL_NOT_CONNECTED(-4),

	/**
	 * Sending of data failed.
	 */
	MGCL_SEND_FAILED(-5),

	/**
	 * Initialization of the MGCL library failed.
	 */
	MGCL_INITIALIZATION_FAILED(-6),

	/**
	 * Shutdown of the MGCL library failed.
	 */
	MGCL_SHUTDOWN_ERROR(-7),

	/**
	 * A lower level network error occurred.
	 */
	MGCL_NETWORK_ERROR(-8),

	/**
	 * Authentication of the MGCL host failed. This may be due to application ID or
	 * mismatched security keys.
	 */
	MGCL_AUTHENTICATION_FAILED(-9),

	/**
	 * Session begin failed.
	 */
	MGCL_SESSIONBEGIN_FAILED(-10),

	/**
	 * Session end failed.
	 */
	MGCL_SESSIONEND_FAILED(-11),

	/**
	 * General request failed.
	 */
	MGCL_UNSUCCESSFUL(-12),

	/**
	 * An invalid argument was used in a function call.
	 */
	MGCL_INVALID_ARG(-13),

	/**
	 * Unable to access the NAT service or resolve the internal NAT address.
	 */
	MGCL_NATRESOLVE_FAILED(-14),

	/**
	 * A game with the same name already exists.
	 */
	MGCL_GAME_NAME_EXISTS(-15),

	/**
	 * The specified world ID is already in use.
	 */
	MGCL_WORLDID_INUSE(-16),

	/**
	 * A lower level DME error has occurred.
	 */
	MGCL_DME_ERROR(-17),

	/**
	 * An attempt was made to re-initialize MGCL without first closing the
	 * subsystem.
	 */
	MGCL_CALL_MGCL_CLOSE_BEFORE_REINITIALIZING(-18),

	/**
	 * The maximum number of games within a lobby world was exceeded.
	 */
	MGCL_NUM_GAME_WORLDS_PER_LOBBY_WORLD_EXCEEDED(-19);

	private final byte value;

	private MGCLErrorCode(int value) {
		this.value = (byte) value;
	}

	public final byte getValue() {
		return value;
	}
}
