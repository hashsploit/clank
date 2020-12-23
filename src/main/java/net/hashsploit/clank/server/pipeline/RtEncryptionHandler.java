package net.hashsploit.clank.server.pipeline;

import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.hashsploit.clank.server.MediusClient;

public class RtEncryptionHandler extends MessageToMessageEncoder<ByteBuf> {

	private static final Logger logger = Logger.getLogger(RtEncryptionHandler.class.getName());
	private final MediusClient client;

	public RtEncryptionHandler(final MediusClient client) {
		super();
		this.client = client;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf input, List<Object> output) throws Exception {
		
	}

	
	
	
	
}
