package net.hashsploit.clank.server.common.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class MediusGetIgnoreListHandler extends MediusPacketHandler {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	
    public MediusGetIgnoreListHandler() {
        super(MediusMessageType.GetIgnoreList, MediusMessageType.GetIgnoreListResponse);
    }
    
    @Override
    public void read(MediusPacket mm) {
    	// Process the packet
    	ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
    	
    	buf.get(messageID);
    	buf.get(sessionKey);
    	
    	logger.fine("Message ID : " + Utils.bytesToHex(messageID));
    	logger.fine("Session Key: " + Utils.bytesToHex(sessionKey));
    }
    
    @Override
    public MediusPacket write(MediusClient client) { 

    	byte[] accountName = Utils.buildByteArrayFromString("Account Name", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
       	byte[] applicationID = Utils.intToBytes(1);
       	byte[] playerStatus = Utils.intToBytes(0);
       	byte[] connectionClass = Utils.intToBytes(1);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.NO_RESULT.getValue()));	 // give no result for now		
			outputStream.write(Utils.intToBytesLittle(0)); // ignoreAccountID
			outputStream.write(Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTNAME_MAXLEN.getValue())); // IgnoreAccountName
			outputStream.write(Utils.hexStringToByteArray("01000000")); // end of list
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new MediusPacket(responseType, outputStream.toByteArray());	    
    }

}
