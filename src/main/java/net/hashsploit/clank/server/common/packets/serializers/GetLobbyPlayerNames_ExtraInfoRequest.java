package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GetLobbyPlayerNames_ExtraInfoRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];;
	private byte[] lobbyWorldID = new byte[4];
	
	public GetLobbyPlayerNames_ExtraInfoRequest(byte[] data) {
		super(MediusMessageType.GetLobbyPlayerNames_ExtraInfo, data);
		
    	ByteBuffer buf = ByteBuffer.wrap(data);
    	buf.get(messageID);
    	buf.get(new byte[3]);
    	buf.get(lobbyWorldID);
	}
	
	public String toString() {
		return "GetLobbyPlayerNames_ExtraInfoRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"lobbyWorldID: " + Utils.bytesToHex(lobbyWorldID);
	}

	public synchronized byte[] getLobbyWorldID() {
		return lobbyWorldID;
	}

	public synchronized void setLobbyWorldID(byte[] lobbyWorldID) {
		this.lobbyWorldID = lobbyWorldID;
	}

	public synchronized byte[] getMessageID() {
		return messageID;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}


	
}
