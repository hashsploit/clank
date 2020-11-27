package net.hashsploit.clank.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpServer extends AbstractServer {
	
	private static final Logger logger = Logger.getLogger(UdpServer.class.getName());
	private static final int SOCKET_TIMEOUT = 300;
	
	private final Class<? extends DatagramChannel> datagramChannelClass;
	private final int threads;
	private EventLoopGroup eventLoopGroup;
	private ChannelFuture channelFuture;
	private ChannelInitializer<DatagramChannel> channelInitializer;
	
	
	public UdpServer(final String address, final int port, final EventLoopGroup eventLoopGroup) {
		super(address, port);
		
		this.threads = -1;
		this.eventLoopGroup = eventLoopGroup;
		
		// Use Linux epoll if available.
		if (Epoll.isAvailable()) {
			logger.fine("Linux epoll is available. Using epoll for datagram channels.");
			datagramChannelClass = EpollDatagramChannel.class;
		} else {
			datagramChannelClass = NioDatagramChannel.class;
		}
		
	}
	
	public UdpServer(final String address, final int port, final int threads) {
		super(address, port);
		
		this.threads = threads;
		
		// Disable Netty from clogging up the console logs.
		Logger.getLogger("io.netty").setLevel(Level.OFF);
		
		final EventLoopGroup localEventLoopGroup;
		
		// Use Linux epoll if available.
		if (Epoll.isAvailable()) {
			logger.fine("Linux epoll is available. Using epoll for datagram channels.");
			datagramChannelClass = EpollDatagramChannel.class;
			localEventLoopGroup = new EpollEventLoopGroup(threads);
		} else {
			datagramChannelClass = NioDatagramChannel.class;
			localEventLoopGroup = new NioEventLoopGroup(threads);
		}
		
		this.eventLoopGroup = localEventLoopGroup;
	}
	
	
	@Override
	public void start() {
		super.start();
		
		if (threads != -1) {
			logger.info(String.format("Starting UDP server with %d thread(s) ...", threads));
		}
		
		try {
			final Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup)
				.channel(datagramChannelClass)
				.handler(channelInitializer);
			
			channelFuture = bootstrap.bind(this.getAddress(), this.getPort()).sync();
			
			if (channelFuture.isSuccess()) {
				logger.info(String.format("UDP server started on %s:%d", this.getAddress(), this.getPort()));
			}
			
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			logger.info("Shutting down UDP server ...");
			stop();
		}
	}

	@Override
	public void stop() {
		eventLoopGroup.shutdownGracefully().awaitUninterruptibly(SOCKET_TIMEOUT);
		channelFuture.channel().close();
		super.stop();
	}
	
	public void setChannelInitializer(final ChannelInitializer<DatagramChannel> channelInitializer) {
		this.channelInitializer = channelInitializer;
	}
	
}
