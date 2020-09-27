package net.hashsploit.clank.server.dme;

import java.util.HashSet;
import java.util.logging.Logger;

import net.hashsploit.clank.server.TcpServer;
import net.hashsploit.clank.server.UdpServer;

public class DmeServer extends TcpServer {
	
	private static final Logger logger = Logger.getLogger(DmeServer.class.getName());
	
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
		
		setChannelInitializer(new DmeTcpClientInitializer(this));
	}
	
	@Override
	public void start() {
		super.start();
		
		
	}
	
	
	@Override
	public void stop() {
		logger.fine("Shutting down DME UDP game servers ...");
		for (final UdpServer server : gameServers) {
			logger.finest("Shutting down DME UDP game server on port " + server.getPort());
			server.stop();
		}
		super.stop();
	}

}
