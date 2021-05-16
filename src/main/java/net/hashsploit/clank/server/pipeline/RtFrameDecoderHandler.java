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
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> output) throws Exception {

		if (!ctx.pipeline().channel().isActive()) {
			//return;
		}
		
		// No valid packet is < 3 or > 2048 bytes, drop the connection
		if (input.readableBytes() < 3 || input.readableBytes() > 2048) {
			logger.warning("Invalid protocol request from: " + ctx.channel().remoteAddress().toString());
			ctx.close();
			return;
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
		
//		if (decoded != null) {
//			logger.severe("INCOMING MESSAGE FRAME: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray((ByteBuf) decoded)));
//			output.add(decoded);
//		}
	}
	
	private List<ByteBuf> basicDeframe(ByteBuf input) {
		// TODO: ADD ERROR CHECKING
		
		logger.finest("Deframe pipeline input: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(input)));
		
		List<ByteBuf> results = new ArrayList<ByteBuf>();
		
		while (input.readableBytes() >= 3) {
			byte id = input.readByte();

			// Check if encrypted or decrypted
			int idCheckInt = id & 0xFF;
			boolean signed = idCheckInt >= 128;
			//logger.finest("idCheckInt: " + idCheckInt);
			//logger.finest("signed: " + signed);
			
			short length = input.readShortLE();
			if (length == 0) {
				ByteBuf buffer;
				buffer = Unpooled.buffer(length + 2 + 1);
				buffer.writeByte(id);
				buffer.writeShortLE(length);

				buffer.resetReaderIndex();
				results.add(buffer);
				continue;
			}
			
			byte[] hash = new byte[4];
			if (signed) {
				input.readBytes(hash);
			}
			byte [] payload = new byte[length];
			input.readBytes(payload);
			
			// Write data to out
			ByteBuf buffer;
			if (signed) {
				buffer = Unpooled.buffer(length + 4 + 2 + 1);
			}
			else {
				buffer = Unpooled.buffer(length + 2 + 1);
			}
			buffer.writeByte(id);
			buffer.writeShortLE(length);
			if (signed) {
				buffer.writeBytes(hash);
			}
			buffer.writeBytes(payload);
			buffer.resetReaderIndex();
			results.add(buffer);
		}
		
		return results;
	}

	private List<ByteBuf> decode(ChannelHandlerContext context, ByteBuf input) {
		
		
		List<ByteBuf> results = basicDeframe(input);
		for (ByteBuf result: results) {
			logger.finest("Deframe pipeline out: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(result)));
		}
		
		return results;
//		if (this.discardingTooLongFrame) {
//			long bytesToDiscard = this.bytesToDiscard;
//			int localBytesToDiscard = (int) Math.min(bytesToDiscard, input.readableBytes());
//			input.skipBytes(localBytesToDiscard);
//			bytesToDiscard -= localBytesToDiscard;
//			this.bytesToDiscard = bytesToDiscard;
//
//			this.failIfNessesary(false);
//		}
//		
//		if (input.readableBytes() < this.lengthFieldEndOffset) {
//			return null;
//		}
//
//		int actualLengthFieldOffset = input.readerIndex() + this.lengthFieldOffset;
//		long frameLength = this.getUnadjustedFrameLength(input, actualLengthFieldOffset, this.lengthFieldLength, this.byteOrder);
//
//		if (frameLength < 0) {
//			input.skipBytes(this.lengthFieldEndOffset);
//			throw new CorruptedFrameException("Negative pre-adjustment length field: " + frameLength);
//		}
//
//		// FIXME: this isn't detecting if the packet is signed or not properly
//		boolean signed = (input.getByte(input.readerIndex()) & 0xFF) >= 0x80;
//		
//		frameLength += this.lengthAdjustment + this.lengthFieldEndOffset + ((signed && frameLength > 0) ? 4 : 0);
//
//		if (frameLength < this.lengthFieldEndOffset) {
//			input.skipBytes(this.lengthFieldEndOffset);
//			throw new CorruptedFrameException("Adjusted frame length (" + frameLength + ") is less than lengthFieldEndOffset: " + this.lengthFieldEndOffset);
//		}
//
//		if (frameLength > this.maxFrameLength) {
//			int startOff = (int) Math.min(20, input.arrayOffset());
//			throw new CorruptedFrameException(String.format("Frame Length exceeds max frame length on buffer: startOffset: %d", startOff));
//		}
//		
//		// never overflows because it's less than maxFrameLength
//		int frameLengthInt = (int) frameLength;
//		if (input.readableBytes() < frameLengthInt) {
//			return null;
//		}
//
//		if (this.initialBytesToStrip > frameLengthInt) {
//			input.skipBytes(frameLengthInt);
//			throw new CorruptedFrameException("Adjusted frame length (" + frameLength + ") is less " + "than initialBytesToStrip: " + this.initialBytesToStrip);
//		}
//		input.skipBytes(this.initialBytesToStrip);
//		
//		// extract frame
//		int readerIndex = input.readerIndex();
//		int actualFrameLength = frameLengthInt - this.initialBytesToStrip;
//		ByteBuf frame = this.extractFrame(context, input, readerIndex, actualFrameLength);
//		input.readerIndex(readerIndex + actualFrameLength);
//		return frame;
	}
//
//	private long getUnadjustedFrameLength(ByteBuf buffer, int offset, int length, ByteOrder order) {
//		long frameLength;
//		switch (length) {
//			case 1:
//				frameLength = buffer.getByte(offset);
//				break;
//			case 2:
//				frameLength = order == ByteOrder.BIG_ENDIAN ? buffer.getUnsignedShort(offset) : buffer.getUnsignedShortLE(offset);
//				break;
//			case 3:
//				frameLength = order == ByteOrder.BIG_ENDIAN ? buffer.getUnsignedMedium(offset) : buffer.getUnsignedMediumLE(offset);
//				break;
//			case 4:
//				frameLength = order == ByteOrder.BIG_ENDIAN ? buffer.getInt(offset) : buffer.getIntLE(offset);
//				break;
//			case 8:
//				frameLength = order == ByteOrder.BIG_ENDIAN ? buffer.getLong(offset) : buffer.getLongLE(offset);
//				break;
//			default:
//				throw new DecoderException("Unsupported lengthFieldLength: " + this.lengthFieldLength + " (expected: 1, 2, 3, 4, or 8)");
//		}
//		return frameLength;
//	}
//
//	private ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
//		ByteBuf buff = buffer.slice(index, length);
//		buff.retain();
//		return buff;
//	}
//
//	private void failIfNessesary(boolean firstDetectionOfTooLongFrame) {
//		if (this.bytesToDiscard == 0) {
//			// Reset to the initial state and tell the handlers that
//			// the frame was too large.
//			long tooLongFrameLength = this.tooLongFrameLength;
//			this.tooLongFrameLength = 0;
//			this.discardingTooLongFrame = false;
//			if (!this.failFast || this.failFast && firstDetectionOfTooLongFrame) {
//				this.fail(tooLongFrameLength);
//			}
//		} else {
//			// Keep discarding and notify handlers if necessary.
//			if (this.failFast && firstDetectionOfTooLongFrame) {
//				this.fail(this.tooLongFrameLength);
//			}
//		}
//	}
//
//	private void fail(long frameLength) {
//		if (frameLength > 0) {
//			throw new TooLongFrameException("Adjusted frame length exceeds " + this.maxFrameLength + ": " + frameLength + " - discarded");
//		} else {
//			throw new TooLongFrameException("Adjusted frame length exceeds " + this.maxFrameLength + " - discarding");
//		}
//	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		logger.warning(String.format("Exception in %s from %s: %s", RtFrameDecoderHandler.class.getName(), ctx.channel().remoteAddress().toString(), cause.getMessage()));
		cause.printStackTrace();
		ctx.close();
	}

}
