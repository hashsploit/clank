package net.hashsploit.clank.rt.handlers;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RtMessageId;

public class RtMsgClientConnectTcp extends RtMessageHandler {

	public RtMsgClientConnectTcp(MediusClient client) {
		super(client, RtMessageId.CLIENT_CONNECT_TCP);
	}

	@Override
	public void read(ByteBuf buffer) {
		
	}
	
	@Override
	public void write(ByteBuf buffer) {
		
	}
	
}
