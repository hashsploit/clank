package net.hashsploit.clank.server.common;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.ISCERTMessage;

public abstract class MediusPacketHandler {
	
	public static final Logger logger = Logger.getLogger("");

    public final MediusMessageType type;
    public final MediusMessageType responseType;

    public MediusPacketHandler(MediusMessageType type, MediusMessageType responseType) {
        this.type = type;    
        this.responseType = responseType;
    }
    
    public MediusMessageType getType() {
        return type;
    }
    
    
    public void read(MediusPacket mm) {
    	
    }
    
    public MediusPacket write(MediusClient client) {
    	return null;
    }
    
}
	
