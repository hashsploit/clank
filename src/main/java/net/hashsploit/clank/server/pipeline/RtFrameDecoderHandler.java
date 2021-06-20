package net.hashsploit.clank.server.pipeline;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.RtBufferDeframer;
import net.hashsploit.clank.utils.Utils;

public class RtFrameDecoderHandler extends ByteToMessageDecoder {

	private static final Logger logger = Logger.getLogger(RtFrameDecoderHandler.class.getName());
	private final ByteOrder byteOrder;
	private final int maxFrameLength;
	private final int lengthFieldOffset;
	private final int lengthFieldLength;
	private final int lengthFieldEndOffset;
	private final int lengthAdjustment;
	private final int initialBytesToStrip;
	private final boolean failFast;
	private boolean discardingTooLongFrame;
	private long tooLongFrameLength;
	private long bytesToDiscard;
	private RtBufferDeframer deframer;

	public RtFrameDecoderHandler(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
		this(maxFrameLength, lengthFieldOffset, lengthFieldLength, 0, 0);
	}

	public RtFrameDecoderHandler(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
		this(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, true);
	}

	public RtFrameDecoderHandler(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
		this(ByteOrder.BIG_ENDIAN, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
	}

	public RtFrameDecoderHandler(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
		if (maxFrameLength <= 0) {
			throw new IllegalArgumentException("maxFrameLength must be a positive integer: " + maxFrameLength);
		}
		if (lengthFieldOffset < 0) {
			throw new IllegalArgumentException("lengthFieldOffset must be a non-negative integer: " + lengthFieldOffset);
		}
		if (initialBytesToStrip < 0) {
			throw new IllegalArgumentException("initialBytesToStrip must be a non-negative integer: " + initialBytesToStrip);
		}
		if (lengthFieldOffset > maxFrameLength - lengthFieldLength) {
			throw new IllegalArgumentException("maxFrameLength (" + maxFrameLength + ") " + "must be equal to or greater than " + "lengthFieldOffset (" + lengthFieldOffset + ") + " + "lengthFieldLength (" + lengthFieldLength + ").");
		}

		this.byteOrder = byteOrder;
		this.maxFrameLength = maxFrameLength;
		this.lengthFieldOffset = lengthFieldOffset;
		this.lengthFieldLength = lengthFieldLength;
		this.lengthAdjustment = lengthAdjustment;
		this.lengthFieldEndOffset = lengthFieldOffset + lengthFieldLength;
		this.initialBytesToStrip = initialBytesToStrip;
		this.failFast = failFast;
		this.deframer = new RtBufferDeframer();
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> output) throws Exception {

		logger.finest("Deframe pipeline input" + ctx.channel().remoteAddress().toString() + ": " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(input)));

		if (!ctx.pipeline().channel().isActive()) {
			//return;
		}
		
		// No valid packet is < 3 or > 2048 bytes, drop the connection
		if (input.readableBytes() < 3 || input.readableBytes() > 2048) {
			logger.warning("Invalid protocol request from: " + ctx.channel().remoteAddress().toString());
			//ctx.close();
			//return;
		}

		RtMessageId rtid = null;
		
		// Convert the current id (-0x80 if it's encrypted)
		byte id = (byte) (input.getByte(0) & (byte) 0x7F);
		
		for (final RtMessageId p : RtMessageId.values()) {
			if (p.getValue() == id) {
				rtid = p;
				break;
			}
		}

		// Invalid protocol
		if (rtid == null) {
			logger.warning("Invalid protocol from: " + ctx.channel().remoteAddress().toString());
			ctx.close();
			return;
		}

		ByteBuf buffer = Unpooled.buffer(input.readableBytes());
		buffer.writeBytes(input);
		List<ByteBuf> decodedFrames = this.decode(ctx, buffer);
		
		for (ByteBuf frame: decodedFrames) {
			output.add(frame);
		}
		
	}
	
	

	private List<ByteBuf> decode(ChannelHandlerContext context, ByteBuf input) {
		
		
		List<ByteBuf> results = deframer.deframe_old(input);
		for (ByteBuf result: results) {
			logger.finest("Deframe pipeline out: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(result)));
		}
		
		return results;

	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		logger.warning(String.format("Exception in %s from %s: %s", RtFrameDecoderHandler.class.getName(), ctx.channel().remoteAddress().toString(), cause.getMessage()));
		cause.printStackTrace();
		ctx.close();
	}

}
