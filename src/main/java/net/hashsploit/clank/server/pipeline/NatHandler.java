package net.hashsploit.clank.server.pipeline;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import net.hashsploit.clank.server.nat.NatClient;
import net.hashsploit.clank.utils.Utils;

public class NatHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = Logger.getLogger(NatHandler.class.getName());
	private NatClient client;

	public NatHandler(final NatClient client) {
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

		// Don't waste resources on bogus requests
		if (datagram.content().readableBytes() > 4) {
			return;
		}
		
		byte[] buff = new byte[datagram.content().readableBytes()];
		datagram.content().readBytes(buff);
		
		// Send ip and port back if the last byte isn't 0xD4
		if (buff.length == 4 && buff[3] != 0xD4) {
			logger.info(String.format("NAT peer request from %s:%d", datagram.sender().getAddress().getHostAddress(), datagram.sender().getPort()));
			
			ByteBuf buffer = ctx.alloc().buffer(6);
			byte[] ipAddrBytes = new byte[4];
			
			String[] parts = datagram.sender().getAddress().getHostAddress().toString().split("\\.");

			for (int i = 0; i < 4; i++) {
			    ipAddrBytes[i] = (byte) (Byte.parseByte(parts[i]) & 0xFF);
			}
			
			buffer.writeBytes(ipAddrBytes);
			buffer.writeShort(datagram.sender().getPort());
			
			byte[] responseArray = new byte[buffer.readableBytes()];
			
			if (buffer.hasArray()) {
			    responseArray = buffer.array();
			} else {
			    buffer.getBytes(buffer.readerIndex(), responseArray);
			}
			
			ctx.writeAndFlush(new DatagramPacket(buffer, datagram.sender()));
			
			logger.finest(Utils.generateDebugPacketString("NAT UDP Peer Request",
				new String[] {
					"Request Payload",
					"Response Payload"
				},
				new String[] {
					Utils.bytesToHex(buff),
					Utils.bytesToHex(responseArray)
				}
			));
		} else {
			logger.finest(String.format("Generic ping from %s:%d", datagram.sender().getAddress().getHostAddress(), datagram.sender().getPort()));
		}
		
		

	}

}
