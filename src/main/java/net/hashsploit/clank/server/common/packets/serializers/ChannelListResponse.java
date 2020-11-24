package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class ChannelListResponse extends MediusPacket {

	private byte[] messageId;
	private MediusCallbackStatus callbackStatus;
	private int mediusWorldId;
	private String lobbyName;
	private int playerCount;
	private boolean endOfList;

	public ChannelListResponse(byte[] messageId, MediusCallbackStatus callbackStatus, int mediusWorldId, String lobbyName, int playerCount, boolean endOfList) {
		super(MediusPacketType.ChannelListResponse);

		this.messageId = messageId;
		this.callbackStatus = callbackStatus;
		this.mediusWorldId = mediusWorldId;
		this.lobbyName = lobbyName;
		this.playerCount = playerCount;
		this.endOfList = endOfList;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
			outputStream.write(Utils.intToBytesLittle(mediusWorldId));
			outputStream.write(Utils.buildByteArrayFromString(lobbyName, MediusConstants.LOBBYNAME_MAXLEN.getValue()));
			outputStream.write(Utils.intToBytesLittle(playerCount));
			outputStream.write(Utils.hexStringToByteArray(endOfList ? "01" : "00")); // EndOfList (char) 00 or 01
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
}
