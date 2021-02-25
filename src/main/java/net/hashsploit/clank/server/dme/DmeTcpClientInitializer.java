package net.hashsploit.clank.server.dme;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.SocketChannel;

public class DmeTcpClientInitializer extends ChannelInitializer<SocketChannel> {

	private final DmeServer dmeServer;
	
	public DmeTcpClientInitializer(DmeServer dmeServer) {
		this.dmeServer = dmeServer;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		
		// Set channel buffer size
		ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(4096));
		
		final DmeTcpClient client = new DmeTcpClient(dmeServer, ch);
		dmeServer.addClient(client);
	}

}
