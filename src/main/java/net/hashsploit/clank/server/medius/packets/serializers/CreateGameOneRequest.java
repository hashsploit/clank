package net.hashsploit.clank.server.medius.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class CreateGameOneRequest extends MediusPacket {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] appID = new byte[4];
	private byte[] minPlayers = new byte[4];
	private byte[] maxPlayers = new byte[4];
	private byte[] gameLevel = new byte[4];
	private byte[] gameName = new byte[MediusConstants.GAMENAME_MAXLEN.getValue()];
	private byte[] gamePassword = new byte[MediusConstants.GAMEPASSWORD_MAXLEN.getValue()];
	private byte[] spectatorPassword = new byte[MediusConstants.GAMEPASSWORD_MAXLEN.getValue()];
	private byte[] playerSkillLevel = new byte[4];
	private byte[] rulesSet = new byte[4];
	private byte[] genField1 = new byte[4];
	private byte[] genField2 = new byte[4];
	private byte[] genField3 = new byte[4];
	private byte[] gameHostType = new byte[4];
	private byte[] attributes = new byte[4];
	
	public CreateGameOneRequest(byte[] data) {
		super(MediusPacketType.CreateGame1, data);
    	ByteBuffer buf = ByteBuffer.wrap(data);
    	buf.get(messageID);
    	buf.get(sessionKey);
    	buf.getShort(); //buffer
    	buf.get(appID);
    	buf.get(minPlayers);
    	buf.get(maxPlayers);
    	buf.get(gameLevel);//aquatos channel id?
    	buf.get(gameName);
    	buf.get(gamePassword);
    	buf.get(spectatorPassword);
    	buf.get(playerSkillLevel);
    	buf.get(rulesSet);
    	buf.get(genField1);
    	buf.get(genField2);
    	buf.get(genField3);
    	buf.get(gameHostType);
    	buf.get(attributes);
	}
	
	public String toString() {
		return "CreateGame1Request: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"appID: " + Utils.bytesToHex(appID) + '\n' + 
				"minPlayers: " + Utils.bytesToHex(minPlayers) + '\n' + 
				"maxPlayers: " + Utils.bytesToHex(maxPlayers) + '\n' + 
				"gameLevel: " + Utils.bytesToHex(gameLevel) + '\n' + 
				"gameName: " + Utils.bytesToHex(gameName) + '\n' + 
				"gamePassword: " + Utils.bytesToHex(gamePassword) + '\n' + 
				"spectatorPassword: " + Utils.bytesToHex(spectatorPassword) + '\n' + 
				"playerSkillLevel: " + Utils.bytesToHex(playerSkillLevel) + '\n' + 
				"rulesSet: " + Utils.bytesToHex(rulesSet) + '\n' + 
				"genField1: " + Utils.bytesToHex(genField1) + '\n' + 
				"genField2: " + Utils.bytesToHex(genField2) + '\n' + 
				"genField3: " + Utils.bytesToHex(genField3) + '\n' + 
				"gameHostType: " + Utils.bytesToHex(gameHostType) + '\n' + 
				"attributes: " + Utils.bytesToHex(attributes);		
	}
	

	public synchronized byte[] getMessageID() {
		return messageID;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}

	public synchronized byte[] getSessionKey() {
		return sessionKey;
	}

	public synchronized void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
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

	public synchronized byte[] getGameName() {
		return gameName;
	}

	public synchronized void setGameName(byte[] gameName) {
		this.gameName = gameName;
	}

	public synchronized byte[] getGamePassword() {
		return gamePassword;
	}

	public synchronized void setGamePassword(byte[] gamePassword) {
		this.gamePassword = gamePassword;
	}

	public synchronized byte[] getSpectatorPassword() {
		return spectatorPassword;
	}

	public synchronized void setSpectatorPassword(byte[] spectatorPassword) {
		this.spectatorPassword = spectatorPassword;
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

	public synchronized byte[] getGameHostType() {
		return gameHostType;
	}

	public synchronized void setGameHostType(byte[] gameHostType) {
		this.gameHostType = gameHostType;
	}

	public synchronized byte[] getAttributes() {
		return attributes;
	}

	public synchronized void setAttributes(byte[] attributes) {
		this.attributes = attributes;
	}

	
}
