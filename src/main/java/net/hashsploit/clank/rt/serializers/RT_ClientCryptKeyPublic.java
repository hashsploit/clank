package net.hashsploit.clank.rt.serializers;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.utils.Utils;

public class RT_ClientCryptKeyPublic extends RTMessage {

	private static final int RSA_KEY_SIZE = 64;
	private final byte[] key;

	public RT_ClientCryptKeyPublic(ByteBuf payload) {
		super(payload);
		key = new byte[RSA_KEY_SIZE];
		payload.readBytes(key);
	}

	/**
	 * Returns the RSA key the client sent to us encrypted.
	 * @return
	 */
	public byte[] getRSAKey() {
		return key;
	}
	
	@Override
	public String toString() {
		return Utils.generateDebugPacketString(RT_ClientCryptKeyPublic.class.getName(),
			new String[] {
				"key"
			},
			new String[] {
				Utils.bytesToHex(key)
			}
		);
	}

}
