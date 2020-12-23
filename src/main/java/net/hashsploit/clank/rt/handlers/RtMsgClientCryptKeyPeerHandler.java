package net.hashsploit.clank.rt.handlers;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RtMessageId;

public class RtMsgClientCryptKeyPeerHandler extends RtMessageHandler {

	public RtMsgClientCryptKeyPeerHandler(MediusClient client) {
		super(client, RtMessageId.CLIENT_CRYPTKEY_PEER);
	}

	@Override
	public void read(ByteBuf buffer) {
		
	}
	
	@Override
	public void write(ByteBuf buffer) {
		
	}
	
}
