package net.hashsploit.clank.server;

import java.util.HashSet;

public interface IServer {
	
	/**
	 * Start the server.
	 */
	public void start();
	
	/**
	 * Stop the server.
	 */
	public void stop();
	
	public HashSet<IClient> getClients();
	
	/**
	 * Add a client to the current server.
	 * @param client
	 */
	public void addClient(IClient client);
	
	/**
	 * Remove a client from the current server.
	 * @param client
	 */
	public void removeClient(IClient client);
	
}
