package net.hashsploit.clank.server.dme;

import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.ClientState;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.IServer;
import net.hashsploit.clank.server.pipeline.TestHandlerDmeTcp;

public class DmeTcpClient implements IClient {
	private static final Logger logger = Logger.getLogger(DmeTcpClient.class.getName());

	private final IServer server;
	private final SocketChannel channel;
	
	
	
	public DmeTcpClient(IServer server, SocketChannel ch) {
		this.server = server;
		this.channel = ch;
		
		
		logger.info("Client connected: " + getIPAddress());

		channel.pipeline().addLast("MediusTestHandlerDME", new TestHandlerDmeTcp(this));
	}
	
	public SocketChannel getSocket() {
		return channel;
	}
	
	public DatagramChannel getDatagram() {
		return null;
	}

	@Override
	public String getIPAddress() {
		return channel.remoteAddress().getAddress().getHostAddress();
	}

	@Override
	public int getPort() {
		return 0;
	}

	@Override
	public ClientState getClientState() {
		return null;
	}

	public byte[] getIPAddressAsBytes() {
		// TODO Auto-generated method stub
		return getIPAddress().getBytes();
	}

	public IServer getServer() {
		// TODO Auto-generated method stub
		return server;
	}
	
}
