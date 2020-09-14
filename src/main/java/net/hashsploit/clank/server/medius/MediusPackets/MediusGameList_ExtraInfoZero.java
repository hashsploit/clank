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

public class MediusGameList_ExtraInfoZero extends MediusPacket {
	
	private static final Logger logger = Logger.getLogger("");
    
    public MediusGameList_ExtraInfoZero() {
        super(MediusPacketType.GameList_ExtraInfo0);
    }
    
    @Override
    public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) { 
    	// Process the packet
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			logger.fine("Error sleeping...");
			e1.printStackTrace();
		}
    	
    	ByteBuffer buf = ByteBuffer.wrap(packetData);
    	
    	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
    	byte[] pageID = new byte[2];
    	byte[] pageSize = new byte[2];
    	
    	buf.get(messageID);
    	buf.get(pageID);
    	buf.get(pageSize);
    	
    	byte[] mediusWorldID = Utils.intToBytesLittle(40);
    	byte[] playerCount = Utils.shortToBytesLittle((short) 1);
    	byte[] minPlayers = Utils.shortToBytesLittle((short) 1);
    	byte[] maxPlayers = Utils.shortToBytesLittle((short) 8);
    	byte[] gameLevel = Utils.intToBytesLittle(1);
    	byte[] playerSkillLevel = Utils.intToBytesLittle(1);
    	byte[] rulesSet = Utils.intToBytesLittle(0);
    	byte[] genericField1 = Utils.intToBytesLittle(0);
    	byte[] genericField2 = Utils.intToBytesLittle(0);
    	byte[] genericField3 = Utils.intToBytesLittle(0);
    	byte[] genericField4 = Utils.intToBytesLittle(0);
    	byte[] genericField5 = Utils.intToBytesLittle(0);
    	byte[] genericField6 = Utils.intToBytesLittle(0);
    	byte[] genericField7 = Utils.intToBytesLittle(0);
    	byte[] genericField8 = Utils.intToBytesLittle(0);
    	byte[] worldSecurityLevelType = Utils.intToBytesLittle(1);
    	byte[] worldStatus = Utils.intToBytesLittle(1);
    	byte[] gameHostType = Utils.intToBytesLittle(0);
    	byte[] gameName = Utils.buildByteArrayFromString("1v1 f.o dox", MediusConstants.GAMENAME_MAXLEN.getValue());
    	byte[] gameStats = Utils.buildByteArrayFromString("game stats", MediusConstants.GAMESTATS_MAXLEN.getValue());
    	byte endOfList = 0x01;
    	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(MediusPacketType.GameList_ExtraInfoResponse0.getShortByte());
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue()));	
			outputStream.write(mediusWorldID);
			outputStream.write(playerCount);
			outputStream.write(minPlayers);
			outputStream.write(maxPlayers);
			outputStream.write(gameLevel);
			outputStream.write(playerSkillLevel);
			outputStream.write(rulesSet);
			outputStream.write(genericField1);
			outputStream.write(genericField2);
			outputStream.write(genericField3);
			outputStream.write(genericField4);
			outputStream.write(genericField5);
			outputStream.write(genericField6);
			outputStream.write(genericField7);
			outputStream.write(genericField8);
			outputStream.write(worldSecurityLevelType);
			outputStream.write(worldStatus);
			outputStream.write(gameHostType);
			outputStream.write(gameName);
			outputStream.write(gameStats);
			outputStream.write(endOfList);
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
