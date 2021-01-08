package net.hashsploit.clank.server.pipeline;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;

public class MasHandler extends MessageToMessageDecoder<ByteBuf> {

	private static final Logger logger = Logger.getLogger(MasHandler.class.getName());
	private final MediusClient client;

	public MasHandler(final MediusClient client) {
		super();
		this.client = client;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
		logger.finest("Mas handler recv -> " + msg.toString());
		
		final ByteBuffer buffer = Utils.toNioBuffer(msg);
		final byte[] data = new byte[buffer.remaining()];
		buffer.get(data);
		
		RtMessageId rtid = Utils.getRtMessageId(data[0]);
		
		if (rtid == null) {
			throw new IllegalStateException("Unknown rtid for packet: " + Utils.bytesToHex(data));
		}
		
		RtMessageHandler handler = client.getServer().getRtHandler(rtid);
		
		ByteBuf outData = Unpooled.copiedBuffer(data);
		handler.read(outData);
		List<RTMessage> messages = handler.write(client);
		
		if (messages != null) {
			for (RTMessage message: messages) {
				logger.finest("Writing out:");
				logger.finest(message.toString());
			}
			//out.add(messages);	
			ctx.pipeline().write(messages);
		}
	}

}
