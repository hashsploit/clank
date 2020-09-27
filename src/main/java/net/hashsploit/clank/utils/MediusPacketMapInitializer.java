package net.hashsploit.clank.utils;

import java.util.HashMap;

import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.packets.MediusAccountLogin;
import net.hashsploit.clank.server.medius.packets.MediusAccountLogout;
import net.hashsploit.clank.server.medius.packets.MediusAccountUpdateStats;
import net.hashsploit.clank.server.medius.packets.MediusChannelInfo;
import net.hashsploit.clank.server.medius.packets.MediusChannelList_ExtraInfoOne;
import net.hashsploit.clank.server.medius.packets.MediusChatMessage;
import net.hashsploit.clank.server.medius.packets.MediusChatToggle;
import net.hashsploit.clank.server.medius.packets.MediusClearGameListFilterZero;
import net.hashsploit.clank.server.medius.packets.MediusCreateGameOne;
import net.hashsploit.clank.server.medius.packets.MediusDnasSignaturePost;
import net.hashsploit.clank.server.medius.packets.MediusGameInfoZero;
import net.hashsploit.clank.server.medius.packets.MediusGameList_ExtraInfoZero;
import net.hashsploit.clank.server.medius.packets.MediusGetAllAnnouncements;
import net.hashsploit.clank.server.medius.packets.MediusGetBuddyList_ExtraInfo;
import net.hashsploit.clank.server.medius.packets.MediusGetClanInvitationsSent;
import net.hashsploit.clank.server.medius.packets.MediusGetClanMemberList_ExtraInfo;
import net.hashsploit.clank.server.medius.packets.MediusGetIgnoreList;
import net.hashsploit.clank.server.medius.packets.MediusGetLadderStatsWide;
import net.hashsploit.clank.server.medius.packets.MediusGetLobbyPlayerNames_ExtraInfo;
import net.hashsploit.clank.server.medius.packets.MediusGetLocations;
import net.hashsploit.clank.server.medius.packets.MediusGetMyClanMessages;
import net.hashsploit.clank.server.medius.packets.MediusGetMyClans;
import net.hashsploit.clank.server.medius.packets.MediusJoinChannel;
import net.hashsploit.clank.server.medius.packets.MediusJoinGame;
import net.hashsploit.clank.server.medius.packets.MediusPlayerInfo;
import net.hashsploit.clank.server.medius.packets.MediusPlayerReport;
import net.hashsploit.clank.server.medius.packets.MediusPolicy;
import net.hashsploit.clank.server.medius.packets.MediusSessionBegin;
import net.hashsploit.clank.server.medius.packets.MediusSessionEnd;
import net.hashsploit.clank.server.medius.packets.MediusSetGameListFilterZero;
import net.hashsploit.clank.server.medius.packets.MediusSetLobbyWorldFilter;
import net.hashsploit.clank.server.medius.packets.MediusSetLocalizationParams;
import net.hashsploit.clank.server.medius.packets.MediusTextFilter;
import net.hashsploit.clank.server.medius.packets.MediusUpdateLadderStatsWide;
import net.hashsploit.clank.server.medius.packets.MediusUpdateUserState;
import net.hashsploit.clank.server.medius.packets.MediusWorldReportZero;

public class MediusPacketMapInitializer {
	
	public static final HashMap<MediusPacketType, MediusPacket> getMap() {
		HashMap<MediusPacketType, MediusPacket> mp = new HashMap<MediusPacketType, MediusPacket>();
		
		mp.put(MediusPacketType.AccountLogout, new MediusAccountLogout());
		mp.put(MediusPacketType.SessionEnd, new MediusSessionEnd());
		
		mp.put(MediusPacketType.UpdateUserState, new MediusUpdateUserState()); // WORKING
		mp.put(MediusPacketType.GetMyClans, new MediusGetMyClans()); // WORKING
		mp.put(MediusPacketType.ChatToggle, new MediusChatToggle()); // WORKING
		mp.put(MediusPacketType.GetLadderStatsWide, new MediusGetLadderStatsWide());
		mp.put(MediusPacketType.AccountUpdateStats, new MediusAccountUpdateStats());
		mp.put(MediusPacketType.UpdateLadderStatsWide, new MediusUpdateLadderStatsWide());
		mp.put(MediusPacketType.PlayerInfo, new MediusPlayerInfo());
		mp.put(MediusPacketType.GetLocations, new MediusGetLocations()); // WORKING
		mp.put(MediusPacketType.Policy, new MediusPolicy()); // WORKING
		mp.put(MediusPacketType.GetAllAnnouncements, new MediusGetAllAnnouncements()); // WORKING
		mp.put(MediusPacketType.GetBuddyList_ExtraInfo, new MediusGetBuddyList_ExtraInfo()); 
		mp.put(MediusPacketType.GetMyClanMessages, new MediusGetMyClanMessages()); // WORKING
		mp.put(MediusPacketType.ClearGameListFilter0, new MediusClearGameListFilterZero()); // not sure if working...
		mp.put(MediusPacketType.SetGameListFilter0, new MediusSetGameListFilterZero()); // not sure if working...
		mp.put(MediusPacketType.SetLobbyWorldFilter, new MediusSetLobbyWorldFilter()); 
		
		mp.put(MediusPacketType.GameList_ExtraInfo0, new MediusGameList_ExtraInfoZero()); // not working
		
		mp.put(MediusPacketType.ChannelList_ExtraInfo1, new MediusChannelList_ExtraInfoOne());  // hard coded

		mp.put(MediusPacketType.JoinChannel, new MediusJoinChannel());  // vvvv
		mp.put(MediusPacketType.ChannelInfo, new MediusChannelInfo());  // vvvv
		mp.put(MediusPacketType.GetLobbyPlayerNames_ExtraInfo, new MediusGetLobbyPlayerNames_ExtraInfo());  // vvvv
	
		// Creating games
		mp.put(MediusPacketType.CreateGame1, new MediusCreateGameOne());
		mp.put(MediusPacketType.JoinGame, new MediusJoinGame());
		mp.put(MediusPacketType.PlayerReport, new MediusPlayerReport());
		mp.put(MediusPacketType.WorldReport0, new MediusWorldReportZero());
		mp.put(MediusPacketType.GameInfo0, new MediusGameInfoZero());
		
		// Chat
		mp.put(MediusPacketType.TextFilter,  new MediusTextFilter());
		mp.put(MediusPacketType.ChatMessage, new MediusChatMessage());
		
		// Inspecting player profile
		mp.put(MediusPacketType.GetIgnoreList, new MediusGetIgnoreList());
		mp.put(MediusPacketType.GetClanInvitationsSent, new MediusGetClanInvitationsSent());
		mp.put(MediusPacketType.GetClanMemberList_ExtraInfo, new MediusGetClanMemberList_ExtraInfo());
	
		// MAS stuff
		mp.put(MediusPacketType.SessionBegin, new MediusSessionBegin());
		mp.put(MediusPacketType.DnasSignaturePost, new MediusDnasSignaturePost());
		mp.put(MediusPacketType.SetLocalizationParams, new MediusSetLocalizationParams());
		mp.put(MediusPacketType.AccountLogin, new MediusAccountLogin());
		
		
		return mp;
	}

}
