package net.hashsploit.clank.server.nat;

import java.util.logging.Logger;

import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.ClientState;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.pipeline.NatHandler;

public class NatClient implements IClient {
	
	private static final Logger logger = Logger.getLogger(NatClient.class.getName());

	private final NatServer server;
	private final DatagramChannel channel;
	
	public NatClient(NatServer server, DatagramChannel ch) {
		this.server = server;
		this.channel = ch;
		
		channel.pipeline().addLast("NatPipelineHandler", new NatHandler(this));
	}

	@Override
	public String getIPAddress() {
		return channel.remoteAddress().getAddress().getHostAddress();
	}

	@Override
	public int getPort() {
		return channel.remoteAddress().getPort();
	}

	@Override
	public ClientState getClientState() {
		return null;
	}

	@Override
	public void onDisconnect() {
		
	}
}
