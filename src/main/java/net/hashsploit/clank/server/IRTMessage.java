package net.hashsploit.clank.server;

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
	public byte[] getPayload();
	
}
