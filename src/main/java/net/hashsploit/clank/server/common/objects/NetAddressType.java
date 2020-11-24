package net.hashsploit.clank.server.common.objects;

public enum NetAddressType {

	/**
	 * (0) This value is "Not in use"
	 */
	NET_ADDRESS_NONE(0),
	
	/**
	 * (1) ASCII string of client's public IPv4 address.
	 */
	NET_ADDRESS_TYPE_EXTERNAL(1),
	
	/**
	 * (2) ASCII string of client's private IPv4 address.
	 */
	NET_ADDRESS_TYPE_INTERNAL(2),
	
	/**
	 * (3) ASCII string representation of a NAT resolution server's IPv4 address.
	 */
	NET_ADDRESS_NAT_SERVICE(3),
	
	/**
	 * (4) 4-byte binary representation of client's public IPv4 address.
	 */
	NET_ADDRESS_TYPE_BINARY_EXTERNAL(4),
	
	/**
	 * (5) 4-byte binary representation of a client's public IPv4 address.
	 */
	NET_ADDRESS_TYPE_BINARY_INTERNAL(5),
	
	/**
	 * (6) 4-byte binary representation of a client's public IPv4 address.
     * The Port parameter contains a 2-byte virtual port in 2 high bytes and
     * the actual network port in the 2 low bytes.
	 */
	NET_ADDRESS_TYPE_BINARY_EXTERNAL_VPORT(6),
	
	/**
	 * (7) 4-byte binary representation of a client's public IPv4 address.
     * The Port parameter contains a 2-byte virtual port in 2 high bytes and
     * the actual network port in the 2 low bytes.
	 */
	NET_ADDRESS_TYPE_BINARY_INTERNAL_VPORT(7),
	
	/**
	 * (8) Contains two 4-byte binary representations of NAT resolution servers
     * IPv4 addresses stored back to back.
	 */
	NET_ADDRESS_TYPE_BINARY_NAT_SERVICES(8);
	
	private final int value;

	private NetAddressType(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
