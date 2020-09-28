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

public class MediusGameWorldPlayerList extends MediusPacket {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] worldID = new byte[4];

    public MediusGameWorldPlayerList() {
        super(MediusPacketType.GameWorldPlayerList,MediusPacketType.GameWorldPlayerListResponse);
    }
    
    @Override
    public void read(MediusMessage mm) {
    	ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
    	buf.get(messageID);
    	buf.get(sessionKey);
    	buf.get(new byte[2]);
    	buf.get(worldID);
    	
    	logger.finest("MediusGameWorldPlayerList inputs:");
    	logger.finest("worldID: " + Utils.bytesToHex(worldID));
    }
    
    @Override
    public MediusMessage write(MediusClient client) {
    	byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.MediusSuccess.getValue());
    	byte[] accountID = Utils.intToBytesLittle(5);
    	byte[] accountName = Utils.buildByteArrayFromString("Smily", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
    	byte[] stats = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTSTATS_MAXLEN.getValue());
    	byte[] connectionClass = Utils.intToBytesLittle(1);
    	byte[] endOfList = Utils.hexStringToByteArray("01000000");
    	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			outputStream.write(callbackStatus);	
			outputStream.write(accountID);
			outputStream.write(accountName);
			outputStream.write(stats);
			outputStream.write(connectionClass);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new MediusMessage(responseType, outputStream.toByteArray());
    }

}
