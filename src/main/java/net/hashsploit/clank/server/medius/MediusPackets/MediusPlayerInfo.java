package net.hashsploit.clank.server.medius.MediusPackets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.utils.Utils;

public class MediusPlayerInfo extends MediusPacket {
	
	private static final Logger logger = Logger.getLogger("");
    
    public MediusPlayerInfo() {
        super(MediusPacketType.PlayerInfo);
    }
    
    @Override
    public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) { 
    	// Process the packet
    	
    	ByteBuffer buf = ByteBuffer.wrap(packetData);
    	
    	//byte[] finalPayload = Utils.hexStringToByteArray("0a1e00019731000000000000000000000000000000000000000000000000000000");
    	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
    	byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
    	byte[] accountID = new byte[4];
    	
    	buf.get(messageID);
    	buf.get(sessionKey);
    	buf.get(accountID);
    	
    	logger.fine("Message ID : " + Utils.bytesToHex(messageID));
    	logger.fine("Session Key: " + Utils.bytesToHex(sessionKey));
    	logger.fine("Account ID: " + Utils.bytesToHex(accountID));
    	
    	byte[] accountName = Utils.buildByteArrayFromString("Account Name", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
       	byte[] applicationID = Utils.intToBytes(1);
       	byte[] playerStatus = Utils.intToBytes(0);
       	byte[] connectionClass = Utils.intToBytes(1);
       	
//       	byte[] leaderAccountID = Utils.intToBytes(1);
//    	byte[] stats = Utils.buildByteArrayFromString("0", MediusConstants.CLANSTATS_MAXLEN.getValue());
//    	byte[] clanStatus = Utils.intToBytes(0);
//    	byte[] endOfList = Utils.hexStringToByteArray("00");
    	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(MediusPacketType.PlayerInfoResponse.getShortByte());
			outputStream.write(messageID);
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue()));			
			outputStream.write(accountName);
			outputStream.write(applicationID);
			outputStream.write(playerStatus);
			outputStream.write(connectionClass);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] response = outputStream.toByteArray();
	    
		// Combine RT id and len
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, response);
		
		byte[] finalPayload = packet.toData().array();
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
        ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
    }

}
