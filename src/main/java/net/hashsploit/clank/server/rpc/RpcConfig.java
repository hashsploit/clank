package net.hashsploit.clank.server.rpc;

public class RpcConfig {

	private final String address;
	private final int port;
	
	public RpcConfig(final String address, final int port) {
		this.address = address;
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}
	
}
