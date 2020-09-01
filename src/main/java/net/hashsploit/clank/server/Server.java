package net.hashsploit.clank.server;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.hashsploit.clank.MediusComponent;

public class Server implements IServer {

	private static final Logger logger = Logger.getLogger("");
	
	private final MediusComponent component;
	private final String address;
	private final int port;
	private final int parentThreads;
	private final int childThreads;
	private final Class<? extends ServerChannel> socketChannelClass;
	private final EventLoopGroup eventParentGroup;
	private final EventLoopGroup eventChildGroup;
	private final ClientChannelInitializer clientChannelInit;
	
	private ChannelFuture channelFuture;
	private HashSet<Client> clients;

	public Server(final MediusComponent component, final String address, final int port, final int parentThreads, final int childThreads) {
		this.component = component;
		this.port = port;
		this.parentThreads = parentThreads;
		this.childThreads = childThreads;
		this.clients = new HashSet<Client>();
		
		if (address.isEmpty()) {
			this.address = "0.0.0.0";
		} else {
			this.address = address;
		}

		// Disable Netty from clogging up the console logs.
		Logger.getLogger("io.netty").setLevel(Level.OFF);
		
		// Use Linux epoll if available.
		if (Epoll.isAvailable()) {
			logger.fine("Linux epoll is available!");
			socketChannelClass = EpollServerSocketChannel.class;
			eventParentGroup = new EpollEventLoopGroup(parentThreads);
			eventChildGroup = new EpollEventLoopGroup(childThreads);
		} else {
			socketChannelClass = NioServerSocketChannel.class;
			eventParentGroup = new NioEventLoopGroup(parentThreads);
			eventChildGroup = new NioEventLoopGroup(childThreads);
		}
		
		clientChannelInit = new ClientChannelInitializer(this);
	}

	/**
	 * Start this server.
	 */
	public void start() {
		
		logger.info(String.format("Starting %s with %d parent threads and %d child threads ...", component.toString(), parentThreads, childThreads));
		
		try {
			final ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(eventParentGroup, eventChildGroup)
				.channel(socketChannelClass)
				.childHandler(clientChannelInit)
				.childOption(ChannelOption.AUTO_CLOSE, true)
				.childOption(ChannelOption.SO_KEEPALIVE, false);

			channelFuture = bootstrap.bind(address, port).sync();
			
			if (channelFuture.isSuccess()) {
				logger.info(String.format("Server started on %s:%d", address, port));
			}
			
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			logger.info("Closing sockets ...");
			eventParentGroup.shutdownGracefully();
			eventChildGroup.shutdownGracefully();
		}

	}

	/**
	 * Shutdown this server.
	 */
	public void stop() {
		eventParentGroup.shutdownGracefully().awaitUninterruptibly(1000);
		eventChildGroup.shutdownGracefully().awaitUninterruptibly(1000);
		channelFuture.channel().close();
		clients.clear();
	}
	
	/**
	 * Get the Medius Component.
	 * @return
	 */
	public MediusComponent getComponent() {
		return component;
	}

	/**
	 * Get the address this server is bound to.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Get the port number this server is running on.
	 * @return
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Get the number of parent threads dedicated to this server.
	 */
	public int getParentThreads() {
		return parentThreads;
	}
	
	/**
	 * Get the number of child threads dedicated to this server.
	 */
	public int getChildThreads() {
		return childThreads;
	}
	
	/**
	 * Get all the clients.
	 * @return
	 */
	public HashSet<Client> getClients() {
		return clients;
	}
	
	/**
	 * Add a client to the total clients.
	 * @param client
	 */
	protected void addClient(Client client) {
		clients.add(client);
	}
	
	/**
	 * Remove a client from the total clients.
	 * @param client
	 */
	protected void removeClient(Client client) {
		clients.remove(client);
	}

}
