package net.hashsploit.clank.server.dme;

import io.netty.channel.EventLoopGroup;
import net.hashsploit.clank.server.UdpServer;

public class DmeUdpServer extends UdpServer {

	private final DmeWorldManager dmeWorldManager;

	public DmeUdpServer(final String address, final int port, final EventLoopGroup eventLoopGroup, DmeWorldManager dmeWorldManager) {
		super(address, port, eventLoopGroup);
		this.dmeWorldManager = dmeWorldManager;
	}
	
	public DmeWorldManager getDmeWorldManager() {
		return dmeWorldManager;
	}
	

}
