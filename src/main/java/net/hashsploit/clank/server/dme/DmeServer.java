package net.hashsploit.clank.server.dme;

import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.server.TcpServer;
import net.hashsploit.clank.server.UdpServer;
import net.hashsploit.clank.server.common.MediusLogicHandler;

public class DmeServer extends TcpServer {

	private static final Logger logger = Logger.getLogger(DmeServer.class.getName());

	private final DmeWorld dmeWorld = new DmeWorld();

	
	private final String udpAddress;
	private final int udpStartingPort;
	private final int udpThreads;
	private HashSet<UdpServer> gameServers;

	public DmeServer(final String tcpAddress, final int tcpPort, final int tcpParentThreads, final int tcpChildThreads, final String udpAddress, final int udpStartingPort, final int udpThreads) {
		super(tcpAddress, tcpPort, tcpParentThreads, tcpChildThreads);

		this.udpAddress = udpAddress;
		this.udpStartingPort = udpStartingPort;
		this.udpThreads = udpThreads;
		this.gameServers = new HashSet<UdpServer>();

		String udpServerAddress = ((DmeConfig) Clank.getInstance().getConfig()).getUdpAddress();
		int udpServerPort = ((DmeConfig) Clank.getInstance().getConfig()).getUdpStartingPort() + 1;

		EventLoopGroup udpEventLoopGroup = new EpollEventLoopGroup(2);
		Executors.newSingleThreadExecutor().execute(() -> { // TODO: this is super tempoarary
			UdpServer udpDmeServer = new UdpServer(udpServerAddress, udpServerPort, udpEventLoopGroup);
			gameServers.add(udpDmeServer);
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
	 * Get all the UDP game servers currently running.
	 * 
	 * @return
	 */
	public HashSet<UdpServer> getGameServers() {
		return gameServers;
	}
	
	public DmeWorld getDmeWorld() {
		return dmeWorld;
	}

}
