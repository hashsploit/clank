package net.hashsploit.clank.server.pipeline;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.utils.Utils;

public class ScertEncrypt extends ChannelOutboundHandlerAdapter {

	private static final Logger logger = Logger.getLogger(ScertEncrypt.class.getName());
	private final MediusClient client;

	public ScertEncrypt(final MediusClient client) {
		super();
		this.client = client;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.fine(ctx.channel().remoteAddress() + ": channel active");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.fine(ctx.channel().remoteAddress() + ": channel inactive");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		final ByteBuffer buffer = toNioBuffer((ByteBuf) msg);
		final byte[] data = new byte[buffer.remaining()];
		buffer.get(data);

		// No valid packet is < 3 or > 2048 bytes, drop the connection
		if (data.length < 3 || data.length > 2048) {
			ctx.close();
			return;
		}

		// Get RT Packet ID
		RTMessageId rtid = null;

		for (RTMessageId p : RTMessageId.values()) {
			if (p.getValue() == data[0]) {
				rtid = p;
				break;
			}
		}

		logger.finest("TOTAL RAW INCOMING DATA: " + Utils.bytesToHex(data));

		// Get the packets
		List<RTMessage> packets = Utils.decodeRTMessageFrames(data);

		for (RTMessage packet : packets) {
			logger.fine("Packet ID: " + rtid.toString());
			logger.fine("Packet ID: " + rtid.getValue());
		}
	}
	
	
	
	
	
	
	
	
	
	

}
