package net.hashsploit.clank.rt;

import java.util.HashMap;

import net.hashsploit.clank.rt.handlers.RtMsgClientAppToServer;
import net.hashsploit.clank.rt.handlers.RtMsgClientConnectTcp;
import net.hashsploit.clank.rt.handlers.RtMsgClientCryptKeyPublicHandler;
import net.hashsploit.clank.rt.handlers.RtMsgClientDisconnect;
import net.hashsploit.clank.rt.handlers.RtMsgClientEcho;
import net.hashsploit.clank.rt.handlers.RtMsgClientHello;
import net.hashsploit.clank.rt.handlers.RtMsgServerCryptKeyPeerHandler;
import net.hashsploit.clank.server.RtMessageId;

public class RtPacketMap {
	
	// Prevent instantiation
	private RtPacketMap() {}
	
	/**
	 * This builds a static-mapping of RT message types to corresponding RT handlers.
	 * 
	 * @param client
	 * @return
	 */
	public static HashMap<RtMessageId, RtMessageHandler> buildRtPacketMap() {
		final HashMap<RtMessageId, RtMessageHandler> mp = new HashMap<RtMessageId, RtMessageHandler>();
		
		mp.put(RtMessageId.CLIENT_HELLO, new RtMsgClientHello());
		
		mp.put(RtMessageId.CLIENT_CONNECT_TCP, new RtMsgClientConnectTcp());
		mp.put(RtMessageId.CLIENT_DISCONNECT, new RtMsgClientDisconnect());
		
		
		mp.put(RtMessageId.CLIENT_CRYPTKEY_PUBLIC, new RtMsgClientCryptKeyPublicHandler());
		mp.put(RtMessageId.SERVER_CRYPTKEY_PEER, new RtMsgServerCryptKeyPeerHandler());
		
		mp.put(RtMessageId.CLIENT_APP_TOSERVER, new RtMsgClientAppToServer());
		
		mp.put(RtMessageId.CLIENT_ECHO, new RtMsgClientEcho());
		
		return mp;
	}
	
	
}
