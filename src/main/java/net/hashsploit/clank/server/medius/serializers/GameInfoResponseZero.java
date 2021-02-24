package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusGameHostType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.utils.Utils;

public class GameInfoResponseZero extends MediusMessage {
	
	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private MediusCallbackStatus callbackStatus;
	private int appId; // int23
	private int minPlayers; // int32
	private int maxPlayers; // int32
	private int gameLevel; // int32
	private int playerSkillLevel; // int32
	private int playerCount; // int32
	private byte[] gameStats;
	private String gameName;
	private int rulesSet; // int32
	private int genField1; // int32
	private int genField2; // int32
	private int genField3; // int32
	private MediusWorldStatus worldStatus;
	private MediusGameHostType gameHostType; // int32
	
	public GameInfoResponseZero(byte[] messageId, MediusCallbackStatus callbackStatus, int appId, int minPlayers, int maxPlayers, int gameLevel, 
			int playerSkillLevel, int playerCount, byte[] gameStats, String gameName, int rulesSet, int genField1, int genField2, int genField3,
			MediusWorldStatus worldStatus, MediusGameHostType gameHostType) {
		super(MediusMessageType.GameInfoResponse0);
		this.messageId = messageId;
		this.callbackStatus = callbackStatus;
		this.appId = appId;
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
			outputStream.write(messageId);
			outputStream.write(new byte[3]);
			
			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
			
			outputStream.write(Utils.intToBytesLittle(appId)); // app id
			outputStream.write(Utils.intToBytesLittle(minPlayers)); // min players
			outputStream.write(Utils.intToBytesLittle(maxPlayers)); // max players
			outputStream.write(Utils.intToBytesLittle(gameLevel)); // gameLevel (aquatos channel id it seems)
			outputStream.write(Utils.intToBytesLittle(playerSkillLevel)); // playerSkillLevel -- weapons. last byte is bolt skill
			outputStream.write(Utils.intToBytesLittle(playerCount)); // playerCount
			outputStream.write(gameStats);
			outputStream.write(Utils.buildByteArrayFromString(gameName, MediusConstants.GAMENAME_MAXLEN.value)); // includes some weird 303030303132202020 (cities id?)
			
			outputStream.write(Utils.intToBytesLittle(rulesSet)); // rulesset -- weapons
			outputStream.write(Utils.intToBytesLittle(genField1)); // genericField1 -- cities location ID
			outputStream.write(Utils.intToBytesLittle(genField2)); // genericField2 -- unknown
			outputStream.write(Utils.intToBytesLittle(genField3)); // genericField3	-- map, settings
			outputStream.write(Utils.intToBytesLittle(worldStatus.value)); // 1 = staging
			outputStream.write(Utils.intToBytesLittle(gameHostType.value)); // gameHostType // 0 to 4
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "GameInfoResponseZero: \n" + 
				"messageId: " + Utils.bytesToHex(messageId) + '\n' + 
				"callbackStatus: " + callbackStatus + '\n' + 
				"appId: " + appId + '\n' + 
				"minPlayers: " + minPlayers + '\n' + 
				"maxPlayers: " + maxPlayers + '\n' + 
				"gameLevel: " + gameLevel + '\n' + 
				"playerSkillLevel: " + playerSkillLevel + '\n' + 
				"playerCount: " + playerCount + '\n' + 
				"gameStats: " + gameStats + '\n' + 
				"gameName: " + gameName + '\n' + 
				"rulesSet: " + rulesSet + '\n' + 
				"genField1: " + genField1 + '\n' + 
				"genField2: " + genField2 + '\n' + 
				"genField3: " + genField3 + '\n' + 
				"worldStatus: " + worldStatus + '\n' + 
				"gameHostType: " + gameHostType;
	}

	public byte[] getMessageId() {
		return messageId;
	}

	public MediusCallbackStatus getCallbackStatus() {
		return callbackStatus;
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

	public int getPlayerSkillLevel() {
		return playerSkillLevel;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public byte[] getGameStats() {
		return gameStats;
	}

	public String getGameName() {
		return gameName;
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

	public MediusWorldStatus getWorldStatus() {
		return worldStatus;
	}

	public MediusGameHostType getGameHostType() {
		return gameHostType;
	}
	
}
