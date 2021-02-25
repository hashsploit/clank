package net.hashsploit.clank.server.dme;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.DatagramChannel;

public class DmeUdpClientInitializer extends ChannelInitializer<DatagramChannel> {

	private final DmeServer dmeServer;
	
	public DmeUdpClientInitializer(DmeServer dmeServer) {
		this.dmeServer = dmeServer;
	}

	@Override
	protected void initChannel(DatagramChannel ch) throws Exception {
		
	}

}
