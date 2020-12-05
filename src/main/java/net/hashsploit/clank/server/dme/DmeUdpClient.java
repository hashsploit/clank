package net.hashsploit.clank.server.dme;

import java.util.logging.Logger;

import io.netty.channel.socket.DatagramChannel;
import net.hashsploit.clank.server.ClientState;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.IServer;
import net.hashsploit.clank.server.pipeline.TestHandlerDmeTcp;
import net.hashsploit.clank.server.pipeline.TestHandlerDmeUdp;

public class DmeUdpClient implements IClient {

	private static final Logger logger = Logger.getLogger(DmeUdpClient.class.getName());

	private final IServer server;
	private final DatagramChannel channel;

	public DmeUdpClient(IServer server, DatagramChannel ch) {
		this.server = server;
		this.channel = ch;

		logger.info("Client connected: " + getIPAddress());

		channel.pipeline().addLast("TestHandlerDmeUdp", new TestHandlerDmeUdp(this));
	}

	@Override
	public String getIPAddress() {
		// FIXME: this must return the remote client's address
		//return channel.remoteAddress().getAddress().getHostAddress();
		return null;
	}

	@Override
	public int getPort() {
		// FIXME: this must return the remote client's port
		//return channel.remoteAddress().getPort();
		return 0;
	}

	@Override
	public ClientState getClientState() {
		return null;
	}

}
