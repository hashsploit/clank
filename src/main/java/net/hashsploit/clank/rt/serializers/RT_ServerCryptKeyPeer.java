package net.hashsploit.clank.rt.serializers;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;

public class RT_ServerCryptKeyPeer extends RTMessage {

	private static final int RC_KEY_SIZE = 64;
	private byte[] key;
	
	public RT_ServerCryptKeyPeer(ByteBuf payload) {
		super(RtMessageId.SERVER_CRYPTKEY_PEER, payload.readableBytes(), payload);
		key = new byte[RC_KEY_SIZE];
		payload.readBytes(key);
	}
	
	/**
	 * Returns the key that will be sent back to the 
	 * @return
	 */
	public byte[] getRCKey() {
		return key;
	}

	@Override
	public String toString() {
		return Utils.generateDebugPacketString(RT_ServerCryptKeyPeer.class.getName(),
			new String[] {
				"key"
			},
			new String[] {
				Utils.bytesToHex(key)
			}
		);
	}
}
