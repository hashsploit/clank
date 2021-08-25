package net.hashsploit.clank.server.dme;

import java.util.concurrent.Executors;
import java.util.logging.Logger;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.config.objects.ServerInfo;
import net.hashsploit.clank.server.TcpServer;
import net.hashsploit.clank.server.UdpServer;
import net.hashsploit.clank.server.rpc.ClankDmeRpcClient;
import net.hashsploit.clank.server.rpc.RpcConfig;
import net.hashsploit.clank.utils.Utils;

public class DmeServer extends TcpServer {

	private static final Logger logger = Logger.getLogger(DmeServer.class.getName());

	private final DmeWorldManager dmeWorldManager = new DmeWorldManager();

	private final String udpAddress;
	private final int udpStartingPort;
	private final int udpThreads;
	private DmeUdpServer udpDmeServer;
	
	private final ClankDmeRpcClient rpcClient;

	public DmeServer(final String tcpAddress, final int tcpPort, final int tcpParentThreads, final int tcpChildThreads, final String udpAddress, final int udpStartingPort, final int udpThreads, final int timeout) {
		super(tcpAddress, tcpPort, tcpParentThreads, tcpChildThreads, timeout);

		this.udpAddress = udpAddress;
		this.udpStartingPort = udpStartingPort;
		this.udpThreads = udpThreads;

		String udpServerAddress = ((DmeConfig) Clank.getInstance().getConfig()).getUdpAddress();
		int udpServerPort = ((DmeConfig) Clank.getInstance().getConfig()).getUdpPort();

		Executors.newSingleThreadExecutor().execute(() -> { // TODO: this is super temporary
			
			EventLoopGroup udpEventLoopGroup = null;

			if (Epoll.isAvailable()) {
				udpEventLoopGroup = new EpollEventLoopGroup(4);
			} else {
				udpEventLoopGroup = new NioEventLoopGroup(4);
			}
			
			this.udpDmeServer = new DmeUdpServer(udpServerAddress, udpServerPort, udpEventLoopGroup, dmeWorldManager);
			udpDmeServer.setChannelInitializer(new DmeUdpClientInitializer(udpDmeServer));
			udpDmeServer.start();
		});

		setChannelInitializer(new DmeTcpClientInitializer(this));
		
		// Start RPC client
		final ServerInfo rpcConfig = ((DmeConfig) Clank.getInstance().getConfig()).getRpcConfig();
		String rpcAddress = rpcConfig.getAddress();
		final int rpcPort = rpcConfig.getPort();
		
		if (rpcAddress == null) {
			rpcAddress = Utils.getPublicIpAddress();
		}
		
		rpcClient = new ClankDmeRpcClient(rpcAddress, rpcPort);
		//rpcClient.updatePlayer(3, 3, PlayerStatus.ACTIVE);
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
	
	public ClankDmeRpcClient getRpcClient() {
		return rpcClient;
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
