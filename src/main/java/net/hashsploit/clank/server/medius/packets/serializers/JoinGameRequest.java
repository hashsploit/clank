package net.hashsploit.clank.server.medius.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class JoinGameRequest extends MediusMessage {

	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	byte[] worldIdToJoin = new byte[4];
	byte[] joinType = new byte[4];
	byte[] gamePassword = new byte[MediusConstants.GAMEPASSWORD_MAXLEN.getValue()];
	byte[] gameHostType = new byte[4];
	byte[] RSApubKey = new byte[10];
	
	public JoinGameRequest(byte[] data) {
		super(MediusMessageType.JoinGame, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);

		buf.get(messageID);
		buf.get(sessionKey);
		buf.get(new byte[2]);// padding
		buf.get(worldIdToJoin);
		buf.get(joinType);
		buf.get(gamePassword);
		buf.get(gameHostType);
	}
	
	public String toString() {
		return "JoinGameRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"worldIdToJoin: " + Utils.bytesToHex(worldIdToJoin) + '\n' + 
				"joinType: " + Utils.bytesToHex(joinType) + '\n' + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"gamePassword: " + Utils.bytesToHex(gamePassword) + '\n' + 
				"gameHostType: " + Utils.bytesToHex(gameHostType);		
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

	public synchronized byte[] getWorldIdToJoin() {
		return worldIdToJoin;
	}

	public synchronized void setWorldIdToJoin(byte[] worldIdToJoin) {
		this.worldIdToJoin = worldIdToJoin;
	}

	public synchronized byte[] getJoinType() {
		return joinType;
	}

	public synchronized void setJoinType(byte[] joinType) {
		this.joinType = joinType;
	}

	public synchronized byte[] getGamePassword() {
		return gamePassword;
	}

	public synchronized void setGamePassword(byte[] gamePassword) {
		this.gamePassword = gamePassword;
	}

	public synchronized byte[] getGameHostType() {
		return gameHostType;
	}

	public synchronized void setGameHostType(byte[] gameHostType) {
		this.gameHostType = gameHostType;
	}

	public synchronized byte[] getRSApubKey() {
		return RSApubKey;
	}

	public synchronized void setRSApubKey(byte[] rSApubKey) {
		RSApubKey = rSApubKey;
	}
	
}
