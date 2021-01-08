package net.hashsploit.clank.rt.handlers;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;

public class RtMsgClientCryptKeyPeerHandler extends RtMessageHandler {

	public RtMsgClientCryptKeyPeerHandler() {
		super(RtMessageId.CLIENT_CRYPTKEY_PEER);
	}

	@Override
	public void read(ByteBuf buffer) {
		
	}
	
	@Override
	public List<RTMessage> write(MediusClient client) {
		
		return null;
	}
	
}
