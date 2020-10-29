package net.hashsploit.clank.utils;

import java.util.HashMap;

import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.packets.handlers.MediusAccountLoginHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusAccountLogoutHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusAccountRegistrationHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusAccountUpdateStatsHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusChannelInfoHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusChannelListHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusChannelList_ExtraInfoOneHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusChatMessageHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusChatToggleHandler;
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
import net.hashsploit.clank.server.medius.packets.handlers.MediusJoinChannelHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusJoinGameHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusLadderPositionHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusLobbyWorldPlayerListHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusPickLocationHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusPlayerInfoHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusPlayerReportHandler;
import net.hashsploit.clank.server.medius.packets.handlers.MediusPolicyHandler;
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

public class MediusPacketMapInitializer {
	
	public static final HashMap<MediusPacketType, MediusPacketHandler> getMap() {
		HashMap<MediusPacketType, MediusPacketHandler> mp = new HashMap<MediusPacketType, MediusPacketHandler>();
		
		mp.put(MediusPacketType.AccountLogout, new MediusAccountLogoutHandler());
		mp.put(MediusPacketType.SessionEnd, new MediusSessionEndHandler());
		
		mp.put(MediusPacketType.UpdateUserState, new MediusUpdateUserStateHandler()); // WORKING
		mp.put(MediusPacketType.GetMyClans, new MediusGetMyClansHandler()); // WORKING
		mp.put(MediusPacketType.ChatToggle, new MediusChatToggleHandler()); // WORKING
		mp.put(MediusPacketType.GetLadderStatsWide, new MediusGetLadderStatsWideHandler());
		mp.put(MediusPacketType.AccountUpdateStats, new MediusAccountUpdateStatsHandler());
		mp.put(MediusPacketType.UpdateLadderStatsWide, new MediusUpdateLadderStatsWideHandler());
		mp.put(MediusPacketType.PlayerInfo, new MediusPlayerInfoHandler());
		mp.put(MediusPacketType.GetLocations, new MediusGetLocationsHandler()); // WORKING
		mp.put(MediusPacketType.Policy, new MediusPolicyHandler()); // WORKING
		mp.put(MediusPacketType.GetAllAnnouncements, new MediusGetAllAnnouncementsHandler()); // WORKING
		mp.put(MediusPacketType.GetBuddyList_ExtraInfo, new MediusGetBuddyList_ExtraInfoHandler()); 
		mp.put(MediusPacketType.GetMyClanMessages, new MediusGetMyClanMessagesHandler()); // WORKING
		mp.put(MediusPacketType.ClearGameListFilter0, new MediusClearGameListFilterZeroHandler()); // not sure if working...
		mp.put(MediusPacketType.SetGameListFilter0, new MediusSetGameListFilterZeroHandler()); // not sure if working...
		mp.put(MediusPacketType.SetLobbyWorldFilter, new MediusSetLobbyWorldFilterHandler()); 
		
		mp.put(MediusPacketType.GameList_ExtraInfo0, new MediusGameList_ExtraInfoZeroHandler()); // not working
		
		mp.put(MediusPacketType.ChannelList_ExtraInfo1, new MediusChannelList_ExtraInfoOneHandler());  // hard coded

		mp.put(MediusPacketType.JoinChannel, new MediusJoinChannelHandler());  // vvvv
		mp.put(MediusPacketType.ChannelInfo, new MediusChannelInfoHandler());  // vvvv
		mp.put(MediusPacketType.GetLobbyPlayerNames_ExtraInfo, new MediusGetLobbyPlayerNames_ExtraInfoHandler());  // vvvv
	
		// Creating games
		mp.put(MediusPacketType.CreateGame1, new MediusCreateGameOneHandler());
		mp.put(MediusPacketType.JoinGame, new MediusJoinGameHandler());
		mp.put(MediusPacketType.PlayerReport, new MediusPlayerReportHandler());
		mp.put(MediusPacketType.WorldReport0, new MediusWorldReportZeroHandler());
		mp.put(MediusPacketType.GameInfo0, new MediusGameInfoZeroHandler());
		mp.put(MediusPacketType.EndGameReport, new MediusEndGameReportHandler());
		mp.put(MediusPacketType.GameWorldPlayerList, new MediusGameWorldPlayerListHandler());
		
		// Chat
		mp.put(MediusPacketType.TextFilter,  new MediusTextFilterHandler());
		mp.put(MediusPacketType.ChatMessage, new MediusChatMessageHandler());
		
		// Inspecting player profile
		mp.put(MediusPacketType.GetIgnoreList, new MediusGetIgnoreListHandler());
		mp.put(MediusPacketType.GetClanInvitationsSent, new MediusGetClanInvitationsSentHandler());
		mp.put(MediusPacketType.GetClanMemberList_ExtraInfo, new MediusGetClanMemberList_ExtraInfoHandler());
	
		// MAS stuff
		mp.put(MediusPacketType.SessionBegin, new MediusSessionBeginHandler());
		mp.put(MediusPacketType.DnasSignaturePost, new MediusDnasSignaturePostHandler());
		mp.put(MediusPacketType.SetLocalizationParams, new MediusSetLocalizationParamsHandler());
		mp.put(MediusPacketType.AccountLogin, new MediusAccountLoginHandler());
		
		// added for Amplitude
		mp.put(MediusPacketType.AccountRegistration, new MediusAccountRegistrationHandler());
		mp.put(MediusPacketType.LadderPosition, new MediusLadderPositionHandler());
		mp.put(MediusPacketType.GetAnnouncements, new MediusGetAnnouncementsHandler());
		mp.put(MediusPacketType.GameList, new MediusGameListHandler());
		mp.put(MediusPacketType.LobbyWorldPlayerList, new MediusLobbyWorldPlayerListHandler());
		mp.put(MediusPacketType.ChannelList, new MediusChannelListHandler());
		
		// added for Syphon Filter
		mp.put(MediusPacketType.GetMyIP, new MediusGetMyIPHandler());
		mp.put(MediusPacketType.VersionServer, new MediusVersionServerHandler());
		mp.put(MediusPacketType.PickLocation, new MediusPickLocationHandler());
		mp.put(MediusPacketType.FindWorldByName, new MediusFindWorldByNameHandler());
		
		
		
		return mp;
	}

}
