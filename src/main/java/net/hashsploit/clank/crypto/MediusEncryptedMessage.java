package net.hashsploit.clank.crypto;

public class MediusEncryptedMessage {
	
	private final byte[] data;
	private final byte[] checksum;
	private final boolean success;
	
	/**
	 * An encrypted Medius message.
	 * @param data
	 * @param checksum
	 * @param status
	 */
	public MediusEncryptedMessage(final byte[] data, final byte[] checksum, final boolean status) {
		this.data = data;
		this.checksum = checksum;
		this.success = status;
	}
	
	/**
	 * Get the encrypted data.
	 * @return
	 */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * Get the checksum hash of the data.
	 * @return
	 */
	public byte[] getChecksum() {
		return checksum;
	}
	
	/**
	 * Returns true if encryption was successful.
	 * @return
	 */
	public boolean isSuccessful() {
		return success;
	}
	
}