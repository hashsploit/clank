package net.hashsploit.clank.server.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.hashsploit.clank.server.IClient;

public class TimeoutHandler extends ReadTimeoutHandler {

	private IClient client;
	
	public TimeoutHandler(IClient client, int timeoutSeconds) {
		super(timeoutSeconds);
		this.client = client;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof ReadTimeoutException) {
			client.onDisconnect();
		} else {
			super.exceptionCaught(ctx, cause);
		}
	}
	
}
