package net.hashsploit.clank.server.medius.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class WorldReportZeroRequest extends MediusPacket {

	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] worldID = new byte[4];
	private byte[] playerCount = new byte[4];
	private byte[] gameName = new byte[MediusConstants.GAMENAME_MAXLEN.getValue()];
	private byte[] gameStats = new byte[MediusConstants.GAMESTATS_MAXLEN.getValue()];
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
		super(MediusPacketType.WorldReport0, data);
		
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
	

	public synchronized byte[] getSessionKey() {
		return sessionKey;
	}

	public synchronized void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}

	public synchronized byte[] getWorldID() {
		return worldID;
	}

	public synchronized void setWorldID(byte[] worldID) {
		this.worldID = worldID;
	}

	public synchronized byte[] getPlayerCount() {
		return playerCount;
	}

	public synchronized void setPlayerCount(byte[] playerCount) {
		this.playerCount = playerCount;
	}

	public synchronized byte[] getGameName() {
		return gameName;
	}

	public synchronized void setGameName(byte[] gameName) {
		this.gameName = gameName;
	}

	public synchronized byte[] getGameStats() {
		return gameStats;
	}

	public synchronized void setGameStats(byte[] gameStats) {
		this.gameStats = gameStats;
	}

	public synchronized byte[] getMinPlayers() {
		return minPlayers;
	}

	public synchronized void setMinPlayers(byte[] minPlayers) {
		this.minPlayers = minPlayers;
	}

	public synchronized byte[] getMaxPlayers() {
		return maxPlayers;
	}

	public synchronized void setMaxPlayers(byte[] maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public synchronized byte[] getGameLevel() {
		return gameLevel;
	}

	public synchronized void setGameLevel(byte[] gameLevel) {
		this.gameLevel = gameLevel;
	}

	public synchronized byte[] getPlayerSkillLevel() {
		return playerSkillLevel;
	}

	public synchronized void setPlayerSkillLevel(byte[] playerSkillLevel) {
		this.playerSkillLevel = playerSkillLevel;
	}

	public synchronized byte[] getRulesSet() {
		return rulesSet;
	}

	public synchronized void setRulesSet(byte[] rulesSet) {
		this.rulesSet = rulesSet;
	}

	public synchronized byte[] getGenField1() {
		return genField1;
	}

	public synchronized void setGenField1(byte[] genField1) {
		this.genField1 = genField1;
	}

	public synchronized byte[] getGenField2() {
		return genField2;
	}

	public synchronized void setGenField2(byte[] genField2) {
		this.genField2 = genField2;
	}

	public synchronized byte[] getGenField3() {
		return genField3;
	}

	public synchronized void setGenField3(byte[] genField3) {
		this.genField3 = genField3;
	}

	public synchronized byte[] getWorldStatus() {
		return worldStatus;
	}

	public synchronized void setWorldStatus(byte[] worldStatus) {
		this.worldStatus = worldStatus;
	}

}
