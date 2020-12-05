package net.hashsploit.clank.server.common;

import io.netty.channel.ChannelHandlerContext;

public class PlayerConnection {

	private final ChannelHandlerContext ctx;
	
	public PlayerConnection(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
	public void write(byte[] data) {
		ctx.writeAndFlush(data);
	}
	
}
