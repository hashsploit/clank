package net.hashsploit.clank.server.pipeline;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class ByteArrayEncoder extends MessageToMessageEncoder<Byte[]> {

	private static final Logger logger = Logger.getLogger("");
	
	public ByteArrayEncoder() {
		super();
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Byte[] msg, List<Object> out) throws Exception {
		
		if (msg == null || msg.length == 0) {
			return;
		}
		
		final ByteBuf buffer = ctx.alloc().buffer(msg.length);
		
		out.add(buffer);
	}

	/**
	 * Convert Netty ByteBuff to ByteBuffer
	 * @param buffer
	 * @return
	 */
	private static ByteBuffer toNioBuffer(final ByteBuf buffer) {
		
		if (buffer.isDirect()) {
			return buffer.nioBuffer();
		}
		
		final byte[] bytes = new byte[buffer.readableBytes()];
		buffer.getBytes(buffer.readerIndex(), bytes);
		
		return ByteBuffer.wrap(bytes);
	}
	
}
