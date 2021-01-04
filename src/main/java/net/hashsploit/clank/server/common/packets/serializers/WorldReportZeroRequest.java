package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class WorldReportZeroRequest extends MediusMessage {

	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.value];
	private byte[] worldID = new byte[4];
	private byte[] playerCount = new byte[4];
	private byte[] gameName = new byte[MediusConstants.GAMENAME_MAXLEN.value];
	private byte[] gameStats = new byte[MediusConstants.GAMESTATS_MAXLEN.value];
	private byte[] minPlayers = new byte[4];
	private byte[] maxPlayers = new byte[4];
	private byte[] gameLevel = new byte[4];
	private byte[] playerSkillLevel = new byte[4];
	private byte[] rulesSet = new byte[4];
	private byte[] genField1 = new byte[4];
	private byte[] genField2 = new byte[4];
	private byte[] genField3 = new byte[4];
	private byte[] worldStatus = new byte[4];
	
	public WorldReportZeroRequest(byte[] data) {
		super(MediusMessageType.WorldReport0, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(sessionKey);
		buf.get(new byte[3]); // buffer
		buf.get(worldID);
		buf.get(playerCount);
		buf.get(gameName);
		buf.get(gameStats);
		buf.get(minPlayers);
		buf.get(maxPlayers);
		buf.get(gameLevel);
		buf.get(playerSkillLevel);
		buf.get(rulesSet);
		buf.get(genField1);
		buf.get(genField2);
		buf.get(genField3);
		buf.get(worldStatus);
	}
	
	public String toString() {
		return "WorldReportZeroRequest: \n" + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"worldID: " + Utils.bytesToHex(worldID) + '\n' + 
				"playerCount: " + Utils.bytesToHex(playerCount) + '\n' + 
				"gameName: " + Utils.bytesToHex(gameName) + '\n' + 
				"gameStats: " + Utils.bytesToHex(gameStats) + '\n' + 
				"minPlayers: " + Utils.bytesToHex(minPlayers) + '\n' + 
				"maxPlayers: " + Utils.bytesToHex(maxPlayers) + '\n' + 
				"gameLevel: " + Utils.bytesToHex(gameLevel) + '\n' + 
				"playerSkillLevel: " + Utils.bytesToHex(playerSkillLevel) + '\n' + 
				"rulesSet: " + Utils.bytesToHex(rulesSet) + '\n' + 
				"genField1: " + Utils.bytesToHex(genField1) + '\n' + 
				"genField2: " + Utils.bytesToHex(genField2) + '\n' + 
				"genField3: " + Utils.bytesToHex(genField3) + '\n' + 
				"worldStatus: " + Utils.bytesToHex(worldStatus);		
	}
	

	public byte[] getSessionKey() {
		return sessionKey;
	}

	public byte[] getWorldID() {
		return worldID;
	}

	public byte[] getPlayerCount() {
		return playerCount;
	}

	public byte[] getGameName() {
		return gameName;
	}

	public byte[] getGameStats() {
		return gameStats;
	}

	public byte[] getMinPlayers() {
		return minPlayers;
	}

	public byte[] getMaxPlayers() {
		return maxPlayers;
	}

	public byte[] getGameLevel() {
		return gameLevel;
	}

	public byte[] getPlayerSkillLevel() {
		return playerSkillLevel;
	}

	public byte[] getRulesSet() {
		return rulesSet;
	}

	public byte[] getGenField1() {
		return genField1;
	}

	public byte[] getGenField2() {
		return genField2;
	}

	public byte[] getGenField3() {
		return genField3;
	}

	public byte[] getWorldStatus() {
		return worldStatus;
	}

}
