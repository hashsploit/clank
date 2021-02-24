package net.hashsploit.clank.server;

public interface IClient {

	/**
	 * Get the client's IP Address as a string.
	 * @return
	 */
	public String getIPAddress();

	/**
	 * Get the client's remote port.
	 * @return
	 */
	public int getPort();
	
	/**
	 * Get the current client's state.
	 * @return
	 */
	public ClientState getClientState();
	
	
	/**
	 * Called on a client's disconnect.
	 */
	public void onDisconnect();
	
}
