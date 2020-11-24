package net.hashsploit.clank.server.common.objects;

public class NetAddressList {
	
	public static final int NET_ADDRESS_LIST_COUNT = 2;
	private final NetAddress[] addressList;
	
	public NetAddressList(NetAddress first, NetAddress second) {
		this.addressList = new NetAddress[NET_ADDRESS_LIST_COUNT];
		this.addressList[0] = first;
		this.addressList[1] = second;
	}
	
	public NetAddress getFirst() {
		return addressList[0];
	}
	
	public NetAddress getSecond() {
		return addressList[1];
	}
	
}
