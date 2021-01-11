package net.hashsploit.clank.server.dme;

import java.nio.ByteOrder;
import java.util.logging.Logger;

import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.hashsploit.clank.server.ClientState;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.IServer;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.pipeline.RtFrameDecoderHandler;
import net.hashsploit.clank.server.pipeline.TestHandlerDmeTcp;
import net.hashsploit.clank.server.rpc.PlayerStatus;
import net.hashsploit.clank.server.rpc.WorldUpdateRequest.WorldStatus;

public class DmeTcpClient implements IClient {
	private static final Logger logger = Logger.getLogger(DmeTcpClient.class.getName());

	private final IServer server;
	private final SocketChannel channel;
	private final DmePlayer player;
	
	public DmeTcpClient(IServer server, SocketChannel ch) {
		this.server = server;
		this.channel = ch;
		this.player = new DmePlayer(this);
		
		logger.info("Client connected: " + getIPAddress());
		channel.pipeline().addLast(new RtFrameDecoderHandler(ByteOrder.LITTLE_ENDIAN, MediusConstants.MEDIUS_MESSAGE_MAXLEN.getValue(), 1, 2, 0, 0, false));
		channel.pipeline().addLast("MediusTestHandlerDME", new TestHandlerDmeTcp(this));
		
		ChannelFuture closeFuture = channel.closeFuture();

		closeFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> future) throws Exception {
				onDisconnect();
			}
		});
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
	
	public void onDisconnect() {
		DmeServer dmeServer = (DmeServer) server;
		
		DmeWorldManager mgr = dmeServer.getDmeWorldManager();
		
		// Delete player from world
		int worldId = mgr.playerDisconnected(player);
		// Relay delete player to MLS
		dmeServer.getRpcClient().updatePlayer(player.getMlsToken(), worldId, PlayerStatus.DISCONNECTED); // 0 = disconnect

		// If the world is empty, delete it, and relay that info
		if (mgr.worldIsEmpty(worldId)) {
			// Delete the world
			mgr.deleteWorld(worldId);
			// Send the world deletion to MLS
			dmeServer.getRpcClient().updateWorld(worldId, WorldStatus.DESTROYED); // 3 = disconnect
		}
		
		logger.info("Player disconnected! WorldManager: ");
		logger.info(mgr.toString());
	}
	
	public void updateDmeWorld(int worldId, WorldStatus status) {
		DmeServer dmeServer = (DmeServer) server;
		dmeServer.getRpcClient().updateWorld(worldId, status);	
	}

	public void updateDmePlayer(String mlsToken, int worldId, PlayerStatus status) {
		DmeServer dmeServer = (DmeServer) server;
		dmeServer.getRpcClient().updatePlayer(mlsToken, worldId, status);		
	}

	public DmePlayer getPlayer() {
		return this.player;
	}
	
}
