package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GenericChatFwdMessageResponse extends MediusMessage {

	
	public GenericChatFwdMessageResponse() {
		super(MediusMessageType.GenericChatFwdMessage);

	}
	
	public String toString() {
		return "GenericChatFwdMessage: \n";
	}

	
	
}
