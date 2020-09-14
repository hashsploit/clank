package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.ChatColor;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.scert.objects.NetConnectionType;
import net.hashsploit.clank.utils.Utils;

public class MediusJoinChannel extends MediusPacket {
	
	private static final Logger logger = Logger.getLogger("");
    
    public MediusJoinChannel() {
        super(MediusPacketType.JoinChannel);
    }
    
    @Override
    public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) { 
    	
    	ByteBuffer buf = ByteBuffer.wrap(packetData);
    	
    	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
    	byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
    	byte[] worldId = new byte[4];
    	byte[] lobbyChannelPassword = new byte[MediusConstants.LOBBYPASSWORD_MAXLEN.getValue()];
    	
    	buf.get(messageID);
    	buf.get(worldId);
    	buf.get(lobbyChannelPassword);
    	
    	
    	
    	
    	// RESPONSE
    	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] callbackStatus = Utils.intToBytesLittle(0);
		
		byte[] ipAddr = "172.16.222.5".getBytes();
		int numZeros = 16 - "172.16.222.5".length();
		String zeroString = new String(new char[numZeros]).replace("\0", "00");
		byte[] zeroTrail = Utils.hexStringToByteArray(zeroString);
		
		try {
			outputStream.write(MediusPacketType.JoinChannelResponse.getShortByte());
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);
			
			outputStream.write(Utils.hexStringToByteArray("01000000")); // net connection type (int/little endian)
			
			outputStream.write(Utils.hexStringToByteArray("01000000")); // net address type (int/little endian)
			
			outputStream.write(ipAddr); // ip address
			outputStream.write(zeroTrail); // zero padding for ip address
			
			outputStream.write(Utils.intToBytesLittle(10078)); // port
			
			outputStream.write(Utils.hexStringToByteArray("00000000")); // world id
			
			//outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000FFFFFFFF0B000000CF09ADE3EA5551113BBB519AC5BCB0CB45CD22DD399257E74E886684A12A3E68A865F487CE86777545D6CBFD90C2C6186F7D05E82419DB2E2230E7F73C")); // RSA_KEY 144
			outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")); // RSA_KEY 144
			
			outputStream.write(Utils.hexStringToByteArray("CF8BB33333323837000000000000000000")); // aSessionKey
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			
			outputStream.write(Utils.hexStringToByteArray("782B6F2F532F71443453633243364B4E")); // aAccessKey
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			
			
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
        ctx.write(msg);
        ctx.flush();
    	
    	
    }
    
    
    
    
    
}
