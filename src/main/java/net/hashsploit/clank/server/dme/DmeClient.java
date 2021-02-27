package net.hashsploit.clank.server.dme;

import net.hashsploit.clank.server.ClientState;
import net.hashsploit.clank.server.IClient;

public abstract class DmeClient implements IClient {

	public DmeClient() {
		
	}
	
	@Override
	public abstract String getIPAddress();

	@Override
	public abstract int getPort();

	@Override
	public abstract ClientState getClientState();

	@Override
	public abstract void onDisconnect();
	
}
