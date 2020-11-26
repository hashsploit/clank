package net.hashsploit.clank.server;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.logging.Logger;

import net.hashsploit.clank.utils.Utils;

public class RTMessage implements IRTMessage {
	private static final Logger logger = Logger.getLogger("");

	private final RTMessageId id;
	private final short length;
	private final byte[] payload;
	
	/**
	 * Represents a single plain-text raw data packet from a Medius client.
	 * @param id
	 * @param checksum
	 * @param payload
	 */
	public RTMessage(RTMessageId id, byte[] payload) {
		this.id = id;
		this.length = (short) payload.length;
	    // Remove RT-ID and length from the packet data
		this.payload = payload;
	}
	
	/**
	 * Get the packet id.
	 * @return
	 */
	public RTMessageId getId() {
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
	public byte[] toBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(length + 2 + 1); // Payload length + 3 for RT ID and RT Len
		
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		buffer.put(id.getValue());
		buffer.putShort(length);
		buffer.put(payload);
		
		buffer.flip();
		return buffer.array();
	}
	
}