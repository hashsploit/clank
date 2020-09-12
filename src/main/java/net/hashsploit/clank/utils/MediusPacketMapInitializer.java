package net.hashsploit.clank.utils;

import java.util.HashMap;

import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.MediusPackets.MediusAccountUpdateStats;
import net.hashsploit.clank.server.medius.MediusPackets.MediusChatToggle;
import net.hashsploit.clank.server.medius.MediusPackets.MediusGetAllAnnouncements;
import net.hashsploit.clank.server.medius.MediusPackets.MediusGetBuddyList_ExtraInfo;
import net.hashsploit.clank.server.medius.MediusPackets.MediusGetLadderStatsWide;
import net.hashsploit.clank.server.medius.MediusPackets.MediusGetLocations;
import net.hashsploit.clank.server.medius.MediusPackets.MediusGetMyClans;
import net.hashsploit.clank.server.medius.MediusPackets.MediusPlayerInfo;
import net.hashsploit.clank.server.medius.MediusPackets.MediusPolicy;
import net.hashsploit.clank.server.medius.MediusPackets.MediusUpdateLadderStatsWide;
import net.hashsploit.clank.server.medius.MediusPackets.MediusUpdateUserState;

public class MediusPacketMapInitializer {
	
	public static final HashMap<MediusPacketType, MediusPacket> getMap() {
		HashMap<MediusPacketType, MediusPacket> mp = new HashMap<MediusPacketType, MediusPacket>();
		
		mp.put(MediusPacketType.UpdateUserState, new MediusUpdateUserState());
		mp.put(MediusPacketType.GetMyClans, new MediusGetMyClans());
		mp.put(MediusPacketType.ChatToggle, new MediusChatToggle());
		mp.put(MediusPacketType.GetLadderStatsWide, new MediusGetLadderStatsWide());
		mp.put(MediusPacketType.AccountUpdateStats, new MediusAccountUpdateStats());
		mp.put(MediusPacketType.UpdateLadderStatsWide, new MediusUpdateLadderStatsWide());
		mp.put(MediusPacketType.PlayerInfo, new MediusPlayerInfo());
		mp.put(MediusPacketType.GetLocations, new MediusGetLocations());
		mp.put(MediusPacketType.Policy, new MediusPolicy());
		mp.put(MediusPacketType.GetAllAnnouncements, new MediusGetAllAnnouncements());
		mp.put(MediusPacketType.GetBuddyList_ExtraInfo, new MediusGetBuddyList_ExtraInfo());

		return mp;
	}

}
