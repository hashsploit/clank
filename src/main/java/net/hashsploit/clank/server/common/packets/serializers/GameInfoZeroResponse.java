package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GameInfoZeroResponse extends MediusMessage {

	private byte[] messageID;
	private byte[] callbackStatus;
	private byte[] appID;
	private byte[] minPlayers;
	private byte[] maxPlayers;
	private byte[] gameLevel;
	private byte[] playerSkillLevel;
	private byte[] playerCount;
	private byte[] gameStats;
	private byte[] gameName;
	private byte[] rulesSet;
	private byte[] genField1;
	private byte[] genField2;
	private byte[] genField3;
	private byte[] worldStatus;
	private byte[] gameHostType;
	
	public GameInfoZeroResponse(byte[] messageID, byte[] callbackStatus, byte[] appID, byte[] minPlayers, byte[] maxPlayers, byte[] gameLevel, 
			byte[] playerSkillLevel, byte[] playerCount, byte[] gameStats, byte[] gameName, byte[] rulesSet, byte[] genField1, byte[] genField2, byte[] genField3,
			byte[] worldStatus, byte[] gameHostType) {
		super(MediusMessageType.GameInfoResponse0);
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.appID = appID;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.gameLevel = gameLevel;
		this.playerSkillLevel = playerSkillLevel;
		this.playerCount = playerCount;
		this.gameStats = gameStats;
		this.gameName = gameName;
		this.rulesSet = rulesSet;
		this.genField1 = genField1;
		this.genField2 = genField2;
		this.genField3 = genField3;
		this.worldStatus = worldStatus;
		this.gameHostType = gameHostType;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);
			outputStream.write(appID); // app id
			outputStream.write(minPlayers); // minplayers
			outputStream.write(maxPlayers); // maxplayers
			outputStream.write(gameLevel); // gameLevel (aquatos channel id it seems)
			outputStream.write(playerSkillLevel); // playerSkillLevel -- weapons. last byte is bolt skill
			outputStream.write(playerCount); // playerCount
			outputStream.write(gameStats);
			outputStream.write(gameName); // includes some weird 303030303132202020 (cities id?)
			outputStream.write(rulesSet); // rulesset -- weapons
			outputStream.write(genField1); // genericField1 -- cities location ID
			outputStream.write(genField2); // genericField2 -- unknown
			outputStream.write(genField3); // genericField3	-- map, settings
			outputStream.write(worldStatus); // 1 = staging
			outputStream.write(gameHostType); // gameHostType // 0 to 4
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "GameInfoZeroResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"appID: " + Utils.bytesToHex(appID) + '\n' + 
				"minPlayers: " + Utils.bytesToHex(minPlayers) + '\n' + 
				"maxPlayers: " + Utils.bytesToHex(maxPlayers) + '\n' + 
				"gameLevel: " + Utils.bytesToHex(gameLevel) + '\n' + 
				"playerSkillLevel: " + Utils.bytesToHex(playerSkillLevel) + '\n' + 
				"playerCount: " + Utils.bytesToHex(playerCount) + '\n' + 
				"gameStats: " + Utils.bytesToHex(gameStats) + '\n' + 
				"gameName: " + Utils.bytesToHex(gameName) + '\n' + 
				"rulesSet: " + Utils.bytesToHex(rulesSet) + '\n' + 
				"genField1: " + Utils.bytesToHex(genField1) + '\n' + 
				"genField2: " + Utils.bytesToHex(genField2) + '\n' + 
				"genField3: " + Utils.bytesToHex(genField3) + '\n' + 
				"worldStatus: " + Utils.bytesToHex(worldStatus) + '\n' + 
				"gameHostType: " + Utils.bytesToHex(gameHostType);
	}
	
	public synchronized byte[] getMessageID() {
		return messageID;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}

	public synchronized byte[] getCallbackStatus() {
		return callbackStatus;
	}

	public synchronized void setCallbackStatus(byte[] callbackStatus) {
		this.callbackStatus = callbackStatus;
	}

	public synchronized byte[] getAppID() {
		return appID;
	}

	public synchronized void setAppID(byte[] appID) {
		this.appID = appID;
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

	public synchronized byte[] getPlayerCount() {
		return playerCount;
	}

	public synchronized void setPlayerCount(byte[] playerCount) {
		this.playerCount = playerCount;
	}

	public synchronized byte[] getGameStats() {
		return gameStats;
	}

	public synchronized void setGameStats(byte[] gameStats) {
		this.gameStats = gameStats;
	}

	public synchronized byte[] getGameName() {
		return gameName;
	}

	public synchronized void setGameName(byte[] gameName) {
		this.gameName = gameName;
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

	public synchronized byte[] getGameHostType() {
		return gameHostType;
	}

	public synchronized void setGameHostType(byte[] gameHostType) {
		this.gameHostType = gameHostType;
	}

	
	
}
