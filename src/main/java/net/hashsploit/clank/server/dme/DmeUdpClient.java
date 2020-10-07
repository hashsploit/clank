package net.hashsploit.clank.server.dme;

import io.netty.channel.socket.DatagramChannel;
import net.hashsploit.clank.server.ClientState;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.IServer;

public class DmeUdpClient implements IClient {

	private final IServer server;
	private final DatagramChannel channel;
	
	public DmeUdpClient(IServer server, DatagramChannel ch) {
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
