package net.hashsploit.clank.utils;

import java.util.HashMap;

import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.handlers.MediusAccountGetIdHandler;
import net.hashsploit.clank.server.medius.handlers.MediusAccountLoginHandler;
import net.hashsploit.clank.server.medius.handlers.MediusAccountLogoutHandler;
import net.hashsploit.clank.server.medius.handlers.MediusAccountRegistrationHandler;
import net.hashsploit.clank.server.medius.handlers.MediusAccountUpdateStatsHandler;
import net.hashsploit.clank.server.medius.handlers.MediusAddToBuddyListHandler;
import net.hashsploit.clank.server.medius.handlers.MediusChannelInfoHandler;
import net.hashsploit.clank.server.medius.handlers.MediusChannelListHandler;
import net.hashsploit.clank.server.medius.handlers.MediusChannelList_ExtraInfoOneHandler;
import net.hashsploit.clank.server.medius.handlers.MediusChatMessageHandler;
import net.hashsploit.clank.server.medius.handlers.MediusChatToggleHandler;
import net.hashsploit.clank.server.medius.handlers.MediusCheckMyClanInvitationsHandler;
import net.hashsploit.clank.server.medius.handlers.MediusClearGameListFilterZeroHandler;
import net.hashsploit.clank.server.medius.handlers.MediusCreateClanHandler;
import net.hashsploit.clank.server.medius.handlers.MediusCreateGameOneHandler;
import net.hashsploit.clank.server.medius.handlers.MediusDnasSignaturePostHandler;
import net.hashsploit.clank.server.medius.handlers.MediusEndGameReportHandler;
import net.hashsploit.clank.server.medius.handlers.MediusFindPlayerHandler;
import net.hashsploit.clank.server.medius.handlers.MediusFindWorldByNameHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGameInfoZeroHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGameListHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGameList_ExtraInfoZeroHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGameWorldPlayerListHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetAllAnnouncementsHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetAnnouncementsHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetBuddyInvitationsHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetBuddyList_ExtraInfoHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetClanInvitationsSentHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetClanMemberList_ExtraInfoHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetIgnoreListHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetLadderStatsWideHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetLobbyPlayerNames_ExtraInfoHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetLocationsHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetMyClanMessagesHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetMyClansHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetMyIPHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetUniverseInformationHandler;
import net.hashsploit.clank.server.medius.handlers.MediusGetWorldSecurityLevelHandler;
import net.hashsploit.clank.server.medius.handlers.MediusJoinChannelHandler;
import net.hashsploit.clank.server.medius.handlers.MediusJoinGameHandler;
import net.hashsploit.clank.server.medius.handlers.MediusLadderPositionHandler;
import net.hashsploit.clank.server.medius.handlers.MediusLadderPosition_ExtraInfoHandler;
import net.hashsploit.clank.server.medius.handlers.MediusLobbyWorldPlayerListHandler;
import net.hashsploit.clank.server.medius.handlers.MediusPickLocationHandler;
import net.hashsploit.clank.server.medius.handlers.MediusPlayerInfoHandler;
import net.hashsploit.clank.server.medius.handlers.MediusPlayerReportHandler;
import net.hashsploit.clank.server.medius.handlers.MediusPolicyHandler;
import net.hashsploit.clank.server.medius.handlers.MediusServerAuthenticationHandler;
import net.hashsploit.clank.server.medius.handlers.MediusServerSessionBeginHandler;
import net.hashsploit.clank.server.medius.handlers.MediusSessionBeginHandler;
import net.hashsploit.clank.server.medius.handlers.MediusSessionEndHandler;
import net.hashsploit.clank.server.medius.handlers.MediusSetGameListFilterZeroHandler;
import net.hashsploit.clank.server.medius.handlers.MediusSetLobbyWorldFilterHandler;
import net.hashsploit.clank.server.medius.handlers.MediusSetLocalizationParamsHandler;
import net.hashsploit.clank.server.medius.handlers.MediusTextFilterHandler;
import net.hashsploit.clank.server.medius.handlers.MediusUpdateLadderStatsWideHandler;
import net.hashsploit.clank.server.medius.handlers.MediusUpdateUserStateHandler;
import net.hashsploit.clank.server.medius.handlers.MediusVersionServerHandler;
import net.hashsploit.clank.server.medius.handlers.MediusWorldReportZeroHandler;

public class MediusMessageMapInitializer {

	// Prevent instantiation
	private MediusMessageMapInitializer() {
	}

	public static final HashMap<MediusMessageType, MediusPacketHandler> getMlsMap() {
		HashMap<MediusMessageType, MediusPacketHandler> mp = new HashMap<MediusMessageType, MediusPacketHandler>();

		mp.put(MediusMessageType.AccountLogout, new MediusAccountLogoutHandler());
		mp.put(MediusMessageType.SessionEnd, new MediusSessionEndHandler());

		mp.put(MediusMessageType.UpdateUserState, new MediusUpdateUserStateHandler());
		mp.put(MediusMessageType.GetMyClans, new MediusGetMyClansHandler());
		mp.put(MediusMessageType.ChatToggle, new MediusChatToggleHandler());
		mp.put(MediusMessageType.GetLadderStatsWide, new MediusGetLadderStatsWideHandler());
		mp.put(MediusMessageType.AccountUpdateStats, new MediusAccountUpdateStatsHandler());
		mp.put(MediusMessageType.UpdateLadderStatsWide, new MediusUpdateLadderStatsWideHandler());
		mp.put(MediusMessageType.PlayerInfo, new MediusPlayerInfoHandler());
		mp.put(MediusMessageType.GetLocations, new MediusGetLocationsHandler());
		mp.put(MediusMessageType.Policy, new MediusPolicyHandler());
		mp.put(MediusMessageType.GetAllAnnouncements, new MediusGetAllAnnouncementsHandler());
		mp.put(MediusMessageType.GetBuddyList_ExtraInfo, new MediusGetBuddyList_ExtraInfoHandler());
		mp.put(MediusMessageType.GetMyClanMessages, new MediusGetMyClanMessagesHandler());
		mp.put(MediusMessageType.ClearGameListFilter0, new MediusClearGameListFilterZeroHandler());
		mp.put(MediusMessageType.SetGameListFilter0, new MediusSetGameListFilterZeroHandler());
		mp.put(MediusMessageType.SetLobbyWorldFilter, new MediusSetLobbyWorldFilterHandler());
		mp.put(MediusMessageType.GameList_ExtraInfo0, new MediusGameList_ExtraInfoZeroHandler());
		mp.put(MediusMessageType.ChannelList_ExtraInfo1, new MediusChannelList_ExtraInfoOneHandler());
		mp.put(MediusMessageType.JoinChannel, new MediusJoinChannelHandler());
		mp.put(MediusMessageType.ChannelInfo, new MediusChannelInfoHandler());
		mp.put(MediusMessageType.GetLobbyPlayerNames_ExtraInfo, new MediusGetLobbyPlayerNames_ExtraInfoHandler());

		// Creating games
		mp.put(MediusMessageType.GetWorldSecurityLevel, new MediusGetWorldSecurityLevelHandler());
		mp.put(MediusMessageType.CreateGame1, new MediusCreateGameOneHandler());
		mp.put(MediusMessageType.JoinGame, new MediusJoinGameHandler());
		mp.put(MediusMessageType.PlayerReport, new MediusPlayerReportHandler());
		mp.put(MediusMessageType.WorldReport0, new MediusWorldReportZeroHandler());
		mp.put(MediusMessageType.GameInfo0, new MediusGameInfoZeroHandler());
		mp.put(MediusMessageType.EndGameReport, new MediusEndGameReportHandler());
		mp.put(MediusMessageType.GameWorldPlayerList, new MediusGameWorldPlayerListHandler());

		// Buddies
		mp.put(MediusMessageType.AccountGetId,  new MediusAccountGetIdHandler());
		mp.put(MediusMessageType.AddToBuddyList,  new MediusAddToBuddyListHandler());
		// Chat
		mp.put(MediusMessageType.TextFilter, new MediusTextFilterHandler());
		mp.put(MediusMessageType.ChatMessage, new MediusChatMessageHandler());

		// Inspecting player profile
		mp.put(MediusMessageType.GetIgnoreList, new MediusGetIgnoreListHandler());
		mp.put(MediusMessageType.GetClanInvitationsSent, new MediusGetClanInvitationsSentHandler());
		mp.put(MediusMessageType.GetClanMemberList_ExtraInfo, new MediusGetClanMemberList_ExtraInfoHandler());
		mp.put(MediusMessageType.FindPlayer, new MediusFindPlayerHandler());
		mp.put(MediusMessageType.LadderPosition_ExtraInfo, new MediusLadderPosition_ExtraInfoHandler());

		// added for Amplitude
		mp.put(MediusMessageType.AccountRegistration, new MediusAccountRegistrationHandler());
		mp.put(MediusMessageType.LadderPosition, new MediusLadderPositionHandler());
		mp.put(MediusMessageType.GetAnnouncements, new MediusGetAnnouncementsHandler());
		mp.put(MediusMessageType.GameList, new MediusGameListHandler());
		mp.put(MediusMessageType.LobbyWorldPlayerList, new MediusLobbyWorldPlayerListHandler());
		mp.put(MediusMessageType.ChannelList, new MediusChannelListHandler());

		// added for Syphon Filter
		mp.put(MediusMessageType.GetMyIP, new MediusGetMyIPHandler());
		mp.put(MediusMessageType.VersionServer, new MediusVersionServerHandler());
		mp.put(MediusMessageType.PickLocation, new MediusPickLocationHandler());
		mp.put(MediusMessageType.FindWorldByName, new MediusFindWorldByNameHandler());
		mp.put(MediusMessageType.GetBuddyInvitations, new MediusGetBuddyInvitationsHandler());
		mp.put(MediusMessageType.CheckMyClanInvitations, new MediusCheckMyClanInvitationsHandler());
		
		mp.put(MediusMessageType.CreateClan, new MediusCreateClanHandler());
		

		return mp;
	}

	public static final HashMap<MediusMessageType, MediusPacketHandler> getMasMap() {
		HashMap<MediusMessageType, MediusPacketHandler> mp = new HashMap<MediusMessageType, MediusPacketHandler>();

		// UYA
		mp.put(MediusMessageType.SessionBegin, new MediusSessionBeginHandler());
		mp.put(MediusMessageType.DnasSignaturePost, new MediusDnasSignaturePostHandler());
		mp.put(MediusMessageType.SetLocalizationParams, new MediusSetLocalizationParamsHandler());
		mp.put(MediusMessageType.AccountLogin, new MediusAccountLoginHandler());
		mp.put(MediusMessageType.SetLobbyWorldFilter, new MediusSetLobbyWorldFilterHandler());

		// Other titles
		mp.put(MediusMessageType.GetMyIP, new MediusGetMyIPHandler());
		mp.put(MediusMessageType.GetLocations, new MediusGetLocationsHandler());
		mp.put(MediusMessageType.PickLocation, new MediusPickLocationHandler());
		mp.put(MediusMessageType.GetAnnouncements, new MediusGetAnnouncementsHandler());
		mp.put(MediusMessageType.Policy, new MediusPolicyHandler());
		mp.put(MediusMessageType.MediusServerSessionBeginRequest, new MediusServerSessionBeginHandler());
		mp.put(MediusMessageType.MediusServerAuthenticationRequest, new MediusServerAuthenticationHandler());
		mp.put(MediusMessageType.VersionServer, new MediusVersionServerHandler());
		mp.put(MediusMessageType.SessionEnd, new MediusSessionEndHandler());

		return mp;
	}

	public static final HashMap<MediusMessageType, MediusPacketHandler> getMuisMap() {
		HashMap<MediusMessageType, MediusPacketHandler> mp = new HashMap<MediusMessageType, MediusPacketHandler>();

		// added for Killzone
		mp.put(MediusMessageType.VersionServer, new MediusVersionServerHandler());
		mp.put(MediusMessageType.GetUniverseInformation, new MediusGetUniverseInformationHandler());

		return mp;
	}

}
