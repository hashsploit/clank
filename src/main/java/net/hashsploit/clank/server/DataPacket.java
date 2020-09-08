package net.hashsploit.clank.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Logger;

public class DataPacket implements ISCERTMessage {
	private static final Logger logger = Logger.getLogger("");

	private final RTPacketId id;
	private final short length;
	private final byte[] payload;
	
	/**
	 * Represents a single plain-text raw data packet from a Medius client.
	 * @param id
	 * @param checksum
	 * @param payload
	 */
	public DataPacket(RTPacketId id, byte[] payload) {
		this.id = id;
		this.length = (short) payload.length;
		this.payload = payload;
	}
	
	/**
	 * Get the packet id.
	 * @return
	 */
	public RTPacketId getId() {
		return id;
	}
	
	/**
	 * Get the data length.
	 * @return
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Get the raw data itself.
	 * @return
	 */
	public byte[] getPayload() {
		return payload;
	}
	
	/**
	 * Get the full representation of the data.
	 * @return
	 */
	public ByteBuffer toData() {
		ByteBuffer buffer = ByteBuffer.allocate(length + 1 + 2);
		
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		buffer.put(id.getByte());
		buffer.putShort(length);
		buffer.put(payload);
		
		buffer.flip();
		
		return buffer;
	}
	
}