package net.hashsploit.clank.server;

import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;

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
	
	public SocketChannel getSocket();
	
	public DatagramChannel getDatagram();

	
}
