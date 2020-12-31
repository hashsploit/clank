package net.hashsploit.clank.utils;

import java.util.HashMap;

import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;

public class MediusMessageMapInitializer {
	
	// Prevent instantiation
	private MediusMessageMapInitializer() {}
	
	public static final HashMap<MediusMessageType, MediusPacketHandler> getMlsMap() {
		HashMap<MediusMessageType, MediusPacketHandler> mp = new HashMap<MediusMessageType, MediusPacketHandler>();
//		
//		mp.put(MediusMessageType.AccountLogout, new MediusAccountLogoutHandler());
//		mp.put(MediusMessageType.SessionEnd, new MediusSessionEndHandler());
//		
//		mp.put(MediusMessageType.UpdateUserState, new MediusUpdateUserStateHandler()); // WORKING
//		mp.put(MediusMessageType.GetMyClans, new MediusGetMyClansHandler()); // WORKING
//		mp.put(MediusMessageType.ChatToggle, new MediusChatToggleHandler()); // WORKING
//		mp.put(MediusMessageType.GetLadderStatsWide, new MediusGetLadderStatsWideHandler());
//		mp.put(MediusMessageType.AccountUpdateStats, new MediusAccountUpdateStatsHandler());
//		mp.put(MediusMessageType.UpdateLadderStatsWide, new MediusUpdateLadderStatsWideHandler());
//		mp.put(MediusMessageType.PlayerInfo, new MediusPlayerInfoHandler());
//		mp.put(MediusMessageType.GetLocations, new MediusGetLocationsHandler()); // WORKING
//		mp.put(MediusMessageType.Policy, new MediusPolicyHandler()); // WORKING
//		mp.put(MediusMessageType.GetAllAnnouncements, new MediusGetAllAnnouncementsHandler()); // WORKING
//		mp.put(MediusMessageType.GetBuddyList_ExtraInfo, new MediusGetBuddyList_ExtraInfoHandler()); 
//		mp.put(MediusMessageType.GetMyClanMessages, new MediusGetMyClanMessagesHandler()); // WORKING
//		mp.put(MediusMessageType.ClearGameListFilter0, new MediusClearGameListFilterZeroHandler()); // not sure if working...
//		mp.put(MediusMessageType.SetGameListFilter0, new MediusSetGameListFilterZeroHandler()); // not sure if working...
//		mp.put(MediusMessageType.SetLobbyWorldFilter, new MediusSetLobbyWorldFilterHandler()); 
//		
//		mp.put(MediusMessageType.GameList_ExtraInfo0, new MediusGameList_ExtraInfoZeroHandler()); // not working
//		
//		mp.put(MediusMessageType.ChannelList_ExtraInfo1, new MediusChannelList_ExtraInfoOneHandler());  // hard coded
//
//		mp.put(MediusMessageType.JoinChannel, new MediusJoinChannelHandler());  // vvvv
//		mp.put(MediusMessageType.ChannelInfo, new MediusChannelInfoHandler());  // vvvv
//		mp.put(MediusMessageType.GetLobbyPlayerNames_ExtraInfo, new MediusGetLobbyPlayerNames_ExtraInfoHandler());  // vvvv
//	
//		// Creating games
//		mp.put(MediusMessageType.GetWorldSecurityLevel, new MediusGetWorldSecurityLevelHandler());
//		mp.put(MediusMessageType.CreateGame1, new MediusCreateGameOneHandler());
//		mp.put(MediusMessageType.JoinGame, new MediusJoinGameHandler());
//		mp.put(MediusMessageType.PlayerReport, new MediusPlayerReportHandler());
//		mp.put(MediusMessageType.WorldReport0, new MediusWorldReportZeroHandler());
//		mp.put(MediusMessageType.GameInfo0, new MediusGameInfoZeroHandler());
//		mp.put(MediusMessageType.EndGameReport, new MediusEndGameReportHandler());
//		mp.put(MediusMessageType.GameWorldPlayerList, new MediusGameWorldPlayerListHandler());
//		
//		// Buddies
//		mp.put(MediusMessageType.AccountGetId,  new MediusAccountGetIdHandler());
//		mp.put(MediusMessageType.AddToBuddyList,  new MediusAddToBuddyListHandler());
//		
//		// Chat
//		mp.put(MediusMessageType.TextFilter,  new MediusTextFilterHandler());
//		mp.put(MediusMessageType.ChatMessage, new MediusChatMessageHandler());
//		
//		// Inspecting player profile
//		mp.put(MediusMessageType.GetIgnoreList, new MediusGetIgnoreListHandler());
//		mp.put(MediusMessageType.GetClanInvitationsSent, new MediusGetClanInvitationsSentHandler());
//		mp.put(MediusMessageType.GetClanMemberList_ExtraInfo, new MediusGetClanMemberList_ExtraInfoHandler());
//
//		// added for Amplitude
//		mp.put(MediusMessageType.AccountRegistration, new MediusAccountRegistrationHandler());
//		mp.put(MediusMessageType.LadderPosition, new MediusLadderPositionHandler());
//		mp.put(MediusMessageType.GetAnnouncements, new MediusGetAnnouncementsHandler());
//		mp.put(MediusMessageType.GameList, new MediusGameListHandler());
//		mp.put(MediusMessageType.LobbyWorldPlayerList, new MediusLobbyWorldPlayerListHandler());
//		mp.put(MediusMessageType.ChannelList, new MediusChannelListHandler());
//		
//		// added for Syphon Filter
//		mp.put(MediusMessageType.GetMyIP, new MediusGetMyIPHandler());
//		mp.put(MediusMessageType.VersionServer, new MediusVersionServerHandler());
//		mp.put(MediusMessageType.PickLocation, new MediusPickLocationHandler());
//		mp.put(MediusMessageType.FindWorldByName, new MediusFindWorldByNameHandler());
//		mp.put(MediusMessageType.GetBuddyInvitations, new MediusGetBuddyInvitationsHandler());
//		mp.put(MediusMessageType.CheckMyClanInvitations, new MediusCheckMyClanInvitationsHandler());
		
		return mp;
	}

	public static final HashMap<MediusMessageType, MediusPacketHandler> getMasMap() {
		HashMap<MediusMessageType, MediusPacketHandler> mp = new HashMap<MediusMessageType, MediusPacketHandler>();

//		// UYA
//		mp.put(MediusMessageType.SessionBegin, new MediusSessionBeginHandler());
//		mp.put(MediusMessageType.DnasSignaturePost, new MediusDnasSignaturePostHandler());
//		mp.put(MediusMessageType.SetLocalizationParams, new MediusSetLocalizationParamsHandler());
//		mp.put(MediusMessageType.AccountLogin, new MediusAccountLoginHandler());
//		mp.put(MediusMessageType.SetLobbyWorldFilter, new MediusSetLobbyWorldFilterHandler());
//		
//		// Other titles
//		mp.put(MediusMessageType.GetMyIP, new MediusGetMyIPHandler());
//		mp.put(MediusMessageType.GetLocations, new MediusGetLocationsHandler());
//		mp.put(MediusMessageType.PickLocation, new MediusPickLocationHandler());
//		mp.put(MediusMessageType.GetAnnouncements, new MediusGetAnnouncementsHandler());
//		mp.put(MediusMessageType.Policy, new MediusPolicyHandler());
//		mp.put(MediusMessageType.MediusServerSessionBeginRequest, new MediusServerSessionBeginHandler());
//		mp.put(MediusMessageType.MediusServerAuthenticationRequest, new MediusServerAuthenticationHandler());
//		mp.put(MediusMessageType.VersionServer, new MediusVersionServerHandler());
//		
		return mp;
	}

}
