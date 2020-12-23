package net.hashsploit.clank.server.pipeline;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hashsploit.clank.server.MediusClient;

public class RtFrameEncoderHandler extends MessageToByteEncoder<ByteBuf> {

	private static final Logger logger = Logger.getLogger(RtFrameEncoderHandler.class.getName());
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		logger.warning(String.format("Exception in %s from %s: %s", RtFrameEncoderHandler.class.getName(), ctx.channel().remoteAddress().toString(), cause.getMessage()));
		ctx.close();
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
		out.writeBytes(msg);
	}

}
