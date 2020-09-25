package net.hashsploit.clank.server.nat;

import java.util.logging.Logger;

import net.hashsploit.clank.server.UdpServer;
import net.hashsploit.clank.server.dme.DmeServer;

public class NatServer extends UdpServer {
	
	private static final Logger logger = Logger.getLogger(DmeServer.class.getName());
	
	public NatServer(String address, int port, int threads) {
		super(address, port, threads);
		
		
	}
	
	@Override
	public void start() {
		super.start();
		
		
	}
	
	
	@Override
	public void stop() {
		
		
		super.stop();
	}
	
}
