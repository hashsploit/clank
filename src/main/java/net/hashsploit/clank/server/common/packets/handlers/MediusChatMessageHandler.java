package net.hashsploit.clank.server.common.packets.handlers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.ChatMessageRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.common.packets.serializers.GenericChatFwdMessageResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusChatMessageHandler extends MediusPacketHandler {
	
	private ChatMessageRequest reqPacket;
	private GenericChatFwdMessageResponse respPacket;

    public MediusChatMessageHandler() {
        super(MediusMessageType.ChatMessage, MediusMessageType.GenericChatFwdMessage);
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
