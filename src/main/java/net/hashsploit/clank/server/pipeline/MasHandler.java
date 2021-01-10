package net.hashsploit.clank.server.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
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
		logger.finest("MAS HANDLER IN: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(msg)));
		
		RtMessageHandler handler = client.getServer().getRtHandler(msg.getByte(0));
		
		if (handler == null) {
			logger.severe("Missing RT Handler: 0x" + Utils.byteToHex(msg.getByte(0)));
		}
		
		// Handle reading
		handler.read(msg);
		
		// Handle writing
		List<RTMessage> messages = handler.write(client);
		List<ByteBuf> buffers = new ArrayList<ByteBuf>();

		if (messages != null) {
			//int packetSize = 0;
			for (final RTMessage message : messages) {
				
				// TODO: Consider framing multiple messages into a single packet.
				//packetSize += message.toBytes().length;
				//ctx.pipeline().write(messages);
				//if (packetSize > 2048) {
				//	ctx.pipeline().flush();
				//}
				
				logger.finest("MAS HANDLER OUT: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(message.getFullMessage())));
				
				//ctx.pipeline().writeAndFlush(message.getFullMessage());
				//out.add(message.getFullMessage());
				buffers.add(message.getFullMessage());
			}
			
			// In order for this to be passed to the RtEncryptionHandler or
			// RtFrameEncoder it must be writtenAndFlushed rather than passed to List<Object> out.
			ctx.pipeline().writeAndFlush(buffers);
		}
	}

}
