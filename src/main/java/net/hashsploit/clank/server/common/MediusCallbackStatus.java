package net.hashsploit.clank.server.common;

public enum MediusCallbackStatus {

	/**
	 * Session begin failed.
	 */
	BEGIN_SESSION_FAILED(-1000),

	/**
	 * Account already exists, can not register with the same account name.
	 */
	ACCOUNT_ALREADY_EXISTS(-999),

	/**
	 * Account name was not found.
	 */
	ACCOUNT_NOT_FOUND(-998),

	/**
	 * The account is marked as already being logged in to the system.
	 */
	ACCOUNT_LOGGED_IN(-997),

	/**
	 * Unable to properly end the session.
	 */
	END_SESSION_FAILED(-996),
	
	/**
	 * Login failed.
	 */
	LOGIN_FAILED(-995),
	
	/**
	 * Registration failed.
	 */
	REGISTRATION_FAILED(-994),
	
	/**
	 * The login step was incorrect. For example, login without having a session.
	 */
	INCORRECT_LOGIN_STEP(-993),
	
	/**
	 * The user is already the leader of a clan, and can not be the leader of multiple clans.
	 */
	ALREADY_LEADER_OF_CLAN(-992),
	
	/**
	 * World Manager error.
	 */
	WORLD_MANAGER_ERROR(-991),
	
	/**
	 * The player attempted some request that requires being the leader of the clan.
	 */
	NOT_CLAN_LEADER(-990),
	
	/**
	 * The player is not privileged to make the request. Typically, the user's session has been destroyed, but is still connected to the server.
	 */
	PLAYER_NOT_PRIVILEGED(-989),
	
	/**
	 * An internal database error occurred.
	 */
	DATABASE_ERROR(-988),
	
	/**
	 * A DME layer error.
	 */
	DME_ERROR(-987),
	
	/**
	 * The maximum number of worlds has been exceeded.
	 */
	EXCEEDS_MAX_WORLDS(-986),
	
	/**
	 * The request has been denied.
	 */
	REQUEST_DENIED(-985),
	
	/**
	 * Setting the game list filter failed.
	 */
	SET_GAME_FILTER_FAILED(-984),
	
	/**
	 * Clearing the game list filter failed.
	 */
	CLEAR_GAME_LIST_FILTER_FAILED(-983),
	
	/**
	 * Getting the game list filter failed.
	 */
	GET_GAME_LIST_FILTER_FAILED(-982),
	
	/**
	 * The number of filters is at the maximum.
	 */
	NUM_FILTERS_AT_MAX(-981),
	
	/**
	 * The filter being referenced does not exist.
	 */
	FILTER_NOT_FOUND(-980),
	
	/**
	 * The request message was invalid.
	 */
	INVALID_REQUEST_MESSAGE(-979),
	
	/**
	 * The specified password was invalid.
	 */
	INVALID_PASSWORD(-978),
	
	/**
	 * The game was not found.
	 */
	GAME_NOT_FOUND(-977),
	
	/**
	 * The channel was not found.
	 */
	CHANNEL_NOT_FOUND(-976),
	
	/**
	 * The game name already exists.
	 */
	GAME_NAME_EXISTS(-975),
	
	/**
	 * The channel name already exists.
	 */
	CHANNEL_NAME_EXISTS(-974),
	
	/**
	 * The game name was not found.
	 */
	GAME_NAME_NOT_FOUND(-973),
	
	/**
	 * The player has been banned.
	 */
	PLAYER_BANNED(-972),
	
	/**
	 * The clan was not found.
	 */
	CLAN_NOT_FOUND(-971),
	
	/**
	 * The clan name already exists.
	 */
	CLAN_NAME_IN_USE(-970),
	
	/**
	 * Session key is invalid.
	 */
	SESSION_KEY_INVALID(-969),
	
	/**
	 * The text string is invalid.
	 */
	TEXT_STRING_INVALID(-968),
	
	/**
	 * The filtering failed.
	 */
	FILTER_FAILED(-967),
	
	/**
	 * General fail message.
	 */
	FAILURE(-966),
	
	/**
	 * Medius File Services (MFS) Internal error.
	 */
	FILE_INTERNAL_ACCESS_ERROR(-965),
	
	/**
	 * Insufficient permissions for the MFS request.
	 */
	FILE_NO_PERMISSIONS(-964),
	
	/**
	 * The file requested in MFS does not exist.
	 */
	FILE_DOES_NOT_EXIST(-963),
	
	/**
	 * The file requested in MFS already exists.
	 */
	FILE_ALREADY_EXISTS(-962),
	
	/**
	 * The filename is not valid in MFS.
	 */
	FILE_INVALID_FILENAME(-961),
	
	/**
	 * The user's quota has been exceeded.
	 */
	FILE_QUOTA_EXCEEDED(-960),
	
	/**
	 * The cache system had an internal failure.
	 */
	CACHE_FAILURE(-959),
	
	/**
	 * The data already exists.
	 */
	DATA_ALREADY_EXISTS(-958),
	
	/**
	 * The data does not exist.
	 */
	DATA_DOES_NOT_EXIST(-957),
	
	/**
	 * A maximum count has been exceeded.
	 */
	MAX_EXCEEDED(-956),
	
	/**
	 * The key used is incorrect.
	 */
	KEY_ERROR(-955),
	
	/**
	 * The application ID is not compatible.
	 */
	INCOMPATIBLE_APP_ID(-954),
	
	/**
	 * The account has been banned.
	 */
	ACCOUNT_BANNED(-953),
	
	/**
	 * The machine has been banned.
	 */
	MACHINE_BANNED(-952),
	
	/**
	 * The leader of the clan can not leave. Must disband instead.
	 */
	LEADER_CANNOT_LEAVE_CLAN(-951),
	
	/**
	 * The feature requested is not enabled.
	 */
	FEATURE_NOT_ENABLED(-950),
	
	/**
	 * The same DNAS signature is already logged in.
	 */
	DNAS_SIGNATURE_LOGGED_IN(-949),
	
	/**
	 * The world is full. Unable to join.
	 */
	WORLD_IS_FULL(-948),
	
	/**
	 * The user is not a member of the clan.
	 */
	NOT_CLAN_MEMBER(-947),
	
	/**
	 * The server is busy. Try again later.
	 */
	SERVER_BUSY(-946),
	
	/**
	 * The maximum number of game worlds per lobby world has been exceeded.
	 */
	NUM_GAME_WORLDS_PER_LOBBY_EXCEEDED(-945),
	
	/**
	 * The account name is not UC compliant.
	 */
	ACCOUNT_NOT_UC_COMPLIANT(-944),
	
	/**
	 * The password is not UC compliant.
	 */
	PASSWORD_NOT_UC_COMPLIANT(-943),
	
	/**
	 * There is an internal gateway error.
	 */
	GATEWAY_ERROR(-942),
	
	/**
	 * The transaction has been cancelled.
	 */
	TRANSACTION_CANCELLED(-941),
	
	/**
	 * The session has failed.
	 */
	SESSION_FAILURE(-940),
	
	/**
	 * The token is already in use.
	 */
	TOKEN_ALREADY_TAKEN(-939),
	
	/**
	 * The token being referenced does not exist.
	 */
	TOKEN_DOES_NOT_EXIST(-938),
	
	/**
	 * The subscription has been aborted.
	 */
	SUBSCRIPTION_ABORTED(-937),
	
	/**
	 * The subscription is invalid.
	 */
	SUBSCRIPTION_INVALID(-936),
	
	/**
	 * The user is not a member of an list.
	 */
	NOT_A_MEMBER(-935),
	
	/**
	 * Success.
	 */
	SUCCESS(0),
	
	/**
	 * No results. This is a valid state.
	 */
	NO_RESULT(1),
	
	/**
	 * The request has been accepted.
	 */
	REQUEST_ACCEPTED(2),
	
	/**
	 * The world has been created with reduced size.
	 */
	WORLD_CREATED_SIZE_REDUCED(3),
	
	/**
	 * The criteria has been met.
	 */
	PASS(4);
	
	private final int value;

	private MediusCallbackStatus(int value) {
		this.value = value;
	}

	/**
	 * Get the value of the id as a int (unsigned short)
	 * 
	 * @return
	 */
	public final int getValue() {
		return value;
	}
}
