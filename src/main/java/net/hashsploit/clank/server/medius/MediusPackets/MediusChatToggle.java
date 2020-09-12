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

public class MediusChatToggle extends MediusPacket {
	
	private static final Logger logger = Logger.getLogger("");
    
    public MediusChatToggle() {
        super(MediusPacketType.ChatToggle);
    }
    
    @Override
    public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) { 
    	// Process the packet
    	logger.fine("Chat toggle data: " + Utils.bytesToHex(packetData));
    	
    	// example input where username is GGGGGGGGGGGG
    	// 0b2e0001963100474747474747474747474747000000000000003133000000000000000000007b24000000000000000000
    	byte[] response = Utils.hexStringToByteArray("019731000000000000000000000000000000000000000000000000000000");
    
		// Combine RT id and len
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, response);
		
		byte[] finalPayload = packet.toData().array();
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
        ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
        ctx.write(msg); // (1)
        ctx.flush(); // (2)
    }

}
