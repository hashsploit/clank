package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GetLobbyPlayerNames_ExtraInfoRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];;
	private byte[] lobbyWorldId = new byte[4];
	
	public GetLobbyPlayerNames_ExtraInfoRequest(byte[] data) {
		super(MediusMessageType.GetLobbyPlayerNames_ExtraInfo, data);
		
    	ByteBuffer buf = ByteBuffer.wrap(data);
    	buf.get(messageId);
    	buf.get(new byte[3]);
    	buf.get(lobbyWorldId);
	}
	
	public String toString() {
		return "GetLobbyPlayerNames_ExtraInfoRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageId) + '\n' + 
				"lobbyWorldID: " + Utils.bytesToHex(lobbyWorldId);
	}

	public synchronized byte[] getLobbyWorldId() {
		return lobbyWorldId;
	}

	public synchronized void setLobbyWorldID(byte[] lobbyWorldID) {
		this.lobbyWorldId = lobbyWorldID;
	}

	public synchronized byte[] getMessageID() {
		return messageId;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageId = messageID;
	}


	
}
