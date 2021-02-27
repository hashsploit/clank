package net.hashsploit.clank.server.dme;

import java.nio.ByteOrder;
import java.util.logging.Logger;

import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.ClientState;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.pipeline.RtFrameDecoderHandler;
import net.hashsploit.clank.server.pipeline.TestHandlerDmeTcp;
import net.hashsploit.clank.server.pipeline.TimeoutHandler;

public class DmeTcpClient extends DmeClient {

	private static final Logger logger = Logger.getLogger(DmeTcpClient.class.getName());
	
	private final DmeServer dmeServer;
	private final SocketChannel channel;
	private final DmePlayer player;
	
	public DmeTcpClient(DmeServer dmeServer, SocketChannel ch) {
		this.dmeServer = dmeServer;
		this.channel = ch;
		this.player = new DmePlayer(this);

		logger.info("Client connected: " + getIPAddress());

		channel.pipeline().addLast(new TimeoutHandler(this, 48));
		channel.pipeline().addLast(new RtFrameDecoderHandler(ByteOrder.LITTLE_ENDIAN, MediusConstants.MEDIUS_MESSAGE_MAXLEN.value, 1, 2, 0, 0, false));
		channel.pipeline().addLast("MediusTestHandlerDME", new TestHandlerDmeTcp(this));

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
