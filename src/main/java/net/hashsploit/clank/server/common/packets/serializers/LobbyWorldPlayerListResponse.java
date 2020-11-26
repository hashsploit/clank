package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusConnectionType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;
import net.hashsploit.clank.utils.Utils;

public class LobbyWorldPlayerListResponse extends MediusMessage {

	private byte[] messageId;
	private MediusCallbackStatus callbackStatus;
	private MediusPlayerStatus playerStatus;
	private int accountId;
	private String accountName;
	private byte[] stats;
	private MediusConnectionType connectionType;
	private boolean endOfList;

	public LobbyWorldPlayerListResponse(byte[] messageId, MediusCallbackStatus callbackStatus, MediusPlayerStatus playerStatus, int accountId, String accountName, byte[] stats, MediusConnectionType connectionType, boolean endOfList) {
		super(MediusMessageType.LobbyWorldPlayerListResponse);

		this.messageId = messageId;
		this.callbackStatus = callbackStatus;
		this.playerStatus = playerStatus;
		this.accountId = accountId;
		this.accountName = accountName;
		this.stats = stats;
		this.connectionType = connectionType;
		this.endOfList = endOfList;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
			outputStream.write(Utils.intToBytesLittle(playerStatus.getValue()));
			outputStream.write(Utils.intToBytesLittle(accountId));
			outputStream.write(Utils.buildByteArrayFromString(accountName, MediusConstants.ACCOUNTNAME_MAXLEN.getValue()));
			outputStream.write(stats);
			outputStream.write(Utils.intToBytesLittle(connectionType.getValue()));
			outputStream.write(Utils.hexStringToByteArray(endOfList ? "01" : "00"));
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
}