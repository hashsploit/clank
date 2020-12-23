package net.hashsploit.clank.server.pipeline;

import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hashsploit.clank.server.MediusClient;

public class InboundOutboundMasHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = Logger.getLogger(InboundOutboundMasHandler.class.getName());
	private final MediusClient client;

	public InboundOutboundMasHandler(final MediusClient client) {
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

}
