package net.hashsploit.clank.server.pipeline;

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
import static io.netty.util.internal.ObjectUtil.checkNotNull;
import static io.netty.util.internal.ObjectUtil.checkPositive;
import static io.netty.util.internal.ObjectUtil.checkPositiveOrZero;

import java.nio.ByteOrder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.TooLongFrameException;

/**
 * A decoder that splits the received {@link ByteBuf}s dynamically by the value
 * of the length field in the message. It is particularly useful when you decode
 * a binary message which has an integer header field that represents the length
 * of the message body or the whole message.
 * <p>
 * {@link RTFrameDecoder} has many configuration parameters so that it can
 * decode any message with a length field, which is often seen in proprietary
 * client-server protocols. Here are some example that will give you the basic
 * idea on which option does what.
 *
 * <h3>2 bytes length field at offset 0, do not strip header</h3>
 *
 * The value of the length field in this example is <tt>12 (0x0C)</tt> which
 * represents the length of "HELLO, WORLD". By default, the decoder assumes that
 * the length field represents the number of the bytes that follows the length
 * field. Therefore, it can be decoded with the simplistic parameter
 * combination.
 * 
 * <pre>
 * <b>lengthFieldOffset</b>   = <b>0</b>
 * <b>lengthFieldLength</b>   = <b>2</b>
 * lengthAdjustment    = 0
 * initialBytesToStrip = 0 (= do not strip header)
 *
 * BEFORE DECODE (14 bytes)         AFTER DECODE (14 bytes)
 * +--------+----------------+      +--------+----------------+
 * | Length | Actual Content |----->| Length | Actual Content |
 * | 0x000C | "HELLO, WORLD" |      | 0x000C | "HELLO, WORLD" |
 * +--------+----------------+      +--------+----------------+
 * </pre>
 *
 * <h3>2 bytes length field at offset 0, strip header</h3>
 *
 * Because we can get the length of the content by calling
 * {@link ByteBuf#readableBytes()}, you might want to strip the length field by
 * specifying <tt>initialBytesToStrip</tt>. In this example, we specified
 * <tt>2</tt>, that is same with the length of the length field, to strip the
 * first two bytes.
 * 
 * <pre>
 * lengthFieldOffset   = 0
 * lengthFieldLength   = 2
 * lengthAdjustment    = 0
 * <b>initialBytesToStrip</b> = <b>2</b> (= the length of the Length field)
 *
 * BEFORE DECODE (14 bytes)         AFTER DECODE (12 bytes)
 * +--------+----------------+      +----------------+
 * | Length | Actual Content |----->| Actual Content |
 * | 0x000C | "HELLO, WORLD" |      | "HELLO, WORLD" |
 * +--------+----------------+      +----------------+
 * </pre>
 *
 * <h3>2 bytes length field at offset 0, do not strip header, the length field
 * represents the length of the whole message</h3>
 *
 * In most cases, the length field represents the length of the message body
 * only, as shown in the previous examples. However, in some protocols, the
 * length field represents the length of the whole message, including the
 * message header. In such a case, we specify a non-zero
 * <tt>lengthAdjustment</tt>. Because the length value in this example message
 * is always greater than the body length by <tt>2</tt>, we specify <tt>-2</tt>
 * as <tt>lengthAdjustment</tt> for compensation.
 * 
 * <pre>
 * lengthFieldOffset   =  0
 * lengthFieldLength   =  2
 * <b>lengthAdjustment</b>    = <b>-2</b> (= the length of the Length field)
 * initialBytesToStrip =  0
 *
 * BEFORE DECODE (14 bytes)         AFTER DECODE (14 bytes)
 * +--------+----------------+      +--------+----------------+
 * | Length | Actual Content |----->| Length | Actual Content |
 * | 0x000E | "HELLO, WORLD" |      | 0x000E | "HELLO, WORLD" |
 * +--------+----------------+      +--------+----------------+
 * </pre>
 *
 * <h3>3 bytes length field at the end of 5 bytes header, do not strip
 * header</h3>
 *
 * The following message is a simple variation of the first example. An extra
 * header value is prepended to the message. <tt>lengthAdjustment</tt> is zero
 * again because the decoder always takes the length of the prepended data into
 * account during frame length calculation.
 * 
 * <pre>
 * <b>lengthFieldOffset</b>   = <b>2</b> (= the length of Header 1)
 * <b>lengthFieldLength</b>   = <b>3</b>
 * lengthAdjustment    = 0
 * initialBytesToStrip = 0
 *
 * BEFORE DECODE (17 bytes)                      AFTER DECODE (17 bytes)
 * +----------+----------+----------------+      +----------+----------+----------------+
 * | Header 1 |  Length  | Actual Content |----->| Header 1 |  Length  | Actual Content |
 * |  0xCAFE  | 0x00000C | "HELLO, WORLD" |      |  0xCAFE  | 0x00000C | "HELLO, WORLD" |
 * +----------+----------+----------------+      +----------+----------+----------------+
 * </pre>
 *
 * <h3>3 bytes length field at the beginning of 5 bytes header, do not strip
 * header</h3>
 *
 * This is an advanced example that shows the case where there is an extra
 * header between the length field and the message body. You have to specify a
 * positive <tt>lengthAdjustment</tt> so that the decoder counts the extra
 * header into the frame length calculation.
 * 
 * <pre>
 * lengthFieldOffset   = 0
 * lengthFieldLength   = 3
 * <b>lengthAdjustment</b>    = <b>2</b> (= the length of Header 1)
 * initialBytesToStrip = 0
 *
 * BEFORE DECODE (17 bytes)                      AFTER DECODE (17 bytes)
 * +----------+----------+----------------+      +----------+----------+----------------+
 * |  Length  | Header 1 | Actual Content |----->|  Length  | Header 1 | Actual Content |
 * | 0x00000C |  0xCAFE  | "HELLO, WORLD" |      | 0x00000C |  0xCAFE  | "HELLO, WORLD" |
 * +----------+----------+----------------+      +----------+----------+----------------+
 * </pre>
 *
 * <h3>2 bytes length field at offset 1 in the middle of 4 bytes header, strip
 * the first header field and the length field</h3>
 *
 * This is a combination of all the examples above. There are the prepended
 * header before the length field and the extra header after the length field.
 * The prepended header affects the <tt>lengthFieldOffset</tt> and the extra
 * header affects the <tt>lengthAdjustment</tt>. We also specified a non-zero
 * <tt>initialBytesToStrip</tt> to strip the length field and the prepended
 * header from the frame. If you don't want to strip the prepended header, you
 * could specify <tt>0</tt> for <tt>initialBytesToSkip</tt>.
 * 
 * <pre>
 * lengthFieldOffset   = 1 (= the length of HDR1)
 * lengthFieldLength   = 2
 * <b>lengthAdjustment</b>    = <b>1</b> (= the length of HDR2)
 * <b>initialBytesToStrip</b> = <b>3</b> (= the length of HDR1 + LEN)
 *
 * BEFORE DECODE (16 bytes)                       AFTER DECODE (13 bytes)
 * +------+--------+------+----------------+      +------+----------------+
 * | HDR1 | Length | HDR2 | Actual Content |----->| HDR2 | Actual Content |
 * | 0xCA | 0x000C | 0xFE | "HELLO, WORLD" |      | 0xFE | "HELLO, WORLD" |
 * +------+--------+------+----------------+      +------+----------------+
 * </pre>
 *
 * <h3>2 bytes length field at offset 1 in the middle of 4 bytes header, strip
 * the first header field and the length field, the length field represents the
 * length of the whole message</h3>
 *
 * Let's give another twist to the previous example. The only difference from
 * the previous example is that the length field represents the length of the
 * whole message instead of the message body, just like the third example. We
 * have to count the length of HDR1 and Length into <tt>lengthAdjustment</tt>.
 * Please note that we don't need to take the length of HDR2 into account
 * because the length field already includes the whole header length.
 * 
 * <pre>
 * lengthFieldOffset   =  1
 * lengthFieldLength   =  2
 * <b>lengthAdjustment</b>    = <b>-3</b> (= the length of HDR1 + LEN, negative)
 * <b>initialBytesToStrip</b> = <b> 3</b>
 *
 * BEFORE DECODE (16 bytes)                       AFTER DECODE (13 bytes)
 * +------+--------+------+----------------+      +------+----------------+
 * | HDR1 | Length | HDR2 | Actual Content |----->| HDR2 | Actual Content |
 * | 0xCA | 0x0010 | 0xFE | "HELLO, WORLD" |      | 0xFE | "HELLO, WORLD" |
 * +------+--------+------+----------------+      +------+----------------+
 * </pre>
 * 
 * @see LengthFieldPrepender
 */
public class RTFrameDecoder extends ByteToMessageDecoder {

	private static final Logger logger = Logger.getLogger("");

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

	/**
	 * Creates a new instance.
	 *
	 * @param maxFrameLength    the maximum length of the frame. If the length of
	 *                          the frame is greater than this value,
	 *                          {@link TooLongFrameException} will be thrown.
	 * @param lengthFieldOffset the offset of the length field
	 * @param lengthFieldLength the length of the length field
	 */
	public RTFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
		this(maxFrameLength, lengthFieldOffset, lengthFieldLength, 0, 0);
	}

	/**
	 * Creates a new instance.
	 *
	 * @param maxFrameLength      the maximum length of the frame. If the length of
	 *                            the frame is greater than this value,
	 *                            {@link TooLongFrameException} will be thrown.
	 * @param lengthFieldOffset   the offset of the length field
	 * @param lengthFieldLength   the length of the length field
	 * @param lengthAdjustment    the compensation value to add to the value of the
	 *                            length field
	 * @param initialBytesToStrip the number of first bytes to strip out from the
	 *                            decoded frame
	 */
	public RTFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip) {
		this(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, true);
	}

	/**
	 * Creates a new instance.
	 *
	 * @param maxFrameLength      the maximum length of the frame. If the length of
	 *                            the frame is greater than this value,
	 *                            {@link TooLongFrameException} will be thrown.
	 * @param lengthFieldOffset   the offset of the length field
	 * @param lengthFieldLength   the length of the length field
	 * @param lengthAdjustment    the compensation value to add to the value of the
	 *                            length field
	 * @param initialBytesToStrip the number of first bytes to strip out from the
	 *                            decoded frame
	 * @param failFast            If <tt>true</tt>, a {@link TooLongFrameException}
	 *                            is thrown as soon as the decoder notices the
	 *                            length of the frame will exceed
	 *                            <tt>maxFrameLength</tt> regardless of whether the
	 *                            entire frame has been read. If <tt>false</tt>, a
	 *                            {@link TooLongFrameException} is thrown after the
	 *                            entire frame that exceeds <tt>maxFrameLength</tt>
	 *                            has been read.
	 */
	public RTFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip, boolean failFast) {
		this(ByteOrder.BIG_ENDIAN, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment,
				initialBytesToStrip, failFast);
	}

	/**
	 * Creates a new instance.
	 *
	 * @param byteOrder           the {@link ByteOrder} of the length field
	 * @param maxFrameLength      the maximum length of the frame. If the length of
	 *                            the frame is greater than this value,
	 *                            {@link TooLongFrameException} will be thrown.
	 * @param lengthFieldOffset   the offset of the length field
	 * @param lengthFieldLength   the length of the length field
	 * @param lengthAdjustment    the compensation value to add to the value of the
	 *                            length field
	 * @param initialBytesToStrip the number of first bytes to strip out from the
	 *                            decoded frame
	 * @param failFast            If <tt>true</tt>, a {@link TooLongFrameException}
	 *                            is thrown as soon as the decoder notices the
	 *                            length of the frame will exceed
	 *                            <tt>maxFrameLength</tt> regardless of whether the
	 *                            entire frame has been read. If <tt>false</tt>, a
	 *                            {@link TooLongFrameException} is thrown after the
	 *                            entire frame that exceeds <tt>maxFrameLength</tt>
	 *                            has been read.
	 */
	public RTFrameDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {

		this.byteOrder = checkNotNull(byteOrder, "byteOrder");

		checkPositive(maxFrameLength, "maxFrameLength");

		checkPositiveOrZero(lengthFieldOffset, "lengthFieldOffset");

		checkPositiveOrZero(initialBytesToStrip, "initialBytesToStrip");

		if (lengthFieldOffset > maxFrameLength - lengthFieldLength) {
			throw new IllegalArgumentException("maxFrameLength (" + maxFrameLength + ") "
					+ "must be equal to or greater than " + "lengthFieldOffset (" + lengthFieldOffset + ") + "
					+ "lengthFieldLength (" + lengthFieldLength + ").");
		}

		this.maxFrameLength = maxFrameLength;
		this.lengthFieldOffset = lengthFieldOffset;
		this.lengthFieldLength = lengthFieldLength;
		this.lengthAdjustment = lengthAdjustment;
		this.lengthFieldEndOffset = lengthFieldOffset + lengthFieldLength;
		this.initialBytesToStrip = initialBytesToStrip;
		this.failFast = failFast;
	}

	@Override
	protected final void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
		ByteBuf decoded = this.decode(ctx, input);
		if (decoded != null) {
			out.add(decoded);
		}
	}

	protected ByteBuf decode(ChannelHandlerContext ctx, ByteBuf input) {
		if (this.discardingTooLongFrame) {
			long bytesToDiscard = this.bytesToDiscard;
			int localBytesToDiscard = (int) Math.min(bytesToDiscard, input.readableBytes());
			input.skipBytes(localBytesToDiscard);
			bytesToDiscard -= localBytesToDiscard;
			this.bytesToDiscard = bytesToDiscard;

			// TODO: make this throw an exception instead, and a few of the other statements
			// below too.
			this.failIfNecessary(false);
		}

		if (input.readableBytes() < this.lengthFieldEndOffset) {
			return null;
		}

		int actualLengthFieldOffset = input.readerIndex() + this.lengthFieldOffset;
		long frameLength = this.getUnadjustedFrameLength(input, actualLengthFieldOffset, this.lengthFieldLength,
				this.byteOrder);

		if (frameLength < 0) {
			input.skipBytes(this.lengthFieldEndOffset);
			throw new CorruptedFrameException("negative pre-adjustment length field: " + frameLength);
		}

		final boolean signed = input.getByte(input.readerIndex()) >= 0x80;
		frameLength += this.lengthAdjustment + this.lengthFieldEndOffset + ((signed && frameLength > 0) ? 4 : 0);

		if (frameLength < this.lengthFieldEndOffset) {
			input.skipBytes(this.lengthFieldEndOffset);
			throw new CorruptedFrameException("Adjusted frame length (" + frameLength + ") is less "
					+ "than lengthFieldEndOffset: " + this.lengthFieldEndOffset);
		}

		if (frameLength > this.maxFrameLength) {
			long discard = frameLength - input.readableBytes();
			this.tooLongFrameLength = frameLength;
			int startOff = (int) Math.min(20, input.arrayOffset());
			logger.log(Level.SEVERE,
					"{context.Channel.RemoteAddress} Frame Length exceeds max frame length on buffer: start:{startOff} {BitConverter.ToString(input.Array, input.ArrayOffset - startOff, startOff + input.ReadableBytes)}");

			if (discard < 0) {
				// buffer contains more bytes then the frameLength so we can discard all now
				input.skipBytes((int) frameLength);
			} else {
				// Enter the discard mode and discard everything received so far.
				// this.discardingTooLongFrame = true;
				this.bytesToDiscard = discard;
				input.skipBytes(input.readableBytes());
			}

			this.failIfNecessary(true);
			return null;
		}

		// never overflows because it's less than maxFrameLength
		int frameLengthInt = (int) frameLength;
		if (input.readableBytes() < frameLengthInt) {
			return null;
		}

		if (this.initialBytesToStrip > frameLengthInt) {
			input.skipBytes(frameLengthInt);
			throw new CorruptedFrameException("Adjusted frame length (" + frameLength + ") is less "
					+ "than initialBytesToStrip: " + this.initialBytesToStrip);
		}
		input.skipBytes(this.initialBytesToStrip);

		// extract frame
		int readerIndex = input.readerIndex();
		int actualFrameLength = frameLengthInt - this.initialBytesToStrip;
		ByteBuf frame = this.extractFrame(ctx, input, readerIndex, actualFrameLength);
		input.readerIndex(readerIndex + actualFrameLength);
		return frame;
	}

	protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order) {
		long frameLength;
        switch (length)
        {
            case 1:
                frameLength = buf.getByte(offset);
                break;
            case 2:
                frameLength = order == ByteOrder.BIG_ENDIAN ? buf.getUnsignedShort(offset) : buf.getUnsignedShortLE(offset);
                break;
            case 3:
                frameLength = order == ByteOrder.BIG_ENDIAN ? buf.getUnsignedMedium(offset) : buf.getUnsignedMediumLE(offset);
                break;
            case 4:
                frameLength = order == ByteOrder.BIG_ENDIAN ? buf.getInt(offset) : buf.getIntLE(offset);
                break;
            case 8:
                frameLength = order == ByteOrder.BIG_ENDIAN ? buf.getLong(offset) : buf.getLongLE(offset);
                break;
            default:
                throw new DecoderException("unsupported lengthFieldLength: " + this.lengthFieldLength + " (expected: 1, 2, 3, 4, or 8)");
        }
        return frameLength;
	}

	private void failIfNecessary(boolean firstDetectionOfTooLongFrame) {
		if (bytesToDiscard == 0) {
			// Reset to the initial state and tell the handlers that
			// the frame was too large.
			long tooLongFrameLength = this.tooLongFrameLength;
			this.tooLongFrameLength = 0;
			discardingTooLongFrame = false;
			if (!failFast || firstDetectionOfTooLongFrame) {
				fail(tooLongFrameLength);
			}
		} else {
			// Keep discarding and notify handlers if necessary.
			if (failFast && firstDetectionOfTooLongFrame) {
				fail(tooLongFrameLength);
			}
		}
	}

	/**
	 * Extract the sub-region of the specified buffer.
	 */
	protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length) {
		return buffer.retainedSlice(index, length);
	}

	private void fail(long frameLength) {
		if (frameLength > 0) {
			throw new TooLongFrameException(
					"Adjusted frame length exceeds " + maxFrameLength + ": " + frameLength + " - discarded");
		} else {
			throw new TooLongFrameException("Adjusted frame length exceeds " + maxFrameLength + " - discarding");
		}
	}
}