package net.hashsploit.clank.crypto;

public class MediusDecryptedMessage {
	
	private final byte[] data;
	private final boolean success;
	
	/**
	 * A decrypted Medius message.
	 * @param data
	 * @param success
	 */
	public MediusDecryptedMessage(final byte[] data, final boolean success) {
		this.data = data;
		this.success = success;
	}
	
	/**
	 * Get the plain-text data.
	 * @return
	 */
	public byte[] getPlain() {
		return data;
	}
	
	/**
	 * Returns true if encryption was successful.
	 * @return
	 */
	public boolean isSuccessful() {
		return success;
	}
	
}
