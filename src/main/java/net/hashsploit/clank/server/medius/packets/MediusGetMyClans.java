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

public class MediusGetMyClans extends MediusPacket {
	
	private static final Logger logger = Logger.getLogger("");
    
    public MediusGetMyClans() {
        super(MediusPacketType.GetMyClans);
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
    	
    	logger.fine("Message ID : " + Utils.bytesToHex(messageID));
    	logger.fine("Session Key: " + Utils.bytesToHex(sessionKey));
    	
    	/*
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
        ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
        */
    	
    	byte[] statusCode = Utils.intToBytesLittle(0);
    	byte[] clanID = Utils.intToBytesLittle(5);
    	byte[] applicationID = Utils.intToBytesLittle(1);
    	byte[] clanName = Utils.buildByteArrayFromString("Test clan name", MediusConstants.CLANNAME_MAXLEN.getValue()); // 32
    	byte[] leaderAccountID = Utils.intToBytesLittle(1);
    	byte[] leaderAccountName = Utils.buildByteArrayFromString("LeaderAccountName", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
    	byte[] stats = Utils.buildByteArrayFromString("0", MediusConstants.CLANSTATS_MAXLEN.getValue());
    	byte[] clanStatus = Utils.intToBytesLittle(MediusCallbackStatus.MediusSuccess.getValue());
    	//byte[] endOfList = Utils.hexStringToByteArray("01000000");
    	byte endOfList = 0x01;
    	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(MediusPacketType.GetMyClansResponse.getShortByte());
			outputStream.write(messageID); // 21
			outputStream.write(Utils.hexStringToByteArray("000000")); // 3
			outputStream.write(statusCode);	// 4
			outputStream.write(clanID); // 4
			outputStream.write(applicationID); // 4
			outputStream.write(clanName); // 32
			outputStream.write(leaderAccountID); // 4
			outputStream.write(leaderAccountName); // 32
			outputStream.write(stats); // 256
			outputStream.write(clanStatus); // 4
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Combine RT id and len
		byte[] data = outputStream.toByteArray();
		logger.fine("Data: " + Utils.bytesToHex(data));
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, data);
		
		byte[] finalPayload = packet.toData().array();
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
        ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
    }

}
