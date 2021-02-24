package net.hashsploit.clank.rt.handlers;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.rt.serializers.RT_ClientEcho;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;

public class RtMsgClientEcho extends RtMessageHandler {

	private RT_ClientEcho reqPacket;
	
	public RtMsgClientEcho() {
		super(RtMessageId.CLIENT_ECHO);
	}

	@Override
	public void read(ByteBuf buffer) {
		reqPacket = new RT_ClientEcho(buffer);		
	}
	
	@Override
	public List<RTMessage> write(MediusClient client) {
		List<RTMessage> responses = new ArrayList<RTMessage>();
		responses.add(reqPacket);
		return responses;
	}
	
}
