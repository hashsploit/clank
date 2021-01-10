package net.hashsploit.clank.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hashsploit.clank.utils.Utils;

public class RTMessage implements IRTMessage {
	
	private final RtMessageId id;
	private final short length;
	private ByteBuf payload;
	
	/**
	 * Create an RT message from raw ByteBuf data.
	 * 
	 * Expects:
	 * id (1 byte)
	 * length (2 bytes LE)
	 * payload (n bytes)
	 * 
	 * @param id
	 * @param checksum
	 * @param payload
	 */
	public RTMessage(ByteBuf data) {
		this(RtMessageId.getIdByByte(data.readByte()), data);
	}
	
	/**
	 * Create an RT message from an RT ID and a ByteBuf.
	 * 
	 * @param id
	 * @param data
	 */
	public RTMessage(RtMessageId id, ByteBuf data) {
		this.id = id;
		this.length = (short) data.readShortLE();
		this.payload = data;
	}
	
	/**
	 * Create an RT message from an RT ID and a byte[] of data.
	 * 
	 * @param id
	 * @param data
	 */
	public RTMessage(RtMessageId id, byte[] data) {
		this.id = id;
		this.length = (short) data.length;
		this.payload = Unpooled.wrappedBuffer(data);
	}

	/**
	 * Create an empty payload RT message.
	 * 
	 * @param id
	 * @param data
	 */
	public RTMessage(RtMessageId id) {
		this.id = id;
		this.length = (short) 0;
		this.payload = Unpooled.wrappedBuffer(new byte[0]);
	}

	
	/**
	 * Get the packet id.
	 * 
	 * @return
	 */
	public RtMessageId getId() {
		return id;
	}
	
	/**
	 * Get the data length.
	 * 
	 * @return
	 */
	public int getLength() {
		return length;
	}
	
	/**
	 * Get the data payload.
	 * 
	 * @return
	 */
	public ByteBuf getPayload() {
		return payload;
	}
	
	/**
	 * Get the full representation of the data.
	 * 
	 * @return
	 */
	public ByteBuf getFullMessage() {
		ByteBuf buffer = Unpooled.buffer(length + 2 + 1);
		buffer.writeByte(id.getValue());
		buffer.writeShortLE(length);
		buffer.writeBytes(payload);
		return buffer;
	}
	
	@Override
	public String toString() {
		return "RTMessage[ id: " + id.toString() + ", len: " + length + " payload: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(payload)) + "]";
	}
	
}