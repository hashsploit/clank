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

public class MediusChannelList_ExtraInfoOne extends MediusPacket {
	
	private static final Logger logger = Logger.getLogger("");
    
    public MediusChannelList_ExtraInfoOne() {
        super(MediusPacketType.ChannelList_ExtraInfo1);
    }
    
    @Override
    public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) { 
    	
    	ByteBuffer buf = ByteBuffer.wrap(packetData);
    	
    	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
    	byte[] pageID = new byte[2];
    	byte[] pageSize = new byte[2];
    	
    	buf.get(messageID);
    	buf.get(pageID);
    	buf.get(pageSize);
    	
//    	byte[] mediusWorldID = Utils.intToBytesLittle(30);
//    	byte[] playerCount = Utils.shortToBytesLittle((short) 1);
//    	byte[] maxPlayers = Utils.shortToBytesLittle((short) 8);
//    	byte[] gameWorldCount = Utils.shortToBytesLittle((short) 0);
//    	byte[] worldSecurityLevelType = Utils.intToBytesLittle(0);
//    	byte[] genericField1 = Utils.intToBytesLittle(0);
//    	byte[] genericField2 = Utils.intToBytesLittle(0);
//    	byte[] genericField3 = Utils.intToBytesLittle(0);
//    	byte[] genericField4 = Utils.intToBytesLittle(0);
//    	byte[] genericFieldFilter = Utils.intToBytesLittle(0);
//    	byte[] lobbyName = Utils.buildByteArrayFromString("Veldin", MediusConstants.LOBBYNAME_MAXLEN.getValue());
//    	
////    	byte endOfList = 0x01;
//    	byte[] endOfList = Utils.hexStringToByteArray("01000000");
//    	
//		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
//		try {
//			outputStream.write(MediusPacketType.ChannelList_ExtraInfoResponse.getShortByte());
//			outputStream.write(messageID);
//			outputStream.write(Utils.hexStringToByteArray("000000"));
//			outputStream.write(Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue()));	
//			outputStream.write(mediusWorldID);
//			outputStream.write(playerCount);
//			outputStream.write(maxPlayers);
//			outputStream.write(gameWorldCount);
//			outputStream.write(Utils.hexStringToByteArray("0000"));
//			outputStream.write(worldSecurityLevelType);
//			outputStream.write(genericField1);
//			outputStream.write(genericField2);
//			outputStream.write(genericField3);
//			outputStream.write(genericField4);
//			outputStream.write(genericFieldFilter);
//			outputStream.write(lobbyName);
//			outputStream.write(endOfList);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		    	
    	//byte[] response = Utils.hexStringToByteArray("0000000100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000");
    	byte[] response = Utils.hexStringToByteArray("000000000000006B0B01000100E000000000000100000002000000000000000000000020000000435930303030303030312D303000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(MediusPacketType.ChannelList_ExtraInfoResponse.getShortByte());
			outputStream.write(messageID);
			outputStream.write(response);					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Combine RT id and len
		byte[] data = outputStream.toByteArray();
		logger.fine("LENGTH: " + Integer.toString(data.length));
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, data);
		
		byte[] finalPayload = packet.toData().array();
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
        ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
    }

}
