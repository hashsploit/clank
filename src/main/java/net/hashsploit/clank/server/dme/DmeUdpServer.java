package net.hashsploit.clank.server.dme;

import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.server.TcpServer;
import net.hashsploit.clank.server.UdpServer;
import net.hashsploit.clank.server.common.MediusLogicHandler;

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
