package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.GameHostType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.utils.Utils;

public class GameListResponse extends MediusMessage {

	private byte[] messageID;
	private MediusCallbackStatus callbackStatus;
	private int mediusWorldId;
	private String gameName;
	private MediusWorldStatus worldStatus;
	private GameHostType gameHostType;
	private int playerCount;
	private boolean endOfList;

	public GameListResponse(byte[] messageID, MediusCallbackStatus callbackStatus, int mediusWorldId, String gameName, MediusWorldStatus worldStatus, GameHostType gameHostType, int playerCount, boolean endOfList) {
		super(MediusMessageType.GameListResponse);

		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.mediusWorldId = mediusWorldId;
		this.gameName = gameName;
		this.worldStatus = worldStatus;
		this.gameHostType = gameHostType;
		this.playerCount = playerCount;
		this.endOfList = endOfList;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
			outputStream.write(Utils.intToBytesLittle(mediusWorldId));
			outputStream.write(Utils.buildByteArrayFromString(gameName, MediusConstants.GAMENAME_MAXLEN.getValue()));
			outputStream.write(Utils.intToBytesLittle(worldStatus.getValue()));
			outputStream.write(Utils.intToBytesLittle(gameHostType.getValue()));
			outputStream.write(Utils.intToBytesLittle(playerCount));
			outputStream.write(Utils.hexStringToByteArray(endOfList ? "01" : "00")); // EndOfList (char) 00 or 01
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
}
