package net.hashsploit.clank.rt;

import java.util.HashMap;

import net.hashsploit.clank.rt.handlers.RtMsgClientConnectTcp;
import net.hashsploit.clank.rt.handlers.RtMsgClientCryptKeyPeerHandler;
import net.hashsploit.clank.rt.handlers.RtMsgClientCryptKeyPublicHandler;
import net.hashsploit.clank.rt.handlers.RtMsgClientDisconnect;
import net.hashsploit.clank.server.MediusClient;
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
	public static HashMap<RtMessageId, RtMessageHandler> buildRtPacketMap(MediusClient client) {
		final HashMap<RtMessageId, RtMessageHandler> mp = new HashMap<RtMessageId, RtMessageHandler>();
		
		mp.put(RtMessageId.CLIENT_CONNECT_TCP, new RtMsgClientConnectTcp(client));
		mp.put(RtMessageId.CLIENT_DISCONNECT, new RtMsgClientDisconnect(client));
		
		
		mp.put(RtMessageId.CLIENT_CRYPTKEY_PUBLIC, new RtMsgClientCryptKeyPublicHandler(client));
		mp.put(RtMessageId.CLIENT_CRYPTKEY_PEER, new RtMsgClientCryptKeyPeerHandler(client));
		
		
		return mp;
	}
	
	
}
