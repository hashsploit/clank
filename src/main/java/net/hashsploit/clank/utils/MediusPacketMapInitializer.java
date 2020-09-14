package net.hashsploit.clank.utils;

import java.util.HashMap;

import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.MediusPackets.MediusAccountUpdateStats;
import net.hashsploit.clank.server.medius.MediusPackets.MediusChannelList_ExtraInfoOne;
import net.hashsploit.clank.server.medius.MediusPackets.MediusChatToggle;
import net.hashsploit.clank.server.medius.MediusPackets.MediusClearGameListFilterZero;
import net.hashsploit.clank.server.medius.MediusPackets.MediusGameList_ExtraInfoZero;
import net.hashsploit.clank.server.medius.MediusPackets.MediusGetAllAnnouncements;
import net.hashsploit.clank.server.medius.MediusPackets.MediusGetBuddyList_ExtraInfo;
import net.hashsploit.clank.server.medius.MediusPackets.MediusGetLadderStatsWide;
import net.hashsploit.clank.server.medius.MediusPackets.MediusGetLocations;
import net.hashsploit.clank.server.medius.MediusPackets.MediusGetMyClanMessages;
import net.hashsploit.clank.server.medius.MediusPackets.MediusGetMyClans;
import net.hashsploit.clank.server.medius.MediusPackets.MediusPlayerInfo;
import net.hashsploit.clank.server.medius.MediusPackets.MediusPolicy;
import net.hashsploit.clank.server.medius.MediusPackets.MediusSetGameListFilterZero;
import net.hashsploit.clank.server.medius.MediusPackets.MediusSetLobbyWorldFilter;
import net.hashsploit.clank.server.medius.MediusPackets.MediusUpdateLadderStatsWide;
import net.hashsploit.clank.server.medius.MediusPackets.MediusUpdateUserState;

public class MediusPacketMapInitializer {
	
	public static final HashMap<MediusPacketType, MediusPacket> getMap() {
		HashMap<MediusPacketType, MediusPacket> mp = new HashMap<MediusPacketType, MediusPacket>();
		
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

	
		return mp;
	}

}
