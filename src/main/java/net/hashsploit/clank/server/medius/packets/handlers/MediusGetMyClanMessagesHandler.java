package net.hashsploit.clank.server.medius.packets.handlers;

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
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class MediusGetMyClanMessagesHandler extends MediusPacketHandler {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] clanID = new byte[4];
	private byte[] start = new byte[4];
	private byte[] pageSize= new byte[4];    
    
	public MediusGetMyClanMessagesHandler() {
        super(MediusPacketType.GetMyClanMessages,MediusPacketType.GetMyClanMessagesResponse);
    }
    
    @Override
    public void read(MediusPacket mm) {
    	ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
    	
    	buf.get(messageID);
    	buf.get(sessionKey);
    	buf.get(clanID);
    	buf.get(start);
    	buf.get(pageSize);
    }
    
    @Override
    public MediusPacket write(MediusClient client) { 
    	// Process the packet

    	byte [] message = Utils.buildByteArrayFromString("Clan message TEST", MediusConstants.CLANMSG_MAXLEN.getValue());
    	byte [] endOfList = Utils.hexStringToByteArray("01000000");
    	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue()));		
			outputStream.write(clanID);
			outputStream.write(message);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		return new MediusPacket(responseType, outputStream.toByteArray());
    }

}