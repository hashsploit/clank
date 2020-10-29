package net.hashsploit.clank.server.medius.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class MediusTextFilterHandler extends MediusPacketHandler {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] mediusTextFilterType = new byte[4];
	private byte[] text = new byte[MediusConstants.CHATMESSAGE_MAXLEN.getValue()];
	
    public MediusTextFilterHandler() {
        super(MediusPacketType.TextFilter, MediusPacketType.TextFilterResponse);
    }
    
    @Override
    public void read(MediusPacket mm) {
    	// Process the packet
    	ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
    	
    	buf.get(messageID);
    	buf.get(sessionKey);
    	buf.get(new byte[2]); // buffer
    	buf.get(mediusTextFilterType);
    	buf.get(text);
    	
    	logger.fine("Message ID : " + Utils.bytesToHex(messageID));
    	logger.fine("Session Key: " + Utils.bytesToHex(sessionKey));
    	logger.fine("Text Filter Type: " + Utils.bytesToHex(mediusTextFilterType));
    	logger.fine("Text: " + Utils.bytesToHex(text));
    	
    }
    
    @Override
    public MediusPacket write(MediusClient client) { 
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(messageID);
			outputStream.write(text);
			outputStream.write(new byte[3]);
			// TODO: this is is set to always pass, add logic
			// check the mediusTextFilterType (this should be using the object instead (TextFilterType)
			outputStream.write(Utils.intToBytesLittle(MediusCallbackStatus.MediusPass.getValue()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new MediusPacket(responseType, outputStream.toByteArray());	    
    }

}
