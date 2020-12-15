package net.hashsploit.clank.server;

import java.util.HashSet;
import java.util.logging.Logger;

public abstract class AbstractServer implements IServer {

	private static final Logger logger = Logger.getLogger(AbstractServer.class.getName());

	private final String address;
	private final int port;
	private HashSet<IClient> clients;

	public AbstractServer(final String address, final int port) {
		this.port = port;
		this.clients = new HashSet<IClient>();

		if (address == null || address.isEmpty()) {
			this.address = "0.0.0.0";
		} else {
			this.address = address;
		}

	}

	/**
	 * Start this server.
	 */
	public void start() {
		
	}

	/**
	 * Shutdown this server.
	 */
	public void stop() {
		clients.clear();
	}

	/**
	 * Get the address this server is bound to.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Get the port number this server is running on.
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Get all the clients.
	 * 
	 * @return
	 */
	@Override
	public HashSet<IClient> getClients() {
		return clients;
	}

	/**
	 * Add a client to the total clients.
	 * 
	 * @param client
	 */
	@Override
	public void addClient(IClient client) {
		clients.add(client);
	}

	/**
	 * Remove a client from the total clients.
	 * 
	 * @param client
	 */
	@Override
	public void removeClient(IClient client) {
		clients.remove(client);
	}

}
