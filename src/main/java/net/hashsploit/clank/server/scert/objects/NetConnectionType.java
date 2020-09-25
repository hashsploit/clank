package net.hashsploit.clank.server.scert.objects;

public enum NetConnectionType {
	
	/**
	 * No information is present.
	 */
	NET_CONNECTION_NONE(0),
	
	/**
	 * Connection to a server via TCP.
	 */
	NET_CONNECITON_TYPE_CLIENT_SERVER_TCP(1),
	
	/**
	 * Connection to another peer via UDP.
	 */
	NET_CONNECITON_TYPE_PEER_TO_PEER_UDP(2),
	
	/**
	 * Connection to a server via TCP and UDP. UDP is normal no reliability or order.
	 */
	NET_CONNECTION_TYPE_CLIENT_SERVER_TCP_AUX_UDP(3),
	
	/**
	 * Connection to a server via TCP, used for SCE-RT "spectator" functionality.
	 */
	NET_CONNECTION_TYPE_CLIENT_LISTENER_TCP(4);
	
	private final int value;

	private NetConnectionType(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
