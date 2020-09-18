package net.hashsploit.clank.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutException;
import net.hashsploit.clank.Clank;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	private final Server server;
	
	public ClientChannelInitializer(Server server) {
		super();
		this.server = server;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) {
		// We might need this later...
        ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(2048)); //set  buf size here
		final Client client = new Client(server, ch);
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
