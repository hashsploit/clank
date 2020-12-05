package net.hashsploit.clank.server.dme;

import java.util.logging.Logger;

import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.ClientState;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.IServer;
import net.hashsploit.clank.server.pipeline.TestHandlerDmeTcp;
import net.hashsploit.clank.server.pipeline.TestHandlerDmeUdp;

public class DmeUdpClient implements IClient {
	private static final Logger logger = Logger.getLogger(DmeTcpClient.class.getName());

	private final IServer server;
	private final DatagramChannel channel;
	
	public DmeUdpClient(IServer server, DatagramChannel ch) {
		this.server = server;
		this.channel = ch;
		
		logger.info("Client connected: " + getIPAddress());

		channel.pipeline().addLast("MediusTestHandlerDMEudp", new TestHandlerDmeUdp(this));
	}
	
	
	public SocketChannel getSocket() {
		return null;
	}
	
	public DatagramChannel getDatagram() {
		return channel;
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

	public IServer getServer() {
		// TODO Auto-generated method stub
		return server;
	}
}
