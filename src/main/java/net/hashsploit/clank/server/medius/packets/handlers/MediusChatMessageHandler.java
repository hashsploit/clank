package net.hashsploit.clank.server.medius.packets.handlers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.packets.serializers.ChatMessageRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.medius.packets.serializers.GenericChatFwdMessageResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusChatMessageHandler extends MediusPacket {
	
	private ChatMessageRequest reqPacket;
	private GenericChatFwdMessageResponse respPacket;

    public MediusChatMessageHandler() {
        super(MediusPacketType.ChatMessage, MediusPacketType.GenericChatFwdMessage);
    }
    
    @Override
    public void read(MediusMessage mm) {
		reqPacket = new ChatMessageRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public MediusMessage write(MediusClient client) { 
    	// TODO: Send to other clients
    	return null;
    }

}
