package net.hashsploit.clank.server.medius.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GenericChatFwdMessageResponse extends MediusMessage {

	
	public GenericChatFwdMessageResponse() {
		super(MediusPacketType.GenericChatFwdMessage);

	}
	
	public String toString() {
		return "GenericChatFwdMessage: \n";
	}

	
	
}
