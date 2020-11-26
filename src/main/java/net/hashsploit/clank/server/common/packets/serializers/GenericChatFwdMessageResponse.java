package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class GenericChatFwdMessageResponse extends MediusPacket {

	
	public GenericChatFwdMessageResponse() {
		super(MediusMessageType.GenericChatFwdMessage);

	}
	
	public String toString() {
		return "GenericChatFwdMessage: \n";
	}

	
	
}
