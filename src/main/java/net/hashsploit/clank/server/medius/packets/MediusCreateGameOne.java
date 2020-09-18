package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusCreateGameOne extends MediusPacket {
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] buffer = new byte[2];
	private byte[] minPlayers = new byte[4];
	private byte[] maxPlayers = new byte[4];
	private byte[] previousLobbyID = new byte[4];
	
    public MediusCreateGameOne() {
        super(MediusPacketType.CreateGame1,MediusPacketType.CreateGameResponse);
    }
    
    @Override
    public void read(MediusMessage mm) {
    	ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
    	/*
    	 * 
    	 * bc290000 // application id
			00000000 // min players
			08000000 // max players
			45070000 // game level
    	 * 
    	 */
    	buf.get(messageID);
    	buf.get(sessionKey);
    	buf.get(buffer);
    	buf.get(minPlayers);
    	buf.get(maxPlayers);
    	buf.get(previousLobbyID);
    }
    
    @Override
    public MediusMessage write(Client client) {
    	byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.MediusSuccess.getValue());
    	byte[] newLobbyID = Utils.intToBytesLittle(123);
    	
		logger.finest("Writing CreateGameOne OUT:");
		logger.finest("MessageID : " + Utils.bytesToHex(messageID) + " | Length: " + Integer.toString(messageID.length));
		logger.finest("newlobbyID : " + Utils.bytesToHex(newLobbyID) + " | Length: " + Integer.toString(newLobbyID.length));
		logger.finest("callbackstatus : " + Utils.bytesToHex(callbackStatus) + " | Length: " + Integer.toString(callbackStatus.length));
    	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue()));	
			outputStream.write(newLobbyID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new MediusMessage(responseType, outputStream.toByteArray());
    }

}
