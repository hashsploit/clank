package net.hashsploit.clank.server;

import io.netty.buffer.ByteBuf;

public interface IRTMessage {
	
	/**
	 * Get the SCE-RT/RTIME packet ID.
	 * @return
	 */
	public RtMessageId getId();
	
	/**
	 * Get the data length.
	 * @return
	 */
	public int getLength();
	
	/**
	 * Get the raw data.
	 * @return
	 */
	public ByteBuf getPayload();
	
}
