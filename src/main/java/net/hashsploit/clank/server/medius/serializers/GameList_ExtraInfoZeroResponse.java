package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import net.hashsploit.clank.server.ChatColor;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusGameHostType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusWorldSecurityLevelType;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.utils.Utils;

public class GameList_ExtraInfoZeroResponse extends MediusMessage {
	protected static final Logger logger = Logger.getLogger(MediusPacketHandler.class.getName());

	private byte[] messageId;
	private int mediusWorldId;
	private MediusCallbackStatus callbackStatus;
	private short playerCount;
	private short minPlayers;
	private short maxPlayers;
	private int gameLevel;
	private int playerSkillLevel;
	private int rulesSet;
	private int genericField1;
	private int genericField2;
	private int genericField3;
	private MediusWorldSecurityLevelType worldSecurityLevelType;
	private MediusWorldStatus worldStatus;
	private MediusGameHostType gameHostType;
	private String gameName;
	private byte[] gameStats;
	private boolean endOfList;

	public GameList_ExtraInfoZeroResponse(byte[] messageId, int mediusWorldId, MediusCallbackStatus callbackStatus, short playerCount, short minPlayers, short maxPlayers, int gameLevel, int playerSkillLevel, int rulesSet, int genericField1, int genericField2, int genericField3,
			MediusWorldSecurityLevelType worldSecurityLevelType, MediusWorldStatus worldStatus, MediusGameHostType gameHostType, String gameName, byte[] gameStats, boolean endOfList) {
		super(MediusMessageType.GameList_ExtraInfoResponse0);
		this.messageId = messageId;
		this.mediusWorldId = mediusWorldId;
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
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding

			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue())); // Callback status (int)
			outputStream.write(Utils.intToBytesLittle(mediusWorldId)); // MediusWorldId (lobby/channel Id)

			outputStream.write(Utils.shortToBytesLittle(playerCount)); // Player count
			outputStream.write(Utils.shortToBytesLittle(minPlayers)); // Min players
			outputStream.write(Utils.shortToBytesLittle(maxPlayers)); // Max players
			outputStream.write(new byte[2]); // Padding

			outputStream.write(Utils.intToBytesLittle(gameLevel)); // Game level
			outputStream.write(Utils.intToBytesLittle(playerSkillLevel)); // Player skill level (first 3 bytes are disabled weapons, last byte = bolt
													// skill)
			
			outputStream.write(Utils.intToBytesLittle(rulesSet)); // Rules set (might be used as team colors or skins)
			outputStream.write(Utils.intToBytesLittle(genericField1)); // Generic field 1 (Location Id (city id; aquatos))
			outputStream.write(Utils.intToBytesLittle(genericField2)); // Generic field 2
			outputStream.write(Utils.intToBytesLittle(genericField3)); // Generic field 3 (map type, game settings)
			outputStream.write(Utils.intToBytesLittle(worldSecurityLevelType.value)); // Security level
			outputStream.write(Utils.intToBytesLittle(worldStatus.value)); // World status
			outputStream.write(Utils.intToBytesLittle(gameHostType.value)); // Game Host Type

			// GameName (contains some ID in it, remainder of the name string buffer is 0x20
			// [space character])
			logger.info(gameName);
			String s = ChatColor.strip(gameName);
			logger.info(s);
			logger.info("" + s.length());
			
			outputStream.write(Utils.buildByteArrayFromString(ChatColor.strip(gameName), MediusConstants.GAMENAME_MAXLEN.value));
			//outputStream.write(Utils.buildByteArrayFromString(ChatColor.strip(gameName), MediusConstants.GAMENAME_MAXLEN.value));

			// GameStats
			outputStream.write(gameStats);

			// End of list
			outputStream.write(Utils.intToBytesLittle(endOfList ? 1 : 0));
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}

}
