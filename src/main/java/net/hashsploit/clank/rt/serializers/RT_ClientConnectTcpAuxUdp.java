package net.hashsploit.clank.rt.serializers;

import java.util.Arrays;

import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.utils.Utils;

public class RT_ClientConnectTcpAuxUdp {

	private final short dmeWorldId;
	private final byte[] mlsToken;
	
	public RT_ClientConnectTcpAuxUdp(RTMessage packet) {
		byte[] bytes = packet.toBytes();
		dmeWorldId = Utils.bytesToShortLittle(bytes[6], bytes[7]);
		mlsToken = Arrays.copyOfRange(bytes, bytes.length-17, bytes.length-1);
	}

	public synchronized short getDmeWorldId() {
		return dmeWorldId;
	}

	public byte[] getMlsToken() {
		return mlsToken;
	}

}
