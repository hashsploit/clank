package net.hashsploit.clank.rt.serializers;

import java.util.Arrays;

import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;

public class RT_ServerCryptKeyPeer extends RTMessage {

	private byte[] key;
	
	public RT_ServerCryptKeyPeer(byte[] packetData) {
		super(RtMessageId.SERVER_CRYPTKEY_PEER, Arrays.copyOfRange(packetData, 3, packetData.length));
		key = this.getPayload();
	}

	@Override
	public String toString() {
		return "RT_ServerCryptKeyPeer: \n" + 
				"key: " + Utils.bytesToHex(key);
	}
}
