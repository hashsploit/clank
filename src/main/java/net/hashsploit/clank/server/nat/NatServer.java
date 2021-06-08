package net.hashsploit.clank.server.nat;

import java.util.logging.Logger;

import net.hashsploit.clank.server.UdpServer;
import net.hashsploit.clank.utils.Utils;

public class NatServer extends UdpServer {
	
	private static final Logger logger = Logger.getLogger(NatServer.class.getName());
	private String publicIp;
	
	public NatServer(String address, int port, int threads) {
		super(address, port, threads);
		setChannelInitializer(new NatClientInitializer(this));
		publicIp = Utils.getPublicIpAddress();
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	
	@Override
	public void stop() {
		super.stop();
	}
	
	public String getPublicIp() {
		return publicIp;
	}
	
}
