package net.hashsploit.clank.server.pipeline;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import net.hashsploit.clank.server.nat.NatClient;
import net.hashsploit.clank.utils.Utils;

public class TestHandlerNATUdp extends ChannelInboundHandlerAdapter {

	private static final Logger logger = Logger.getLogger(TestHandlerNATUdp.class.getName());
	private NatClient client;

	public TestHandlerNATUdp(final NatClient client) {
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

		final DatagramPacket datagram = (DatagramPacket) msg;

		//byte[] buff = new byte[datagram.content().readableBytes()];
		//datagram.content().readBytes(buff);
		
		logger.finest(Utils.generateDebugPacketString("NAT UDP Payload",
			new String[] {
			"Payload"
			},
			new String[] {
				""
				//Utils.bytesToHex(datagram.content())
			}
		));
		
		if (datagram.content().readableBytes() == 4 && datagram.content().getByte(datagram.content().readerIndex() + 3) != 0xD4) {
			ByteBuf buffer = ctx.alloc().buffer(6);
			buffer.writeBytes(datagram.sender().getAddress().getHostAddress().getBytes());
			buffer.writeShort(datagram.sender().getPort());
			ctx.writeAndFlush(new DatagramPacket(buffer, datagram.sender()));
		}

	}

}
