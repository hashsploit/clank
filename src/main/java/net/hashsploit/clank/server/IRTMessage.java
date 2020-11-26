package net.hashsploit.clank.server;

public interface ISCERTMessage {
	
	/**
	 * Get the SCE-RT/RTIME packet ID.
	 * @return
	 */
	public RTPacketId getId();
	
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
