package net.hashsploit.clank.server.dme;

import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.ClientState;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.IServer;

public class DmeClient implements IClient {

	private final IServer server;
	private final SocketChannel channel;
	
	public DmeClient(IServer server, SocketChannel ch) {
		this.server = server;
		this.channel = ch;
		
		
		
	}

	@Override
	public String getIPAddress() {
		return null;
	}

	@Override
	public int getPort() {
		return 0;
	}

	@Override
	public ClientState getClientState() {
		return null;
	}
	
}
