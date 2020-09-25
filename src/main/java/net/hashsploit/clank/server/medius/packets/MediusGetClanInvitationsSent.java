package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusGetClanInvitationsSent extends MediusPacket {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] start = new byte[4];
	private byte[] pageSize = new byte[4];
	
    public MediusGetClanInvitationsSent() {
        super(MediusPacketType.GetClanInvitationsSent, MediusPacketType.GetClanInvitationsSentResponse);
    }
    
    @Override
    public void read(MediusMessage mm) {
    	// Process the packet
    	ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
    	
    	buf.get(messageID);
    	buf.get(sessionKey);
    	buf.get(start);
    	buf.get(pageSize);
    	
    	logger.fine("Message ID : " + Utils.bytesToHex(messageID));
    	logger.fine("Session Key: " + Utils.bytesToHex(sessionKey));
    }
    
    @Override
    public MediusMessage write(MediusClient client) { 

    	byte[] accountName = Utils.buildByteArrayFromString("Account Name", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
       	byte[] applicationID = Utils.intToBytes(1);
       	byte[] playerStatus = Utils.intToBytes(0);
       	byte[] connectionClass = Utils.intToBytes(1);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.MediusNoResult.getValue()));	 // give no result for now		
			outputStream.write(Utils.intToBytesLittle(0)); // ignoreAccountID
			outputStream.write(Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTNAME_MAXLEN.getValue())); // IgnoreAccountName
			outputStream.write(Utils.buildByteArrayFromString("", MediusConstants.CLANMSG_MAXLEN.getValue())); // IgnoreAccountName
			outputStream.write(Utils.intToBytesLittle(0)); // clanInvitationsResponseStatus
			outputStream.write(Utils.hexStringToByteArray("01000000")); // end of list
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new MediusMessage(responseType, outputStream.toByteArray());	    
    }

}
