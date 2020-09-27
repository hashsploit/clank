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

public class MediusPlayerReport extends MediusPacket {
	
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] mediusWorldID = new byte[4];
	private byte[] stats = new byte[MediusConstants.ACCOUNTSTATS_MAXLEN.getValue()];
	
    public MediusPlayerReport() {
        super(MediusPacketType.PlayerReport, null);
    }
    
    @Override
    public void read(MediusMessage mm) {
    	// Process the packet
    	ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
    	
    	buf.get(sessionKey);
    	buf.get(new byte[3]); // buffer
    	buf.get(mediusWorldID);
    	buf.get(stats);

    }
    
    @Override
    public MediusMessage write(MediusClient client) { 
    	return null;
    }

}
