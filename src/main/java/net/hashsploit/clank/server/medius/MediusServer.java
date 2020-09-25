package net.hashsploit.clank.server.medius;

import net.hashsploit.clank.ServerComponent;
import net.hashsploit.clank.server.MediusClientChannelInitializer;
import net.hashsploit.clank.server.TcpServer;

public class MediusServer extends TcpServer {
	
	private final ServerComponent component;
	
	public MediusServer(final ServerComponent component, final String address, final int port, final int parentThreads, final int childThreads) {
		super(address, port, parentThreads, childThreads);
		this.component = component;
		
		
		
		
		
		setChannelInitializer(new MediusClientChannelInitializer(this));
	}
	
	@Override
	public void start() {
		super.start();
		
	}
	
	
	@Override
	public void stop() {
		
		super.stop();
	}
	
	/**
	 * Get the server's component type.
	 * @return
	 */
	public ServerComponent getComponent() {
		return component;
	}
	
}
