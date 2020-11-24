package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusApplicationType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.objects.MediusWorldStatus;
import net.hashsploit.clank.utils.Utils;

public class FindWorldByNameResponse extends MediusPacket {

	private byte[] messageId;
	private MediusCallbackStatus callbackStatus;
	private int appId;
	private String appName;
	private MediusApplicationType appType;
	private int worldId;
	private String worldName;
	private MediusWorldStatus worldStatus;
	private boolean endOfList;

	public FindWorldByNameResponse(byte[] messageId, MediusCallbackStatus callbackStatus, int appId, String appName, MediusApplicationType appType, int worldId, String worldName, MediusWorldStatus worldStatus, boolean endOfList) {
		super(MediusPacketType.FindWorldByNameResponse);

		this.messageId = messageId;
		this.callbackStatus = callbackStatus;
		this.appId = appId;
		this.appName = appName;
		this.appType = appType;
		this.worldId = worldId;
		this.worldName = worldName;
		this.worldStatus = worldStatus;
		this.endOfList = endOfList;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
			outputStream.write(Utils.intToBytesLittle(appId));
			outputStream.write(Utils.buildByteArrayFromString(appName, MediusConstants.APPNAME_MAXLEN.getValue()));
			outputStream.write(Utils.intToBytesLittle(appType.getValue()));
			outputStream.write(Utils.intToBytesLittle(worldId));
			outputStream.write(Utils.buildByteArrayFromString(worldName, MediusConstants.WORLDNAME_MAXLEN.getValue()));
			outputStream.write(Utils.intToBytesLittle(worldStatus.getValue()));
			outputStream.write(Utils.hexStringToByteArray(endOfList ? "01" : "00")); // EndOfList (char) 00 or 01
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
}