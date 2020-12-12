package net.hashsploit.clank.server.dme;

import java.util.logging.Logger;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.hashsploit.clank.server.ClientState;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.IServer;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.pipeline.TestHandlerDmeTcp;
import net.hashsploit.clank.server.rpc.PlayerUpdateRequest.PlayerStatus;
import net.hashsploit.clank.server.rpc.WorldUpdateRequest.WorldStatus;

public class DmeTcpClient implements IClient {
	private static final Logger logger = Logger.getLogger(DmeTcpClient.class.getName());

	private final IServer server;
	private final SocketChannel channel;
	
	public DmeTcpClient(IServer server, SocketChannel ch) {
		this.server = server;
		this.channel = ch;
		
		
		logger.info("Client connected: " + getIPAddress());

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
		int accountId = dmeServer.getDmeWorldManager().getPlayerId(channel);
		int worldId = dmeServer.getDmeWorldManager().getWorldId(accountId);
		
		DmeWorldManager mgr = dmeServer.getDmeWorldManager();
		
		// Delete player from world
		mgr.playerDisconnected(accountId, worldId);
		// Relay delete player to MLS
		dmeServer.getRpcClient().updatePlayer(accountId, worldId, PlayerStatus.DISCONNECTED); // 0 = disconnect

		// If the world is empty, delete it, and relay that info
		if (mgr.worldIsEmpty(worldId)) {
			// Delete the world
			mgr.deleteWorld(worldId);
			// Send the world deletion to MLS
			dmeServer.getRpcClient().updateWorld(worldId, WorldStatus.DESTROYED_VALUE); // 3 = disconnect
		}
		
	}

	public void updatePlayer(int playerId, int status) {
		DmeServer dmeServer = (DmeServer) server;
		DmeWorldManager mgr = dmeServer.getDmeWorldManager();
		
		
	}
	
}
