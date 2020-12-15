package net.hashsploit.clank.server.rpc;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;

public abstract class AbstractRpcServer {
	
	private static final Logger logger = Logger.getLogger(AbstractRpcServer.class.getName());

	private final String address;
	private final int port;
	private final ServerBuilder<?> builder;
	private Server server;

	public AbstractRpcServer(String address, int port) {
		this.address = address;
		this.port = port;
		builder = NettyServerBuilder.forAddress(new InetSocketAddress(address, port));

		// TODO: builder.useTransportSecurity(certChain, privateKey);
	}

	/**
	 * Add a service to this RPC Server.
	 * 
	 * @param service
	 */
	protected void addService(BindableService bindableService) {
		builder.addService(bindableService);
	}

	/**
	 * Add encryption to the transport layer.
	 * 
	 * @param key
	 */
	public void setupEncryption(InputStream certChain, InputStream privateKey) {
		builder.useTransportSecurity(certChain, privateKey);
	}
	
	/**
	 * Start the RPC server.
	 */
	public void start() {
		try {
			server = builder.build();
			server.start();
			logger.info(String.format("RPC server started on %s:%d", address, port));
		} catch (IOException e) {
			logger.severe(String.format("Failed to start the RPC service %s:%d: %s", address, port, e.getMessage()));
		}
	}

	/**
	 * Shutdown the RPC server.
	 */
	public void stop() {
		server.shutdown();
		logger.info(String.format("RPC server on %s:%d closed", address, port));
	}

	/**
	 * Get the RPC Server's address.
	 * 
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Get the RPC Server's port.
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Get the RPC Server.
	 * 
	 * @return
	 */
	public Server getGrpcServer() {
		return server;
	}

}
