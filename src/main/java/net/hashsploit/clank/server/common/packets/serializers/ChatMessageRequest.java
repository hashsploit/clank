package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ChatMessageRequest extends MediusMessage {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] messageType = new byte[4];
	private byte[] targetID = new byte[4];
	private byte[] text = new byte[MediusConstants.CHATMESSAGE_MAXLEN.getValue()];
	
	public ChatMessageRequest(byte[] data) {
		super(MediusMessageType.ChatMessage, data);
		
    	// Process the packet
    	ByteBuffer buf = ByteBuffer.wrap(data);
    	
    	buf.get(messageID);
    	buf.get(sessionKey);
    	buf.get(new byte[2]); // buffer
    	buf.get(messageType);
    	buf.get(targetID);
    	buf.get(text);
	}
	
	public String getDebugString() {
		return Utils.generateDebugPacketString(ChatMessageRequest.class.getName(),
			new String[] {
				"messageId",
				"sessionKey",
				"messageType",
				"targetId",
				"text"
			},
			new String[] {
				Utils.bytesToHex(messageID),
				Utils.bytesToHex(sessionKey),
				Utils.bytesToHex(messageType),
				Utils.bytesToHex(targetID),
				Utils.bytesToString(text)
			}
		);
	}
	
	@Override
	public String toString() {
		return "ChatMessageRequest: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"messageType: " + Utils.bytesToHex(messageType) + '\n' + 
				"targetID: " + Utils.bytesToHex(targetID) + '\n' + 
				"text: " + Utils.bytesToHex(text);		
	}

	public synchronized byte[] getMessageID() {
		return messageID;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}

	public synchronized byte[] getSessionKey() {
		return sessionKey;
	}

	public synchronized void setSessionKey(byte[] sessionKey) {
		this.sessionKey = sessionKey;
	}

	public synchronized byte[] getMessageType() {
		return messageType;
	}

	public synchronized void setMessageType(byte[] messageType) {
		this.messageType = messageType;
	}

	public synchronized byte[] getTargetID() {
		return targetID;
	}

	public synchronized void setTargetID(byte[] targetID) {
		this.targetID = targetID;
	}

	public synchronized byte[] getText() {
		return text;
	}

	public synchronized void setText(byte[] text) {
		this.text = text;
	}
	

	
}
