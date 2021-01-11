package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.ChatColor;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ChannelList_ExtraInfoOneResponse extends MediusMessage {

	private byte[] messageId;
	private MediusCallbackStatus callbackStatus;
	private int mediusWorldId;
	private int playerCount;
	private int maxPlayers;
	private int worldSecurityLevelType;
	private byte[] genericField1;
	private byte[] genericField2;
	private byte[] genericField3;
	private byte[] genericField4;
	private byte[] genericFieldFilter;
	private String lobbyName;
	private boolean endOfList;

	
	public ChannelList_ExtraInfoOneResponse(byte[] messageId, MediusCallbackStatus callbackStatus, int mediusWorldId, int playerCount, int maxPlayers, 
				int worldSecurityLevelType, byte[] genericField1, byte[] genericField2, byte[] genericField3, byte[] genericField4, 
				byte[] genericFieldFilter, String lobbyName, boolean endOfList) {
		super(MediusMessageType.ChannelList_ExtraInfoResponse);
		this.messageId = messageId;
		this.callbackStatus = callbackStatus;
		this.mediusWorldId = mediusWorldId;
		this.playerCount = playerCount;
		this.maxPlayers = maxPlayers;
		this.worldSecurityLevelType = worldSecurityLevelType;
		this.genericField1 = genericField1;
		this.genericField2 = genericField2;
		this.genericField3 = genericField3;
		this.genericField4 = genericField4;
		this.genericFieldFilter = genericFieldFilter;
		this.lobbyName = lobbyName;
		this.endOfList = endOfList;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageId);
			outputStream.write(new byte[3]);
			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
			outputStream.write(Utils.intToBytesLittle(mediusWorldId));
			outputStream.write(Utils.intToBytesLittle(playerCount));
			outputStream.write(Utils.intToBytesLittle(maxPlayers));
			outputStream.write(Utils.intToBytesLittle(worldSecurityLevelType));
			outputStream.write(genericField1);
			outputStream.write(genericField2);
			outputStream.write(genericField3);
			outputStream.write(genericField4);
			outputStream.write(genericFieldFilter);
			outputStream.write(Utils.padByteArray(ChatColor.parse(lobbyName), MediusConstants.LOBBYNAME_MAXLEN.value));
			outputStream.write(Utils.intToBytesLittle(endOfList ? 1 : 0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return outputStream.toByteArray();
	}

}
