package net.hashsploit.clank.server.medius;

public enum MediusConstants {

	/**
	 * Maximum number of bytes used to represent an account name, including the null
	 * termination.
	 */
	ACCOUNTNAME_MAXLEN(32),

	/**
	 * The maximum number of bytes in a world name, including the null termination.
	 */
	WORLDNAME_MAXLEN(64),

	/**
	 * The maximum number of bytes for the password for a world, including the null
	 * termination.
	 */
	WORLDPASSWORD_MAXLEN(32),

	/**
	 * The maximum number of bytes used to represent the world stats. This is a
	 * binary field and does not contain a default value.
	 */
	WORLDSTATS_MAXLEN(256),

	/**
	 * The maximum number of bytes for the server's IP address string, including
	 * null termination.
	 */
	SERVERIP_MAXLEN(20),

	/**
	 * Maximum number of bytes in a string used to denote the user's IP address,
	 * including null termination.
	 */
	IP_MAXLEN(20),

	/**
	 * The maximum number of bytes needed to represent a port (as in IP address and
	 * port), including null termination.
	 */
	SERVERPORT_MAXLEN(8),

	/**
	 * The maximum number of bytes needed to represent the server version, including
	 * null termination.
	 */
	SERVERVERSION_MAXLEN(16),

	/**
	 * Maximum number of bytes a string representation of the account ID can occupy,
	 * including NULL termination.
	 */
	ACCOUNTID_MAXLEN(32),

	/**
	 * The maximum number of bytes that a password may contain, including null
	 * termination.
	 */
	PASSWORD_MAXLEN(32),

	/**
	 * The maximum number of bytes that a player name may use, including null
	 * termination.
	 */
	PLAYERNAME_MAXLEN(32),

	/**
	 * The maximum number of bytes for the user's first (given) name, stored as part
	 * of the user's profile, including null termination.
	 * 
	 * @deprecated
	 */
	FIRSTNAME_MAXLEN(32),

	/**
	 * The maximum number of bytes for the user's middle name, stored as part of the
	 * user's profile, including null termination.
	 * 
	 * @deprecated
	 */
	MIDDLENAME_MAXLEN(32),

	/**
	 * The maximum number of bytes for the user's last name field, stored as part of
	 * the user's profile, including null termination.
	 * 
	 * @deprecated
	 */
	LASTNAME_MAXLEN(32),

	/**
	 * The maximum number of bytes for an address field, stored as part of the
	 * user's profile, including null termination.
	 * 
	 * @deprecated
	 */
	ADDRESS_MAXLEN(32),

	/**
	 * The maximum number of bytes for the email address field, stored as part of
	 * the user's profile, including null termination.
	 * 
	 * @deprecated
	 */
	EMAILADDRESS_MAXLEN(80),

	/**
	 * The maximum number of bytes for the city field, stored as part of the user's
	 * profile, including null termination.
	 * 
	 * @deprecated
	 */
	CITY_MAXLEN(32),

	/**
	 * The maximum number of bytes for the state field, stored as part of the user's
	 * profile, including the null termination.
	 * 
	 * @deprecated
	 */
	STATE_MAXLEN(3),

	/**
	 * The maximum number of bytes for the province field, stored as part of the
	 * user's profile, including null termination.
	 * 
	 * @deprecated
	 */
	PROVINCE_MAXLEN(32),

	/**
	 * The maximum number of bytes for the postal code field, stored as part of the
	 * user's profile, including null termination.
	 * 
	 * @deprecated
	 */
	POSTALCODE_MAXLEN(16),

	/**
	 * The maximum number of bytes for the country field, stored as part of the
	 * user's profile, including null termination.
	 * 
	 * @deprecated
	 */
	COUNTRY_MAXLEN(32),

	/**
	 * The account stats field contains up to this many bytes of binary data.
	 */
	ACCOUNTSTATS_MAXLEN(256),

	/**
	 * Maximum number of bytes in a game name, including the null termination.
	 */
	GAMENAME_MAXLEN(WORLDNAME_MAXLEN.value),

	/**
	 * Maximum number of bytes in the lobby name, including null termination.
	 */
	LOBBYNAME_MAXLEN(WORLDNAME_MAXLEN.value),

	/**
	 * The maxmimum number of bytes for a game password, including null termination.
	 * The game password is a string encoded in either ISO-8859-1 or UTF-8.
	 */
	GAMEPASSWORD_MAXLEN(WORLDPASSWORD_MAXLEN.value),

	/**
	 * Maximum number of bytes for the lobby world password, including null
	 * termination.
	 */
	LOBBYPASSWORD_MAXLEN(WORLDPASSWORD_MAXLEN.value),

	/**
	 * Maximum number of bytes for the game stats. This is a binary field of fixed
	 * length, and no default value.
	 */
	GAMESTATS_MAXLEN(WORLDSTATS_MAXLEN.value),

	/**
	 * The maximum number of bytes for the winning team field in an end-game report,
	 * including null termination.
	 */
	WINNINGTEAM_MAXLEN(64),

	/**
	 * The maximum number of bytes that an application can use to describe itself,
	 * including the null termination.
	 */
	APPNAME_MAXLEN(32),

	/**
	 * Maximum number of bytes in a chat message, including the null termination.
	 */
	CHATMESSAGE_MAXLEN(64),

	/**
	 * Maximum number of bytes in the payload of a Medius binary message.
	 */
	BINARYMESSAGE_MAXLEN(400),

	/**
	 * The maximum number of bytes in a single chunk of the policy, including null
	 * termination.
	 */
	POLICY_MAXLEN(256),

	/**
	 * The maximum number of bytes in a single chunk of a response to a news
	 * request, including null termination.
	 */
	NEWS_MAXLEN(256),

	/**
	 * Maximum number of bytes in a MediusErrorMessage from the server to the
	 * client, including null termination.
	 */
	ERRORMSG_MAXLEN(256),

	/**
	 * Internal definition. Will be removed in a future release of the API.
	 */
	MAX_WORLDS_PER_SERVER(1000),

	/**
	 * The maximum number of bytes in a single announcement text chunk, as returned
	 * by the server, including null termination.
	 */
	ANNOUNCEMENT_MAXLEN(1000),

	/**
	 * Maximum number of bytes used to store a comma delimited list of accounts on
	 * the memory card. Do not use.
	 * 
	 * @deprecated
	 */
	ACCOUNTLIST_MAXLEN(256),

	/**
	 * The maximum number of bytes needed to represent the session key, including
	 * null termination.
	 */
	SESSIONKEY_MAXLEN(17),

	/**
	 * Maximum number of bytes to use for the message ID. Must be a null terminated
	 * string.
	 */
	MESSAGEID_MAXLEN(21),

	/**
	 * Maximum number of bytes used to denote the path to the medius.ico file
	 * location on the memory card.
	 * 
	 * @deprecated
	 */
	ICONLOCATION_MAXLEN(64),

	/**
	 * This denotes the minimum value of a valid world id. Specifically, zero is not
	 * a valid world id.
	 */
	MEDIUS_BASE_WORLDID(1),

	/**
	 * Maximum number of bytes in the access key field.
	 */
	ACCESSKEY_MAXLEN(17),

	/**
	 * The maximum number of bytes for a version string, including null termination.
	 */
	VERSIONSTRING_MAXLEN(56),

	/**
	 * Maximum number of bytes for a location description, including the null
	 * termination.
	 */
	LOCATIONNAME_MAXLEN(64),

	/**
	 * The maximum number of bytes for a user name, including null termination.
	 */
	USERNAME_MAXLEN(32),

	/**
	 * Internal definition used to denote the maximum size of an escaped account
	 * stats field, including null termination. The end user should never need to
	 * reference this value.
	 */
	ESC_ACCOUNTSTATS_MAXLEN((ACCOUNTSTATS_MAXLEN.value * 3) + 1),

	/**
	 * Maximum number of bytes in a DNAS signature post. All binary data.
	 */
	DNASSIGNATURE_MAXLEN(32),

	/**
	 * The maximum number of bytes used to describe a unique billing token
	 * associated with the players current session.
	 */
	BILLINGTOKEN_MAXLEN(20),

	/**
	 * The maximum number of bytes for the title name, including null termination.
	 */
	TITLENAME_MAXLEN(64),

	/**
	 * The maximum number of bytes for a universe name, including null termination.
	 */
	UNIVERSENAME_MAXLEN(128),

	/**
	 * The maximum number of bytes for the DNS name for the entry point
	 * (authentication server) for a given universe, including null termination.
	 */
	UNIVERSEDNS_MAXLEN(128),

	/**
	 * The maximum number of bytes for the universe description from the MUIS,
	 * including the null termination.
	 */
	UNIVERSEDESCRIPTION_MAXLEN(256),

	/**
	 * The maximum number of bytes needed to represent the Billing Service Provider
	 * (BSP) name, including the null termination.
	 * 
	 * @param SCEA or SCEE or SCEK
	 */
	UNIVERSE_BSP_MAXLEN(8),

	/**
	 * The maximum number of bytes for the full name of the Billing Service Provider
	 * (BSP), incluing the null termination.
	 * 
	 * @param Sony Computer Entertainment America, Inc. Billing System
	 */
	UNIVERSE_BSP_NAME_MAXLEN(128),

	/**
	 * Maximum number of bytes for the extended information field in the universe
	 * information, including null termination. Have to allow for up to 3 bytes per
	 * character in the case of UTF-8.
	 * 
	 * The contents of the field is up to the title developer.
	 * 
	 * Used on the MUIS.
	 */
	UNIVERSE_EXTENDED_INFO_MAXLEN(128),

	/**
	 * The maximum number of bytes for the entry point for SVO for the given
	 * universe, including null termination.
	 */
	UNIVERSE_SVO_URL_MAXLEN(128),

	/**
	 * The maximum number of bytes contained in the policy (as a whole) after all of
	 * the pieces have been assembled, including null termination.
	 * 
	 * The policy is encoded in ISO-8859-1 or UTF-8. One byte does not equal one
	 * character in a UTF-8, and care should be taken to ensure that the policy does
	 * not truncate in the middle of a multi-byte character.
	 */
	FULLPOLICY_MAXLEN(10000),

	/**
	 * Length of array storing stats to be used for calculating stats.
	 * 
	 * @deprecated
	 */
	LADDERSTATS_MAXLEN(15),

	/**
	 * This field is fixed at one hundred ladder stats. Each player has at most this
	 * many stats in which they can be tracked. Each stat is a signed integer value.
	 * Each user has an absolute ranking for the field (1st, 2nd, 1500th, etc.)
	 * 
	 * Stats fields must be densely populated from the first entry. Sparsely
	 * populating the table is not allowed. If three entries are used, then these
	 * must be the first three, and not 15, 46, and 79.
	 */
	LADDERSTATSWIDE_MAXLEN(100),

	/**
	 * Maximum number of bytes used to describe when a user is banned until,
	 * including the null termination.
	 */
	BANDATETIME_MAXLEN(32),

	/**
	 * Used for MediusPostDebugInfo. Maximum number of bytes in the
	 * MediusPostDebugInfoRequest, including the null termination.
	 */
	DEBUGMESSAGE_MAXLEN(200),

	/**
	 * Related to a deprecated API for ladder ranking requests applied to a list 
 * of account ID's with this maximum size.
	 * @deprecated
	 */
	ID_ARRAY_MAXLEN(50),
	
	/**
	 * Internal definition for the maximum message size in bytes.
	 */
	MEDIUS_MESSAGE_MAXLEN(512),
	
	
	// CLAN SIZE CONSTANTS
	
	/**
	 * Maximum number of bytes in a clan name, including the null termination.
	 */
	CLANNAME_MAXLEN(32),
	
	/**
	 * Maximum number of byte in a clan stats field.  This is a fixed length field, 
 * and is binary. 
 * 
 * There are no default values for the field.  Please set the clan stats when 
 * creating the clan.
	 */
	CLANSTATS_MAXLEN(256),
	
	/**
	 * Internal definition used to denote the maximum size of an escaped clan stats field, 
 * including null termination.  The end user should never need to reference this value. 
	 */
	ESC_CLANSTATS_MAXLEN((CLANSTATS_MAXLEN.value * 3) + 1),
	
	/**
	 * MediusAddPlayerToClanRequest currently uses CLANMSG_MAXLEN as the define. The size is the same (200 bytes), and the structure will be migrated to use this new definition in a future release.
	 */
	CLANWELCOMEMSG_MAXLEN(200),
	
	/**
	 * Maximum number of bytes for a clan invitation message, including the null termination.
	 */
	CLANINVITEMSG_MAXLEN(200),
	
	/**
	 * MediusCheckMyClanInvitationsResponse currently uses CLANMSG_MAXLEN as the define.  
 * The size is the same (200 bytes), and the structure will be migrated to use this 
 * new definition in a future release.
	 */
	CLANINVITERESPONSEMSG_MAXLEN(200),
	
	/**
	 * Maximum number of bytes in a clan challenge message.
	 * 
	 * @deprecated
	 */
	CLANCHALLENGEMSG_MAXLEN(200),
	
	/**
	 * Maximum number of bytes for clan-related text, including the null termination.
	 */
	CLANMSG_MAXLEN(200),
	
	/**
	 * Number of bytes allowed for MediusToken. The value is binary and there is no default.
	 */
	MEDIUS_TOKEN_MAXSIZE(8),
	
	/**
	 * Number of bits in an atomic bitfield element.
	 */
	MEDIUS_BITFIELD_ELEMENT_SIZE(8),

	/**
	 * Number of bits for the generic chat filter.
	 */
	MEDIUS_GENERIC_CHAT_FILTER_BITFIELD_LEN(128),

	/**
	 * Number of bytes needed to support the medius chat filter.
	 */
	MEDIUS_GENERIC_CHAT_FILTER_BYTES_LEN((MEDIUS_GENERIC_CHAT_FILTER_BITFIELD_LEN.value + 7) / 8);
	
	private final int value;

	private MediusConstants(int value) {
		this.value = (short) value;
	}

	public final int getValue() {
		return value;
	}
}
