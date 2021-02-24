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

public class GameListResponse extends MediusMessage {

	private byte[] messageID;
	private MediusCallbackStatus callbackStatus;
	private int mediusWorldId;
	private String gameName;
	private MediusWorldStatus worldStatus;
	private MediusGameHostType gameHostType;
	private int playerCount;
	private boolean endOfList;

	public GameListResponse(byte[] messageID, MediusCallbackStatus callbackStatus, int mediusWorldId, String gameName, MediusWorldStatus worldStatus, MediusGameHostType gameHostType, int playerCount, boolean endOfList) {
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
			outputStream.write(Utils.buildByteArrayFromString(gameName, MediusConstants.GAMENAME_MAXLEN.value));
			outputStream.write(Utils.intToBytesLittle(worldStatus.value));
			outputStream.write(Utils.intToBytesLittle(gameHostType.value));
			outputStream.write(Utils.intToBytesLittle(playerCount));
			
			outputStream.write(Utils.intToBytesLittle(endOfList ? 1 : 0));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
}
