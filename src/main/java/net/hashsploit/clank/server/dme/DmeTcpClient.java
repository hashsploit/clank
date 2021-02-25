package net.hashsploit.clank.server.dme;

import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.ClientState;
import net.hashsploit.clank.server.IClient;

public class DmeTcpClient implements IClient {

	public DmeTcpClient(DmeServer dmeServer, SocketChannel ch) {
		
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

	@Override
	public void onDisconnect() {
		
	}

}
