package net.hashsploit.clank.server.pipeline;

import java.util.List;
import java.util.logging.Logger;

import io.grpc.netty.shaded.io.netty.channel.ChannelHandlerContext;
import io.grpc.netty.shaded.io.netty.handler.codec.MessageToMessageEncoder;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.medius.crypto.RawRTMessage;

public class RtEncryptionHandler extends MessageToMessageEncoder<RawRTMessage> {

	private static final Logger logger = Logger.getLogger(RtEncryptionHandler.class.getName());
	private final MediusClient client;

	public RtEncryptionHandler(final MediusClient client) {
		super();
		this.client = client;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, RawRTMessage message, List<Object> output) throws Exception {
		
	}

	
	
	
	
}
