package net.hashsploit.clank.server;

public interface IServer {
	
	/**
	 * Start the server.
	 */
	public void start();
	
	/**
	 * Stop the server.
	 */
	public void stop();
	
	/**
	 * Get the number of parent threads.
	 * @return
	 */
	public int getParentThreads();
	
	/**
	 * Get the number of child threads.
	 * @return
	 */
	public int getChildThreads();
	
}
