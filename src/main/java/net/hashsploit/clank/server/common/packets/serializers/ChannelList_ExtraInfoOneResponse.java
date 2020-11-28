package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ChannelList_ExtraInfoOneResponse extends MediusMessage {

	private byte[] messageID;
	private byte[] callbackStatus;
	private byte[] mediusWorldID;
	private byte[] playerCount;
	private byte[] maxPlayers;
	private byte[] worldSecurityLevelType;
	private byte[] genericField1;
	private byte[] genericField2;
	private byte[] genericField3;
	private byte[] genericField4;
	private byte[] genericFieldFilter;
	private byte[] lobbyName;
	private byte[] endOfList;

	
	public ChannelList_ExtraInfoOneResponse(byte[] messageID, byte[] callbackStatus, byte[] mediusWorldID, byte[] playerCount, byte[] maxPlayers, 
				byte[] worldSecurityLevelType, byte[] genericField1, byte[] genericField2, byte[] genericField3, byte[] genericField4, 
				byte[] genericFieldFilter, byte[] lobbyName, byte[] endOfList) {
		super(MediusMessageType.ChannelList_ExtraInfoResponse);
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.mediusWorldID = mediusWorldID;
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
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));//padding
			outputStream.write(callbackStatus);	
			outputStream.write(mediusWorldID);
			outputStream.write(playerCount);
			outputStream.write(maxPlayers);
			outputStream.write(worldSecurityLevelType);
			outputStream.write(genericField1);
			outputStream.write(genericField2);
			outputStream.write(genericField3);
			outputStream.write(genericField4);
			outputStream.write(genericFieldFilter);
			outputStream.write(lobbyName);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "ChannelList_ExtraInfoOneResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"mediusWorldID: " + Utils.bytesToHex(mediusWorldID) + '\n' + 
				"playerCount: " + Utils.bytesToHex(playerCount) + '\n' + 
				"maxPlayers: " + Utils.bytesToHex(maxPlayers) + '\n' + 
				"worldSecurityLevelType: " + Utils.bytesToHex(worldSecurityLevelType) + '\n' + 
				"genericField1: " + Utils.bytesToHex(genericField1) + '\n' + 
				"genericField2: " + Utils.bytesToHex(genericField2) + '\n' + 
				"genericField3: " + Utils.bytesToHex(genericField3) + '\n' + 
				"genericField4: " + Utils.bytesToHex(genericField4) + '\n' + 
				"genericFieldFilter: " + Utils.bytesToHex(genericFieldFilter) + '\n' + 
				"lobbyName: " + Utils.bytesToHex(lobbyName) + '\n' + 
				"endOfList: " + Utils.bytesToHex(endOfList);
	}
	
	
	
}
