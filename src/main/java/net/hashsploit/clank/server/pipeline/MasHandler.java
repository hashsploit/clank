package net.hashsploit.clank.server.pipeline;

import java.util.List;
import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.objects.MediusMessage;

public class MasHandler extends MessageToMessageDecoder<MediusMessage> {

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
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		logger.finest("-> " + msg.toString());
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, MediusMessage msg, List<Object> out) throws Exception {
		
	}

}
