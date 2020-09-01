package net.hashsploit.clank.server;

import java.nio.ByteBuffer;

public class EncryptedDataPacket extends DataPacket implements ISCERTMessage {

	private final byte[] checksum;
	
	/**
	 * Represents a single raw-data packet from a Medius client.
	 * @param id
	 * @param checksum
	 * @param payload
	 */
	public EncryptedDataPacket(RTPacketId id, byte[] checksum, byte[] payload) {
		super(id, payload);
		
		if (checksum.length != 4) {
			throw new IllegalStateException("EncryptedDataPacket requires the checksum be 4-bytes long! Got " + checksum.length + " instead.");
		}
		
		//this.id = id;
		this.checksum = checksum;
		//this.length = data.length - checksum.length;
		//this.data = data;
	}
	
	/**
	 * Get the payload length.
	 * @return
	 */
	public int getLength() {
		return super.getLength() - checksum.length;
	}
	
	/**
	 * Get the checksum of the packet.
	 * @return
	 */
	public byte[] getChecksum() {
		return checksum;
	}
	
	/**
	 * Get the full representation of the data.
	 * @return
	 */
	public ByteBuffer toData() {
		ByteBuffer buffer = ByteBuffer.allocate(super.getLength() + 1 + 2 + checksum.length);
		
		buffer.put(super.getId().getByte());
		buffer.putInt(super.getLength());
		buffer.put(checksum);
		buffer.put(super.getPayload());
		
		buffer.flip();
		
		return buffer;
	}
	
}
