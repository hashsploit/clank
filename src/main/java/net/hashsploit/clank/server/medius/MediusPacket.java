package net.hashsploit.clank.server.medius;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.ISCERTMessage;
import net.hashsploit.clank.server.medius.objects.MediusMessage;

public abstract class MediusPacket {
	
	public static final Logger logger = Logger.getLogger("");

    public final MediusPacketType type;
    public final MediusPacketType responseType;

    public MediusPacket(MediusPacketType type, MediusPacketType responseType) {
        this.type = type;    
        this.responseType = responseType;
    }
    
    public MediusPacketType getType() {
        return type;
    }
    
    
    public void read(MediusMessage mm) {
    	
    }
    
    public MediusMessage write(Client client) {
    	return null;
    }
    
}
	
