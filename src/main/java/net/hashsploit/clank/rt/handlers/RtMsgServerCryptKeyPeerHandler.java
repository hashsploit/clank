package net.hashsploit.clank.rt.handlers;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.rt.serializers.RT_ServerCryptKeyPeer;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;

public class RtMsgServerCryptKeyPeerHandler extends RtMessageHandler {
	
	private RT_ServerCryptKeyPeer reqPacket;
	
	
	public RtMsgServerCryptKeyPeerHandler() {
		super(RtMessageId.SERVER_CRYPTKEY_PEER);
	}

	@Override
	public void read(ByteBuf buffer) {
		reqPacket = new RT_ServerCryptKeyPeer(buffer);
	}
	
	@Override
	public List<RTMessage> write(MediusClient client) {
		
		return null;
	}
	
}
