package net.hashsploit.clank.rt.serializers;

import java.util.Arrays;

import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;

public class RT_ClientCryptKeyPublic extends RTMessage {

	private byte[] key;
	
	public RT_ClientCryptKeyPublic(byte[] packetData) {
		super(RtMessageId.CLIENT_CRYPTKEY_PUBLIC, Arrays.copyOfRange(packetData, 3, packetData.length));
		key = this.getPayload();
	}

	@Override
	public String toString() {
		return "RT_ClientCryptKeyPublic: \n" + 
				"key: " + Utils.bytesToHex(key);
	}



}
