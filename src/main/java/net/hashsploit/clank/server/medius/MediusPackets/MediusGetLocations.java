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

public class MediusGetLocations extends MediusPacket {
	
	private static final Logger logger = Logger.getLogger("");
    
    public MediusGetLocations() {
        super(MediusPacketType.GetLocations);
    }
    
    @Override
    public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) { 
    	// Process the packet
    	logger.fine("Get my clans: " + Utils.bytesToHex(packetData));
    	
    	ByteBuffer buf = ByteBuffer.wrap(packetData);
    	
    	//byte[] finalPayload = Utils.hexStringToByteArray("0a1e00019731000000000000000000000000000000000000000000000000000000");
    	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
    	byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
    	
    	buf.get(messageID);
    	buf.get(sessionKey);

    	byte[] response = Utils.hexStringToByteArray("202020000000004368696361676F2C20494C2055534100202020476574204C6F636174696F6E7320526571756573742052656365697665642053657373696F6E6B65793A2031330000000001030000");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(MediusPacketType.GetLocationsResponse.getShortByte());
			outputStream.write(messageID);
			outputStream.write(response);				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
//    	byte[] locationID = Utils.intToBytes(10);// random location
//    	byte[] locationName = Utils.buildByteArrayFromString("Chicago", MediusConstants.LOCATIONNAME_MAXLEN.getValue());
//    	byte[] statusCode = Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue());
//    	byte[] endOfList = Utils.hexStringToByteArray("00");
//
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
//		try {
//			outputStream.write(MediusPacketType.GetLocationsResponse.getShortByte());
//			outputStream.write(messageID);
//			outputStream.write(locationID);			
//			outputStream.write(locationName);			
//			outputStream.write(statusCode);			
//			outputStream.write(endOfList);			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		// Combine RT id and len
		byte[] data = outputStream.toByteArray();
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, data);
		
		byte[] finalPayload = packet.toData().array();
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
        ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
    }

}
