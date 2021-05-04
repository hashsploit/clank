package net.hashsploit.clank.rt.handlers;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.rt.serializers.RT_ServerApp;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;

public class RtMsgServerApp extends RtMessageHandler {

	private RT_ServerApp reqPacket;
	
	public RtMsgServerApp() {
		super(RtMessageId.SERVER_APP);
	}

	@Override
	public void read(ByteBuf buffer) {
		reqPacket = new RT_ServerApp(buffer);		
	}
	
	@Override
	public List<RTMessage> write(MediusClient client) {
		
		List<RTMessage> responses = new ArrayList<RTMessage>();
		
		// Detect which medius packet is being parsed
		MediusPacketHandler mediusPacket = client.getMediusMessageMap().get(reqPacket.getMediusMessageType());

		// Process this medius packet
		mediusPacket.read(client, new MediusMessage(reqPacket.getMediusPayload()));
		mediusPacket.write(client);
		
		return responses;
	}
	
}
