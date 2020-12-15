package net.hashsploit.clank.server.rpc;

public class RpcServerConfig {
	
	private final String address;
	private final int port;
	private final boolean encryption;
	private final String certChainFile;
	private final String privateKeyFile;
	
	public RpcServerConfig(final String address, final int port, final boolean encryption, final String certChainFile, final String privateKeyFile) {
		this.address = address;
		this.port = port;
		this.encryption = encryption;
		this.certChainFile = certChainFile;
		this.privateKeyFile = privateKeyFile;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public boolean isEncryption() {
		return encryption;
	}

	public String getCertChainFile() {
		return certChainFile;
	}

	public String getPrivateKeyFile() {
		return privateKeyFile;
	}
	
}
