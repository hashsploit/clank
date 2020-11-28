package net.hashsploit.clank.server;

import java.util.logging.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.hashsploit.clank.utils.Utils;

public class TcpServer extends AbstractServer {
	
	private static final Logger logger = Logger.getLogger(TcpServer.class.getName());
	private static final int BACKLOG = 100;
	private static final int SOCKET_TIMEOUT = 100;
	
	private final Class<? extends ServerChannel> socketChannelClass;
	private final int parentThreads;
	private final int childThreads;
	
	private final EventLoopGroup parentEventLoopGroup;
	private final EventLoopGroup childEventLoopGroup;
	private ChannelInitializer<SocketChannel> channelInitializer;
	
	private ChannelFuture channelFuture;
	
	public TcpServer(final String address, final int port, final int parentThreads, final int childThreads) {
		super(address, port);
		
		this.parentThreads = parentThreads;
		this.childThreads = childThreads;

		// Use Linux epoll if available.
		if (Epoll.isAvailable()) {
			logger.fine("Linux epoll is available. Using epoll for socket channels.");
			socketChannelClass = EpollServerSocketChannel.class;
			parentEventLoopGroup = new EpollEventLoopGroup(parentThreads);
			childEventLoopGroup = new EpollEventLoopGroup(childThreads);
		} else {
			socketChannelClass = NioServerSocketChannel.class;
			parentEventLoopGroup = new NioEventLoopGroup(parentThreads);
			childEventLoopGroup = new NioEventLoopGroup(childThreads);
		}
	}

	@Override
	public void start() {
		super.start();
		
		logger.info(String.format("Starting TCP server with %d parent thread(s), and %d child thread(s) ...", parentThreads, childThreads));
		
		if (!Utils.tcpPortIsAvailable(this.getAddress(), this.getPort())) {
			throw new IllegalStateException(String.format("Port %s is currently in use!", this.getPort()));
		}
		
		try {
			final ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(parentEventLoopGroup, childEventLoopGroup)
				.channel(socketChannelClass)
				.option(ChannelOption.SO_BACKLOG, BACKLOG)
				.childHandler(channelInitializer)
				.childOption(ChannelOption.AUTO_CLOSE, true)
				.childOption(ChannelOption.SO_KEEPALIVE, false);
			
			channelFuture = bootstrap.bind(this.getAddress(), this.getPort()).sync();
			
			if (channelFuture.isSuccess()) {
				logger.info(String.format("TCP server started on %s:%d", this.getAddress(), this.getPort()));
			}
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			logger.info("Shutting down TCP server ...");
			stop();
		}
	}

	@Override
	public void stop() {
		parentEventLoopGroup.shutdownGracefully().awaitUninterruptibly(SOCKET_TIMEOUT);
		childEventLoopGroup.shutdownGracefully().awaitUninterruptibly(SOCKET_TIMEOUT);
		channelFuture.channel().close();
		super.stop();
	}
	
	public void setChannelInitializer(final ChannelInitializer<SocketChannel> channelInitializer) {
		this.channelInitializer = channelInitializer;
	}

}
