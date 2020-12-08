package net.hashsploit.clank.server.dme;

import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.server.MediusLogicHandler;
import net.hashsploit.clank.server.TcpServer;
import net.hashsploit.clank.server.UdpServer;

public class DmeServer extends TcpServer {

	private static final Logger logger = Logger.getLogger(DmeServer.class.getName());

	private final DmeWorldManager dmeWorldManager = new DmeWorldManager();

	private final String udpAddress;
	private final int udpStartingPort;
	private final int udpThreads;
	private DmeUdpServer udpDmeServer;

	public DmeServer(final String tcpAddress, final int tcpPort, final int tcpParentThreads, final int tcpChildThreads, final String udpAddress, final int udpStartingPort, final int udpThreads) {
		super(tcpAddress, tcpPort, tcpParentThreads, tcpChildThreads);

		this.udpAddress = udpAddress;
		this.udpStartingPort = udpStartingPort;
		this.udpThreads = udpThreads;

		String udpServerAddress = ((DmeConfig) Clank.getInstance().getConfig()).getUdpAddress();
		int udpServerPort = ((DmeConfig) Clank.getInstance().getConfig()).getUdpStartingPort();

		EventLoopGroup udpEventLoopGroup = new EpollEventLoopGroup(2);
		Executors.newSingleThreadExecutor().execute(() -> { // TODO: this is super tempoarary
			this.udpDmeServer = new DmeUdpServer(udpServerAddress, udpServerPort, udpEventLoopGroup, dmeWorldManager);
			udpDmeServer.setChannelInitializer(new DmeUdpClientInitializer(udpDmeServer));
			udpDmeServer.start();
		});

		setChannelInitializer(new DmeTcpClientInitializer(this));
	}

	@Override
	public void start() {
		super.start();

	}

	@Override
	public void stop() {
		/*
		logger.fine("Shutting down DME UDP game servers ...");
		for (final UdpServer server : gameServers) {
			logger.finest("Shutting down DME UDP game server on port " + server.getPort());
			server.stop();
		}
		*/
		super.stop();
	}
	
	/**
	 * Get the currently running gameserver
	 * 
	 * @return
	 */
	public UdpServer getUdpServer() {
		return udpDmeServer;
	}
	
	public DmeWorldManager getDmeWorldManager() {
		return dmeWorldManager;
	}

}
