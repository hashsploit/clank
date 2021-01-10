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

		RtMessageId rtid = Utils.getRtMessageId(msg.getByte(msg.readerIndex()));

		RtMessageHandler handler = client.getServer().getRtHandler(rtid);
		
		// Handle reading
		logger.finest("Data to read: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(msg)));
		handler.read(msg);
		
		// Handle writing
		List<RTMessage> messages = handler.write(client);

		if (messages != null) {
			//int packetSize = 0;
			for (final RTMessage message : messages) {
				
				// TODO: Consider framing multiple messages into a single packet.
				//packetSize += message.toBytes().length;
				//ctx.pipeline().write(messages);
				//if (packetSize > 2048) {
				//	ctx.pipeline().flush();
				//}
				
				ctx.pipeline().writeAndFlush(message.getFullMessage());
				
			}
			
		}
	}

}
