package net.hashsploit.clank.utils;

import java.util.HashMap;

import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusAccountGetIdHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusAccountLoginHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusAccountLogoutHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusAccountRegistrationHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusAccountUpdateStatsHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusAddToBuddyListHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusChannelInfoHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusChannelListHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusChannelList_ExtraInfoOneHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusChatMessageHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusChatToggleHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusCheckMyClanInvitationsHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusClearGameListFilterZeroHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusCreateGameOneHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusDnasSignaturePostHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusEndGameReportHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusFindWorldByNameHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGameInfoZeroHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGameListHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGameList_ExtraInfoZeroHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGameWorldPlayerListHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetAllAnnouncementsHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetAnnouncementsHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetBuddyInvitationsHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetBuddyList_ExtraInfoHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetClanInvitationsSentHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetClanMemberList_ExtraInfoHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetIgnoreListHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetLadderStatsWideHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetLobbyPlayerNames_ExtraInfoHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetLocationsHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetMyClanMessagesHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetMyClansHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetMyIPHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusGetWorldSecurityLevelHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusJoinChannelHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusJoinGameHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusLadderPositionHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusLobbyWorldPlayerListHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusPickLocationHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusPlayerInfoHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusPlayerReportHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusPolicyHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusServerAuthenticationHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusServerSessionBeginHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusSessionBeginHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusSessionEndHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusSetGameListFilterZeroHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusSetLobbyWorldFilterHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusSetLocalizationParamsHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusTextFilterHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusUpdateLadderStatsWideHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusUpdateUserStateHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusVersionServerHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusWorldReportZeroHandler;

public class MediusMessageMapInitializer {
	
	// Prevent instantiation
	private MediusMessageMapInitializer() {}
	
	public static final HashMap<MediusMessageType, MediusPacketHandler> getMlsMap() {
		HashMap<MediusMessageType, MediusPacketHandler> mp = new HashMap<MediusMessageType, MediusPacketHandler>();
		
		mp.put(MediusMessageType.AccountLogout, new MediusAccountLogoutHandler());
		mp.put(MediusMessageType.SessionEnd, new MediusSessionEndHandler());
		
		mp.put(MediusMessageType.UpdateUserState, new MediusUpdateUserStateHandler()); // WORKING
		mp.put(MediusMessageType.GetMyClans, new MediusGetMyClansHandler()); // WORKING
		mp.put(MediusMessageType.ChatToggle, new MediusChatToggleHandler()); // WORKING
		mp.put(MediusMessageType.GetLadderStatsWide, new MediusGetLadderStatsWideHandler());
		mp.put(MediusMessageType.AccountUpdateStats, new MediusAccountUpdateStatsHandler());
		mp.put(MediusMessageType.UpdateLadderStatsWide, new MediusUpdateLadderStatsWideHandler());
		mp.put(MediusMessageType.PlayerInfo, new MediusPlayerInfoHandler());
		mp.put(MediusMessageType.GetLocations, new MediusGetLocationsHandler()); // WORKING
		mp.put(MediusMessageType.Policy, new MediusPolicyHandler()); // WORKING
		mp.put(MediusMessageType.GetAllAnnouncements, new MediusGetAllAnnouncementsHandler()); // WORKING
		mp.put(MediusMessageType.GetBuddyList_ExtraInfo, new MediusGetBuddyList_ExtraInfoHandler()); 
		mp.put(MediusMessageType.GetMyClanMessages, new MediusGetMyClanMessagesHandler()); // WORKING
		mp.put(MediusMessageType.ClearGameListFilter0, new MediusClearGameListFilterZeroHandler()); // not sure if working...
		mp.put(MediusMessageType.SetGameListFilter0, new MediusSetGameListFilterZeroHandler()); // not sure if working...
		mp.put(MediusMessageType.SetLobbyWorldFilter, new MediusSetLobbyWorldFilterHandler()); 
		
		mp.put(MediusMessageType.GameList_ExtraInfo0, new MediusGameList_ExtraInfoZeroHandler()); // not working
		
		mp.put(MediusMessageType.ChannelList_ExtraInfo1, new MediusChannelList_ExtraInfoOneHandler());  // hard coded

		mp.put(MediusMessageType.JoinChannel, new MediusJoinChannelHandler());  // vvvv
		mp.put(MediusMessageType.ChannelInfo, new MediusChannelInfoHandler());  // vvvv
		mp.put(MediusMessageType.GetLobbyPlayerNames_ExtraInfo, new MediusGetLobbyPlayerNames_ExtraInfoHandler());  // vvvv
	
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
		mp.put(MediusMessageType.TextFilter,  new MediusTextFilterHandler());
		mp.put(MediusMessageType.ChatMessage, new MediusChatMessageHandler());
		
		// Inspecting player profile
		mp.put(MediusMessageType.GetIgnoreList, new MediusGetIgnoreListHandler());
		mp.put(MediusMessageType.GetClanInvitationsSent, new MediusGetClanInvitationsSentHandler());
		mp.put(MediusMessageType.GetClanMemberList_ExtraInfo, new MediusGetClanMemberList_ExtraInfoHandler());

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
		
		return mp;
	}

}
