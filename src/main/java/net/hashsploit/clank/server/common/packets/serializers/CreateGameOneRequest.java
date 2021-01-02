package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class CreateGameOneRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
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
	private boolean noAttributes = false;
	
	public CreateGameOneRequest(byte[] data) {
		super(MediusMessageType.CreateGame1, data);
    	ByteBuffer buf = ByteBuffer.wrap(data);
    	
    	// Support multi-version responses.
    	if (data.length == 212) {
    		buf.get(messageId);
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
    	} else {
    		buf.get(messageId);
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
        	noAttributes = true;
    	}
    	
    	
	}
	
	public String toString() {
		return "CreateGame1Request: \n" + 
				"messageID: " + Utils.bytesToHex(messageId) + '\n' + 
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
	

	public byte[] getMessageId() {
		return messageId;
	}

	public byte[] getSessionKey() {
		return sessionKey;
	}

	public byte[] getAppId() {
		return appID;
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

	public byte[] getGameName() {
		return gameName;
	}

	public byte[] getGamePassword() {
		return gamePassword;
	}

	public byte[] getSpectatorPassword() {
		return spectatorPassword;
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

	public byte[] getGameHostType() {
		return gameHostType;
	}

	public synchronized byte[] getAttributes() {
		return attributes;
	}
	
}
