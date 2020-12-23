package net.hashsploit.clank.server.pipeline;

import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.utils.Utils;

public class RtDecryptionHandler extends MessageToMessageDecoder<ByteBuf> {

	private static final Logger logger = Logger.getLogger(RtDecryptionHandler.class.getName());
	private MediusClient client;

	public RtDecryptionHandler(MediusClient client) {
		super();
		this.client = client;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf input, List<Object> output) throws Exception {
		final Object decoded = this.decode(ctx, input);
		if (decoded != null) {
			output.add(decoded);
		}
	}

	private Object decode(ChannelHandlerContext ctx, ByteBuf input) {
		byte id = input.getByte(input.readerIndex());
		byte[] hash = null;
		long frameLength = input.getShortLE(input.readerIndex() + 1);
		int totalLength = 3;

		logger.severe(RtDecryptionHandler.class.getName() + ": " + Utils.bytesToHex(Utils.byteBufToNioBuffer(input).array()));

		if (frameLength <= 0) {

			// return BaseScertMessage.Instantiate((RT_MSG_TYPE) (id & 0x7F), null, new byte[0], _getCipher);
		}

		if (id >= 0x80) {
			hash = new byte[4];
			input.getBytes(input.readerIndex() + 3, hash);
			totalLength += 4;
			id &= 0x7F;
		}

		if (frameLength < 0) {
			throw new CorruptedFrameException("negative pre-adjustment length field: " + frameLength);
		}

		// never overflows because it's less than maxFrameLength
		int frameLengthInt = (int) frameLength;
		if (input.readableBytes() < frameLengthInt) {
			input.resetReaderIndex();
			return null;
		}

		// extract frame
		byte[] messageContents = new byte[frameLengthInt];
		input.getBytes(input.readableBytes() + totalLength, messageContents);

		input.readerIndex(input.readableBytes() + totalLength + frameLengthInt);
		// return BaseScertMessage.Instantiate((RT_MSG_TYPE) id, hash, messageContents, _getCipher);
		return null;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		logger.warning(String.format("Exception in %s from %s: %s", RtDecryptionHandler.class.getName(), ctx.channel().remoteAddress().toString(), cause.getMessage()));
		ctx.close();
	}

}
