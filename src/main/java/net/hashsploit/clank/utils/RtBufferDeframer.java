package net.hashsploit.clank.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RtBufferDeframer {
	private static final Logger logger = Logger.getLogger(RtBufferDeframer.class.getName());

	private RtBuffer rtBuffer;
	
	public RtBufferDeframer() {	
		rtBuffer = new RtBuffer();
	}
	
	public List<ByteBuf> deframe(ByteBuf input) {		
		List<ByteBuf> results = new ArrayList<ByteBuf>();
		
		while (input.readableBytes() != 0) {
			byte nextByte = input.readByte();
			rtBuffer.process(nextByte);
			
			if (rtBuffer.isFull()) {
				results.add(rtBuffer.toByteBuf());
			    rtBuffer.clear();
			}
		}
		
		return results;
	}
	
	public List<ByteBuf> deframe_old(ByteBuf input) {		
		List<ByteBuf> results = new ArrayList<ByteBuf>();
		
		try {
			while (input.readableBytes() >= 3) {
				byte id = input.readByte();

				// Check if encrypted or decrypted
				int idCheckInt = id & 0xFF;
				boolean signed = idCheckInt >= 128;
				//logger.finest("idCheckInt: " + idCheckInt);
				//logger.finest("signed: " + signed);
				
				short length = input.readShortLE();
				if (length == 0) {
					ByteBuf buffer;
					buffer = Unpooled.buffer(length + 2 + 1);
					buffer.writeByte(id);
					buffer.writeShortLE(length);

					buffer.resetReaderIndex();
					results.add(buffer);
					continue;
				}
				
				byte[] hash = new byte[4];
				if (signed) {
					input.readBytes(hash);
				}
				if (length < 0) {
					break;
				}
				byte [] payload = new byte[length];
				input.readBytes(payload);
				
				// Write data to out
				ByteBuf buffer;
				if (signed) {
					buffer = Unpooled.buffer(length + 4 + 2 + 1);
				}
				else {
					buffer = Unpooled.buffer(length + 2 + 1);
				}
				buffer.writeByte(id);
				buffer.writeShortLE(length);
				if (signed) {
					buffer.writeBytes(hash);
				}
				buffer.writeBytes(payload);
				buffer.resetReaderIndex();
				results.add(buffer);
			}
		}
		catch (IndexOutOfBoundsException e){
			logger.severe("Not enough data received! This is either because the TCP Window is Full or the the socket is not receiving enough data.");
		}
		
		
		return results;
	}
	
}