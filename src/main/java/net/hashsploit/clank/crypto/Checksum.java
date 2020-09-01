package net.hashsploit.clank.crypto;

import org.bouncycastle.crypto.digests.SHA1Digest;

public class Checksum {

	// Prevent instantiation
	private Checksum() {}
	
	public static byte[] hash(byte[] input, CipherContext cipherContext) {
		return hash(input, 0, input.length, 0, cipherContext.getByte());
	}

	public static byte[] hash(byte[] input, int inOff, int length, int outOff, byte packetId) {
		byte[] result = new byte[20];
		byte[] output = new byte[4];

		// Compute sha1 hash
		SHA1Digest digest = new SHA1Digest();
		digest.update(input, inOff, length);
		digest.doFinal(result, 0);

		// Inject context inter highest 3 bits
		result[3] = (byte) ((result[3] & 0x1F) | ((packetId & 7) << 5));

		System.arraycopy(result, 0, output, outOff, 4);

		return output;
	}

}
