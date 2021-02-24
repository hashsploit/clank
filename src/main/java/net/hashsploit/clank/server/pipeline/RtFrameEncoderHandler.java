package net.hashsploit.clank.server.pipeline;

import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.hashsploit.clank.server.MediusClient;

public class RtFrameEncoderHandler extends MessageToByteEncoder<List<ByteBuf>> {

	private static final Logger logger = Logger.getLogger(RtFrameEncoderHandler.class.getName());
	private final MediusClient client;
	
	public RtFrameEncoderHandler(MediusClient client) {
		this.client = client;
	}
	
	@Override
	protected void encode(ChannelHandlerContext ctx, List<ByteBuf> msgs, ByteBuf out) throws Exception {
		for (ByteBuf msg : msgs) {
			out.writeBytes(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		logger.warning(String.format("Exception in %s from %s: %s", RtFrameDecoderHandler.class.getName(), ctx.channel().remoteAddress().toString(), cause.getMessage()));
		cause.printStackTrace();
		ctx.close();
	}

}
