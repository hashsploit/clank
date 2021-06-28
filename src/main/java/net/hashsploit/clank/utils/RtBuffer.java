package net.hashsploit.clank.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RtBuffer {

	private byte[] buffer;
	private int cursor;
	private boolean encrypted;
	
	public RtBuffer() {
		buffer = new byte[1024];		
		clear();
	}

	public void process(byte nextByte) {
		buffer[cursor] = nextByte;		
		
		// Check encryption
		if (cursor == 0) {
			byte id = buffer[0];
			// Check if encrypted or decrypted
			int idCheckInt = id & 0xFF;
			encrypted = idCheckInt >= 128;
		}
		
		cursor++;
	}

	public boolean isFull() {
		if (cursor < 3) {
			return false;
		}
		
		return getFullLength() == cursor;
	}
	
	private int getFullLength() {
		// 1 -> id, 2 -> len
		int totalLength = 1 + 2 + Utils.bytesToShortLittle(buffer[1], buffer[2]);

		// 4-> hash
		if (encrypted) {
			totalLength += 4;
		}
		
		return totalLength;
	}


	public ByteBuf toByteBuf() {
		ByteBuf b;
		int totalLength = getFullLength();
		b = Unpooled.buffer(totalLength);
		b.writeBytes(buffer, 0, totalLength);
		b.resetReaderIndex();
		return b;
	}

	public void clear() {
		cursor = 0;
	}
	
}