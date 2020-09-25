package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusChannelList_ExtraInfoOne extends MediusPacket {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] pageID = new byte[2];
	private byte[] pageSize = new byte[2];
	
	
    public MediusChannelList_ExtraInfoOne() {
        super(MediusPacketType.ChannelList_ExtraInfo1, MediusPacketType.ChannelList_ExtraInfoResponse);
    }
    
    @Override
    public void read(MediusMessage mm) {
    	ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
    	buf.get(messageID);
    	buf.get(pageID);
    	buf.get(pageSize);
    }
    
    @Override
    public MediusMessage write(MediusClient client) {
    	byte[] mediusWorldID = Utils.hexStringToByteArray("45070000");
    	byte[] playerCount = Utils.shortToBytesLittle((short) 1);
    	byte[] maxPlayers = Utils.shortToBytesLittle((short) 224);
    	byte[] worldSecurityLevelType = Utils.intToBytesLittle(0);
    	byte[] genericField1 = Utils.intToBytesLittle(1);
    	byte[] genericField2 = Utils.intToBytesLittle(1);
    	byte[] genericField3 = Utils.intToBytesLittle(0);
    	byte[] genericField4 = Utils.intToBytesLittle(0);
    	byte[] genericFieldFilter = Utils.intToBytesLittle(32);
    	byte[] lobbyName = Utils.buildByteArrayFromString("CY00000000-00", MediusConstants.LOBBYNAME_MAXLEN.getValue());
    	
		logger.finest("Writing ChannelList_ExtraInfo1 OUT:");
		logger.finest("mediusWorldID : " + Utils.bytesToHex(mediusWorldID) + " | Length: " + Integer.toString(mediusWorldID.length));
		logger.finest("playerCount : " + Utils.bytesToHex(playerCount) + " | Length: " + Integer.toString(playerCount.length));
		logger.finest("maxPlayers : " + Utils.bytesToHex(maxPlayers) + " | Length: " + Integer.toString(maxPlayers.length));
		logger.finest("worldSecurityLevelType : " + Utils.bytesToHex(worldSecurityLevelType) + " | Length: " + Integer.toString(worldSecurityLevelType.length));
		logger.finest("genericField1 : " + Utils.bytesToHex(genericField1) + " | Length: " + Integer.toString(genericField1.length));
		logger.finest("genericField2 : " + Utils.bytesToHex(genericField2) + " | Length: " + Integer.toString(genericField2.length));
		logger.finest("genericField3 : " + Utils.bytesToHex(genericField3) + " | Length: " + Integer.toString(genericField3.length));
		logger.finest("genericField4 : " + Utils.bytesToHex(genericField4) + " | Length: " + Integer.toString(genericField4.length));
		logger.finest("lobbyName : " + Utils.bytesToHex(lobbyName) + " | Length: " + Integer.toString(lobbyName.length));
		
    	byte[] endOfList = Utils.hexStringToByteArray("01000000");
    	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue()));	
			outputStream.write(mediusWorldID);
			outputStream.write(playerCount);
			outputStream.write(maxPlayers);
			outputStream.write(worldSecurityLevelType);
			outputStream.write(genericField1);
			outputStream.write(genericField2);
			outputStream.write(genericField3);
			outputStream.write(genericField4);
			outputStream.write(genericFieldFilter);
			outputStream.write(lobbyName);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new MediusMessage(responseType, outputStream.toByteArray());
    }
}
