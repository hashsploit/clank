package net.hashsploit.clank.crypto;

import net.hashsploit.clank.utils.Utils;

/**
 * Highest 3 bits of the hash/checksum.
 * Denotes which key used to sign the message.
 * @author Dnawrkshp
 *
 */
public enum CipherContext {

	ID_00(0x00),
	RC_SERVER_SESSION(0x01),
	ID_02(0x02),
	RC_CLIENT_SESSION(0x03),
	ID_04(0x04),
	ID_05(0x05),
	ID_06(0x06),
	RSA_AUTH(0x07);
	
	private final byte id;
	
	private CipherContext(int id) {
		this.id = (byte) id;
	}
	
	public final byte getByte() {
		return id;
	}
	
	@Override
	public String toString() {
		return "0x" + Utils.byteToHex(getByte());
	}
	
}
