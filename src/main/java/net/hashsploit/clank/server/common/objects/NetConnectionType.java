package net.hashsploit.clank.server.common.objects;

public enum NetConnectionType {
	
	/**
	 * (0) No information is present.
	 */
	NET_CONNECTION_NONE(0),
	
	/**
	 * (1) Connection to TCP server.
	 */
	NET_CONNECTION_TYPE_CLIENT_SERVER_TCP(1),
	
	/**
	 * (2) Connection to another peer via UDP.
	 */
	NET_CONNECTION_TYPE_PEER_TO_PEER_UDP(2),
	
	/**
	 * (3) Connection to a TCP server with UDP aux.
	 */
	NET_CONNECTION_TYPE_CLIENT_SERVER_TCP_AUX_UDP(3),
	
	/**
	 * (4) Connection to a TCP spectator.
	 */
	NET_CONNECTION_TYPE_CLIENT_LISTENER_TCP(4);
	
	private final int value;
	
	private NetConnectionType(int value) {
		this.value = (byte) value;
	}
	
	public final int getValue() {
		return value;
	}
	
}
