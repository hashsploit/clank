package net.hashsploit.clank.server.medius.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class GameList_ExtraInfoZeroResponse extends MediusPacket {

	private byte[] messageID;
	private byte[] mediusWorldID;
	private byte[] callbackStatus;
	private byte[] playerCount;
	private byte[] minPlayers;
	private byte[] maxPlayers;
	private byte[] gameLevel;
	private byte[] playerSkillLevel;
	private byte[] rulesSet;
	private byte[] genericField1;
	private byte[] genericField2;
	private byte[] genericField3;
	private byte[] worldSecurityLevelType;
	private byte[] worldStatus;
	private byte[] gameHostType;
	private byte[] gameName;
	private byte[] gameStats;
	private byte[] endOfList;
	
	public GameList_ExtraInfoZeroResponse(byte[] messageID, byte[] mediusWorldID, byte[] callbackStatus, byte[] playerCount, byte[] minPlayers, byte[] maxPlayers, 
			byte[] gameLevel, byte[] playerSkillLevel, byte[] rulesSet, byte[] genericField1, byte[] genericField2, byte[] genericField3, 
			byte[] worldSecurityLevelType, byte[] worldStatus, byte[] gameHostType, byte[] gameName, byte[] gameStats, byte[] endOfList) {
		super(MediusPacketType.GameList_ExtraInfoResponse0);
		this.messageID = messageID;
		this.mediusWorldID = mediusWorldID;
		this.callbackStatus = callbackStatus;
		this.playerCount = playerCount;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.gameLevel = gameLevel;
		this.playerSkillLevel = playerSkillLevel;
		this.rulesSet = rulesSet;
		this.genericField1 = genericField1;
		this.genericField2 = genericField2;
		this.genericField3 = genericField3;
		this.worldSecurityLevelType = worldSecurityLevelType;
		this.worldStatus = worldStatus;
		this.gameHostType = gameHostType;
		this.gameName = gameName;
		this.gameStats = gameStats;
		this.endOfList = endOfList;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			
			outputStream.write(callbackStatus); // Callback status (int)
			outputStream.write(mediusWorldID); // MediusWorldId (lobby/channel Id)
			
			outputStream.write(playerCount); // Player count
			outputStream.write(minPlayers); // Min players
			outputStream.write(maxPlayers); // Max players
			outputStream.write(Utils.hexStringToByteArray("0000")); // Padding
			
			outputStream.write(gameLevel); // Game level
			outputStream.write(playerSkillLevel); // Player skill level (first 3 bytes are disabled weapons, last byte = bolt skill)
			
			outputStream.write(rulesSet); // Rules set (might be used as team colors or skins)
			outputStream.write(genericField1); // Generic field 1 (Location Id (city id; aquatos))
			outputStream.write(genericField2); // Generic field 2
			outputStream.write(genericField3); // Generic field 3 (map type, game settings)
			outputStream.write(worldSecurityLevelType); // Security level
			outputStream.write(worldStatus); // World status
			outputStream.write(gameHostType); // Game Host Type
			
			// GameName (contains some ID in it, remainder of the name string buffer is 0x20 [space character]) 
			outputStream.write(gameName);
			
			// GameStats
			outputStream.write(gameStats);
			
			// End of list
			outputStream.write(endOfList);
		} catch (IOException e) {
			
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//			outputStream.write(messageID);
//			outputStream.write(Utils.hexStringToByteArray("0000000000000065EB0000020000000800000045070000000000040000000028000000000000000800E41100000000010000000400000031763120662e6f20646f7820202020202020303030303030323830303030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		return outputStream.toByteArray();
	}
	
	public String toString() {
		return "GameList_ExtraInfoResponse0: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"mediusWorldID: " + Utils.bytesToHex(mediusWorldID) + '\n' + 
				"playerCount: " + Utils.bytesToHex(playerCount) + '\n' + 
				"minPlayers: " + Utils.bytesToHex(minPlayers) + '\n' + 
				"maxPlayers: " + Utils.bytesToHex(maxPlayers) + '\n' + 
				"gameLevel: " + Utils.bytesToHex(gameLevel) + '\n' + 
				"playerSkillLevel: " + Utils.bytesToHex(playerSkillLevel) + '\n' + 
				"rulesSet: " + Utils.bytesToHex(rulesSet) + '\n' + 
				"genericField1: " + Utils.bytesToHex(genericField1) + '\n' + 
				"genericField2: " + Utils.bytesToHex(genericField2) + '\n' + 
				"genericField3: " + Utils.bytesToHex(genericField3) + '\n' + 
				"worldSecurityLevelType: " + Utils.bytesToHex(worldSecurityLevelType) + '\n' + 
				"worldStatus: " + Utils.bytesToHex(worldStatus) + '\n' + 
				"gameHostType: " + Utils.bytesToHex(gameHostType) + '\n' + 
				"gameName: " + Utils.bytesToHex(gameName) + '\n' + 
				"gameStats: " + Utils.bytesToHex(gameStats) + '\n' + 
				"endOfList: " + Utils.bytesToHex(endOfList);
	}
		
}
