package net.hashsploit.clank.server.dme;

import java.util.concurrent.Executors;
import java.util.logging.Logger;

import net.hashsploit.clank.server.TcpServer;
import net.hashsploit.clank.server.UdpServer;
import net.hashsploit.clank.server.rpc.ClankDmeRpcClient;
import net.hashsploit.clank.server.rpc.RpcConfig;
import net.hashsploit.clank.utils.Utils;

public class DmeServer extends TcpServer {

	private static final Logger logger = Logger.getLogger(DmeServer.class.getName());

	private final ClankDmeRpcClient rpcClient;
	private UdpServer udpServer;

	public DmeServer(final String tcpAddress, final int tcpPort, final int tcpParentThreads, final int tcpChildThreads, final String udpAddress, final int udpPort, final int udpThreads, final RpcConfig rpcConfig) {
		super(tcpAddress, tcpPort, tcpParentThreads, tcpChildThreads);

		// If the thread pool count is <= 0, create a new thread per world rather than a
		// fixed pool size
		// if (udpThreads <= 0) {
		// udpThreads = 1;
		// }

		udpServer = new UdpServer(udpAddress, udpPort, udpThreads);
		udpServer.setChannelInitializer(new DmeUdpClientInitializer(this));
		this.setChannelInitializer(new DmeTcpClientInitializer(this));

		// Start RPC client
		String rpcAddress = rpcConfig.getAddress();
		final int rpcPort = rpcConfig.getPort();

		if (rpcAddress == null) {
			rpcAddress = Utils.getPublicIpAddress();
		}

		rpcClient = new ClankDmeRpcClient(rpcAddress, rpcPort);
	}

	@Override
	public void start() {
		
		// Create a new thread for the dedicated UDP server to run in and start it.
		Executors.newSingleThreadExecutor().execute(() -> {
			udpServer.start();
		});
		
		super.start();

	}

	@Override
	public void stop() {
		super.stop();
	}

	/**
	 * Get the current RPC client.
	 * 
	 * @return
	 */
	public ClankDmeRpcClient getRpcClient() {
		return rpcClient;
	}

	/**
	 * Get the current UDP server instance.
	 * 
	 * @return
	 */
	public UdpServer getUdpServer() {
		return udpServer;
	}

}
