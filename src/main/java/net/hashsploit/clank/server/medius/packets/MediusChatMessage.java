package net.hashsploit.clank.server.medius.packets;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusChatMessage extends MediusPacket {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] messageType = new byte[4];
	private byte[] targetID = new byte[4];
	private byte[] text = new byte[MediusConstants.CHATMESSAGE_MAXLEN.getValue()];
	
    public MediusChatMessage() {
        super(MediusPacketType.ChatMessage, MediusPacketType.GenericChatFwdMessage);
    }
    
    @Override
    public void read(MediusMessage mm) {
    	// Process the packet
    	ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
    	
    	buf.get(messageID);
    	buf.get(sessionKey);
    	buf.get(new byte[2]); // buffer
    	buf.get(messageType);
    	buf.get(targetID);
    	buf.get(text);
    	
    	logger.fine("Message ID : " + Utils.bytesToHex(messageID));
    	logger.fine("Session Key: " + Utils.bytesToHex(sessionKey));
    	logger.fine("Text: " + Utils.bytesToHex(text));
    	
    }
    
    @Override
    public MediusMessage write(MediusClient client) { 
    	// TODO: Send to other clients
    	return null;
    }

}
