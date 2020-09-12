package net.hashsploit.clank.server.medius;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.ISCERTMessage;

public abstract class MediusPacket {
    
    public final MediusPacketType type;

    public MediusPacket(MediusPacketType type) {
        this.type = type;    
    }
    
    public MediusPacketType getType() {
        return type;
    }
    
    
    public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) { 

    }
    

}
	
