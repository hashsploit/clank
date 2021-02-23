package net.hashsploit.clank.server.medius.serializers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusGameHostType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class CreateGameOneRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.value];
	private int appId; // int23
	private int minPlayers; // int32
	private int maxPlayers; // int32
	private int gameLevel; // int32
	private String gameName;
	private String gamePassword;
	private String spectatorPassword;
	private int playerSkillLevel; // int32
	private int rulesSet; // int32
	private int genField1; // int32
	private int genField2; // int32
	private int genField3; // int32
	private MediusGameHostType gameHostType; // int32
	private int attributes; // int32
	private boolean hasAttributes;

	public CreateGameOneRequest(byte[] data) {
		super(MediusMessageType.CreateGame1, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.order(ByteOrder.LITTLE_ENDIAN);

		buf.get(messageId);
		buf.get(sessionKey);
		
		buf.getShort(); // buffer
		
		appId = buf.getInt();
		minPlayers = buf.getInt();
		maxPlayers = buf.getInt();
		gameLevel = buf.getInt(); // aquatos channel id?

		byte[] gameNameBytes = new byte[MediusConstants.GAMENAME_MAXLEN.value];
		buf.get(gameNameBytes);
		gameName = Utils.parseMediusString(gameNameBytes);

		byte[] gamePasswordBytes = new byte[MediusConstants.GAMEPASSWORD_MAXLEN.value];
		buf.get(gamePasswordBytes);
		gamePassword = Utils.parseMediusString(gamePasswordBytes);

		byte[] spectatorPasswordBytes = new byte[MediusConstants.GAMEPASSWORD_MAXLEN.value];
		buf.get(spectatorPasswordBytes);
		spectatorPassword = Utils.parseMediusString(spectatorPasswordBytes);

		playerSkillLevel = buf.getInt();
		rulesSet = buf.getInt();
		genField1 = buf.getInt();
		genField2 = buf.getInt();
		genField3 = buf.getInt();
		gameHostType = MediusGameHostType.getTypeFromValue(buf.getInt());
		
		// Support multi-version responses.
		if (data.length == 212) {
			attributes = buf.getInt();
			hasAttributes = true;
		}

	}

	@Override
	public String toString() {
		return "CreateGame1Request: \n" + "messageId: " + Utils.bytesToHex(messageId) + '\n' + "sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + "appId: " + appId + '\n' + "minPlayers: " + minPlayers + '\n' + "maxPlayers: " + maxPlayers + '\n' + "gameLevel: " + gameLevel + '\n' + "gameName: "
				+ gameName + '\n' + "gamePassword: " + gamePassword + '\n' + "spectatorPassword: " + spectatorPassword + '\n' + "playerSkillLevel: " + playerSkillLevel + '\n' + "rulesSet: " + rulesSet + '\n' + "genField1: " + genField1 + '\n' + "genField2: " + genField2 + '\n' + "genField3: "
				+ genField3 + '\n' + "gameHostType: " + gameHostType + '\n' + "attributes: " + attributes;
	}

	public byte[] getMessageId() {
		return messageId;
	}

	public byte[] getSessionKey() {
		return sessionKey;
	}

	public int getAppId() {
		return appId;
	}

	public int getMinPlayers() {
		return minPlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getGameLevel() {
		return gameLevel;
	}

	public String getGameName() {
		return gameName;
	}

	public String getGamePassword() {
		return gamePassword;
	}

	public String getSpectatorPassword() {
		return spectatorPassword;
	}

	public int getPlayerSkillLevel() {
		return playerSkillLevel;
	}

	public int getRulesSet() {
		return rulesSet;
	}

	public int getGenField1() {
		return genField1;
	}

	public int getGenField2() {
		return genField2;
	}

	public int getGenField3() {
		return genField3;
	}

	public MediusGameHostType getGameHostType() {
		return gameHostType;
	}

	public int getAttributes() {
		return attributes;
	}

	public boolean hasAttributes() {
		return hasAttributes;
	}

}
