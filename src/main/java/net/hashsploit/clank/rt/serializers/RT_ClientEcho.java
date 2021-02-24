package net.hashsploit.clank.rt.serializers;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.server.RTMessage;

public class RT_ClientEcho extends RTMessage {

	private final byte heartbeat;
	
	public RT_ClientEcho(ByteBuf payload) {
		super(payload);
		heartbeat = payload.readByte();
	}
	
	/**
	 * Returns the heartbeat payload.
	 * @return
	 */
	public byte getHeartbeat() {
		return heartbeat;
	}

}
