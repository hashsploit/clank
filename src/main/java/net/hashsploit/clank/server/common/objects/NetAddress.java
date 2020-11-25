package net.hashsploit.clank.server.common.objects;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.scert.SCERTConstants;
import net.hashsploit.clank.server.scert.SCERTObject;
import net.hashsploit.clank.utils.Utils;

public class NetAddress extends SCERTObject {
	
	public final NetAddressType type;
	public final String address;
	public final int port;
	
	public NetAddress(final NetAddressType type, final String address, final int port) {
		this.type = type;
		this.address = address;
		this.port = port;
	}

	public NetAddressType getType() {
		return type;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}
	
	@Override
	public byte[] serialize() {
		ByteBuffer buffer = ByteBuffer.allocate(4 + SCERTConstants.NET_MAX_NETADDRESS_LENGTH.getValue() + 4);
		
		buffer.put(Utils.intToBytesLittle(type.getValue()));
		
		final byte[] ipAddr = address.getBytes();
		final int numZeros = 16 - address.length();
		final String zeroString = new String(new char[numZeros]).replace("\0", "00");
		final byte[] zeroTrail = Utils.hexStringToByteArray(zeroString);
		
		buffer.put(ipAddr);
		buffer.put(zeroTrail);
		buffer.put(Utils.intToBytesLittle(port));
		
		return buffer.array();
	}
	
	public void deserialize(byte[] data) {
		
	}
	
}
