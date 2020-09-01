package net.hashsploit.clank.server.medius;

public enum MediusCallbackStatus {

	/**
	 * Session begin failed.
	 */
	MediusBeginSessionFailed(-1000),

	/**
	 * Account already exists, can not register with the same account name.
	 */
	MediusAccountAlreadyExists(-999),

	/**
	 * Account name was not found.
	 */
	MediusAccountNotFound(-998),

	/**
	 * The account is marked as already being logged in to the system.
	 */
	MediusAccountLoggedIn(-997),

	/**
	 * Unable to properly end the session.
	 */
	MediusEndSessionFailed(-996),
	
	/**
	 * Login failed.
	 */
	MediusLoginFailed(-995),
	
	/**
	 * Registration failed.
	 */
	MediusRegistrationFailed(-994),
	
	/**
	 * The login step was incorrect. For example, login without having a session.
	 */
	MediusIncorrectLoginStep(-993),
	
	/**
	 * The user is already the leader of a clan, and can not be the leader of multiple clans.
	 */
	MediusAlreadyLeaderOfClan(-992),
	
	/**
	 * World Manager error.
	 */
	MediusWMError(-991),
	
	/**
	 * The player attempted some request that requires being the leader of the clan.
	 */
	MediusNotClanLeader(-990),
	
	/**
	 * The player is not privileged to make the request. Typically, the user's session has been destroyed, but is still connected to the server.
	 */
	MediusPlayerNotPrivileged(-989),
	
	/**
	 * An internal database error occurred.
	 */
	MediusDBError(-988),
	
	/**
	 * A DME layer error.
	 */
	MediusDMEError(-987),
	
	/**
	 * The maximum number of worlds has been exceeded.
	 */
	MediusExceedsMaxWorlds(-986),
	
	/**
	 * The request has been denied.
	 */
	MediusRequestDenied(-985),
	
	/**
	 * Setting the game list filter failed.
	 */
	MediusSetGameListFilterFailed(-984),
	
	/**
	 * Clearing the game list filter failed.
	 */
	MediusClearGameListFilterFailed(-983),
	
	/**
	 * Getting the game list filter failed.
	 */
	MediusGetGameListFilterFailed(-982),
	
	/**
	 * The number of filters is at the maximum.
	 */
	MediusNumFiltersAtMax(-981),
	
	/**
	 * The filter being referenced does not exist.
	 */
	MediusFilterNotFound(-980),
	
	/**
	 * The request message was invalid.
	 */
	MediusInvalidRequestMsg(-979),
	
	/**
	 * The specified password was invalid.
	 */
	MediusInvalidPassword(-978),
	
	/**
	 * The game was not found.
	 */
	MediusGameNotFound(-977),
	
	/**
	 * The channel was not found.
	 */
	MediusChannelNotFound(-976),
	
	/**
	 * The game name already exists.
	 */
	MediusGameNameExists(-975),
	
	/**
	 * The channel name already exists.
	 */
	MediusChannelNameExists(-974),
	
	/**
	 * The game name was not found.
	 */
	MediusGameNameNotFound(-973),
	
	/**
	 * The player has been banned.
	 */
	MediusPlayerBanned(-972),
	
	/**
	 * The clan was not found.
	 */
	MediusClanNotFound(-971),
	
	/**
	 * The clan name already exists.
	 */
	MediusClanNameInUse(-970),
	
	/**
	 * Session key is invalid.
	 */
	MediusSessionKeyInvalid(-969),
	
	/**
	 * The text string is invalid.
	 */
	MediusTextStringInvalid(-968),
	
	/**
	 * The filtering failed.
	 */
	MediusFilterFailed(-967),
	
	/**
	 * General fail message.
	 */
	MediusFail(-966),
	
	/**
	 * Medius File Services (MFS) Internal error.
	 */
	MediusFileInternalAccessError(-965),
	
	/**
	 * Insufficient permissions for the  MFS request.
	 */
	MediusFileNoPermissions(-964),
	
	/**
	 * The file requested in MFS does not exist.
	 */
	MediusFileDoesNotExist(-963),
	
	/**
	 * The file requested in MFS already exists.
	 */
	MediusFileAlreadyExists(-962),
	
	/**
	 * The filename is not valid in MFS.
	 */
	MediusFileInvalidFilename(-961),
	
	/**
	 * The user's quota has been exceeded.
	 */
	MediusFileQuotaExceeded(-960),
	
	/**
	 * The cache system had an internal failure.
	 */
	MediusCacheFailure(-959),
	
	/**
	 * The data already exists.
	 */
	MediusDataAlreadyExists(-958),
	
	/**
	 * The data does not exist.
	 */
	MediusDataDoesNotExist(-957),
	
	/**
	 * A maximum count has been exceeded.
	 */
	MediusMaxExceeded(-956),
	
	/**
	 * The key used is incorrect.
	 */
	MediusKeyError(-955),
	
	/**
	 * The application ID is not compatible.
	 */
	MediusIncompatibleAppID(-954),
	
	/**
	 * The account has been banned.
	 */
	MediusAccountBanned(-953),
	
	/**
	 * The machine has been banned.
	 */
	MediusMachineBanned(-952),
	
	/**
	 * The leader of the clan can not leave.  Must disband instead.
	 */
	MediusLeaderCannotLeaveClan(-951),
	
	/**
	 * The feature requested is not enabled.
	 */
	MediusFeatureNotEnabled(-950),
	
	/**
	 * The same DNAS signature is already logged in.
	 */
	MediusDNASSignatureLoggedIn(-949),
	
	/**
	 * The world is full. Unable to join.
	 */
	MediusWorldIsFull(-948),
	
	/**
	 * The user is not a member of the clan.
	 */
	MediusNotClanMember(-947),
	
	/**
	 * The server is busy. Try again later.
	 */
	MediusServerBusy(-946),
	
	/**
	 * The maximum number of game worlds per lobby world has been exceeded.
	 */
	MediusNumGameWorldsPerLobbyWorldExceeded(-945),
	
	/**
	 * The account name is not UC compliant.
	 */
	MediusAccountNotUCCompliant(-944),
	
	/**
	 * The password is not UC compliant.
	 */
	MediusPasswordNotUCCompliant(-943),
	
	/**
	 * There is an internal gateway error.
	 */
	MediusGatewayError(-942),
	
	/**
	 * The transaction has been cancelled.
	 */
	MediusTransactionCanceled(-941),
	
	/**
	 * The session has failed.
	 */
	MediusSessionFail(-940),
	
	/**
	 * The token is already in use.
	 */
	MediusTokenAlreadyTaken(-939),
	
	/**
	 * The token being referenced does not exist.
	 */
	MediusTokenDoesNotExist(-938),
	
	/**
	 * The subscription has been aborted.
	 */
	MediusSubscriptionAborted(-937),
	
	/**
	 * The subscription is invalid.
	 */
	MediusSubscriptionInvalid(-936),
	
	/**
	 * The user is not a member of an list.
	 */
	MediusNotAMember(-935),
	
	/**
	 * Success.
	 */
	MediusSuccess(0),
	
	/**
	 * No results. This is a valid state.
	 */
	MediusNoResult(1),
	
	/**
	 * The request has been accepted.
	 */
	MediusRequestAccepted(2),
	
	/**
	 * The world has been created with reduced size.
	 */
	MediusWorldCreatedSizeReduced(3),
	
	/**
	 * The criteria has been met.
	 */
	MediusPass(4);
	
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
