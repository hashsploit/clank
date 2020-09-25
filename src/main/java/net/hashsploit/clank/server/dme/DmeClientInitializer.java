package net.hashsploit.clank.server.dme;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutException;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.IServer;

public class DmeClientInitializer extends ChannelInitializer<SocketChannel> {
	
	private final IServer server;
	
	public DmeClientInitializer(IServer server) {
		super();
		this.server = server;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) {
		
		// TODO: Pull bytebuffer allocator from config
		ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(2048));
		
		
		final DmeClient client = new DmeClient(server, ch);
		server.addClient(client);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		
		if (!(e.getCause() instanceof ReadTimeoutException)) {
			Clank.getInstance().getTerminal().handleException(e);
	    }
		
		ctx.channel().close().awaitUninterruptibly();
	}
	
}
