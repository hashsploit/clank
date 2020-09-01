package net.hashsploit.clank.crypto;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.utils.Utils;

/**
 * PlayStation 3's custom RCQ Medius implementation
 * 
 * @author hashsploit
 */
public class RCQ implements ICipher {

	private byte[] key = null;
	private CipherContext context;

	public RCQ(byte[] key, CipherContext context) {
		this.context = context;
		setKey(key);
	}

	public void setKey(byte[] key) {
		setKey(key);
	}

	@Override
	public MediusDecryptedMessage decrypt(byte[] data, byte[] hash) {

		if (key == null) {
			return null;
		}

		byte[] plain = new byte[data.length];
		System.arraycopy(data, 0, plain, 0, plain.length);

		// Check if empty hash
		// If hash is 0, the data is already in plaintext
		if (hash[0] == 0 && hash[1] == 0 && hash[2] == 0 && (hash[3] & 0x1F) == 0) {
			return new MediusDecryptedMessage(plain, true);
		}

		// IV
		byte[] iv = new byte[0x10];
		int[] seed = new int[4];
		System.arraycopy(key, 0, iv, 0, 0x10);
		
        for (int i = 0; i < 4; ++i) {
            //seed[i] = BitConverter.ToUInt32(iv, i * 4);
        	seed[i] = iv[i*4] & 0xFF;
        }
        
        hash = rcPass(hash, seed, false);

        for (int i = 0; i < 4; ++i) {
            //var b = BitConverter.GetBytes(seed[i]);
            //Array.Copy(b, 0, iv, i * 4, 4);
        	final ByteBuffer buffer = ByteBuffer.allocate(4);
        	buffer.putInt(iv[i]);
        	System.arraycopy(buffer.array(), 0, iv, i * 4, 4);
        }

        iv = rcPass(iv, seed, false);
        plain = rcPass(plain, seed, true);

        byte[] checkHash = hash(plain);
        
        return new MediusDecryptedMessage(plain, Utils.sequenceEquals(checkHash, hash));
	}

	@Override
	public MediusEncryptedMessage encrypt(byte[] input) {
		byte[] hash = hash(input, context);
        
        if (key == null) {
            return null;
        }

        // IV
        byte[] iv_buffer = new byte[0x10];
        int[] iv = new int[4];
        
        System.arraycopy(key, 0, iv_buffer, 0, 0x10);
        iv_buffer = flipWords(iv_buffer);

        // Reload
        for (int i = 0; i < 4; ++i) {
            //iv[i] = BitConverter.ToUInt32(iv_buffer, i * 4);
            iv[i] = iv_buffer[i*4] & 0xFF;
        }
        
        hash = rcPass(hash, iv, false);

        for (int i = 0; i < 4; ++i) {
            //var b = BitConverter.GetBytes(iv[i]);
            //Array.Copy(b, 0, iv_buffer, i * 4, 4);
        	final ByteBuffer buffer = ByteBuffer.allocate(4);
        	buffer.putInt(iv[i]);
        	System.arraycopy(buffer.array(), 0, iv_buffer, i * 4, 4);
        }
        
        iv_buffer = rcPass(iv_buffer, iv, false);
        input = rcPass(input, iv, true);
        
        return new MediusEncryptedMessage(input, hash, true);
	}

	/**
	 * Iterate through buffer and flip endianness of each 4 byte word.
	 * 
	 * @param input
	 */
	protected static byte[] flipWords(byte[] input) {
		for (int i = 0; i < input.length; i += 4) {
			byte temp = input[i + 0];
			input[i + 0] = input[i + 3];
			input[i + 3] = temp;
			temp = input[i + 1];
			input[i + 1] = input[i + 2];
			input[i + 2] = temp;
		}
		
		return input;
	}

	protected static byte[] rcPass(byte[] input, int[] iv, boolean sign) {
		char r0 = (char) 0x00000000;
		char r3 = (char) 0x5B3AA654;
		char r5 = (char) 0x75970A4D;
		char r6 = (char) 0x00000000;

		int newLength = (input.length % 4 != 0) ? (input.length + (4 - (input.length % 4))) : input.length;
		byte[] buffer = new byte[newLength];

		System.arraycopy(input, 0, buffer, 0, input.length);
		flipWords(buffer);

		// B5A0559C 88AA4C20 013D2CC7 CB2DE2B6
		int r16 = iv[0];
		int r17 = iv[1];
		int r18 = iv[2];
		int r19 = iv[3];

		for (int i = 0; i < input.length; i += 4) {
			r19 ^= r3;
			r18 += r16;
			r18 += r19;
			r18 = (char) ((r18 << 7) | (r18 >> (32 - 7)));
			r17 += r19;
			r17 += r18;
			r18 ^= r5;
			r17 = (char) ((r17 << 11) | (r17 >> (32 - 11)));
			r16 += r18;
			r16 += r17;
			r16 = (char) ((r16 >> 15) | (r16 << (32 - 15)));
			r0 = (char) (r16 & r17);
			r17 = (char) ~r17;
			r6 = (char) (r18 & r17);
			r0 |= r6;
			r19 += r0;
			r16 = (char) ~r16;

			r0 = (char) ((buffer[i + 0] << 24) | (buffer[i + 1] << 16) | (buffer[i + 2] << 8) | (buffer[i + 3] << 0));
			r19 ^= r0;

			if (sign) {
				
				final ByteBuffer r19buff = ByteBuffer.allocate(4);
				r19buff.putInt(r19);
				
	        	byte[] r19_b = r19buff.array();
				buffer[i + 0] = r19_b[0];
				buffer[i + 1] = r19_b[1];
				buffer[i + 2] = r19_b[2];
				buffer[i + 3] = r19_b[3];
			}
		}

		iv[0] = r16;
		iv[1] = r17;
		iv[2] = r18;
		iv[3] = r19;

		// Copy signed buffer back into input
		// This can be moved into the loop at some point
		if (sign) {
			for (int i = 0; i < input.length; ++i) {
				input[i] = buffer[i];
			}
		}

		return input;
	}

	@Override
	public CipherContext getContext() {
		return context;
	}

	protected void setPacketId(CipherContext context) {
		this.context = context;
	}

	public byte[] hash(byte[] input) {
		return Checksum.hash(input, context);
	}

	public static byte[] hash(byte[] input, CipherContext context) {
		int r0 = 0x00000000;
		int r3 = 0x5B3AA654;
		int r5 = 0x75970A4D;
		int r6 = (int) input.length;

		int newLength = (input.length % 4 != 0) ? (input.length + (4 - (input.length % 4))) : input.length;
		byte[] buffer = new byte[newLength];
		System.arraycopy(input, 0, buffer, 0, input.length);
		flipWords(buffer);

		// IV
		// Here the IV is determined by performing an RC pass on an empty 16 byte
		// buffer.
		byte[] empty = new byte[0x10];
		int[] iv = new int[4];
		empty = rcPass(empty, iv, false);

		// B5A0559C 88AA4C20 013D2CC7 CB2DE2B6
		int r16 = iv[0];
		int r17 = iv[1];
		int r18 = iv[2];
		int r19 = iv[3];

		for (int i = 0; i < input.length; i += 4) {
			r19 ^= r3;
			r18 += r16;
			r18 += r19;
			r18 = (r18 << 7) | (r18 >> (32 - 7));
			r17 += r19;
			r17 += r18;
			r18 ^= r5;
			r17 = (r17 << 11) | (r17 >> (32 - 11));
			r16 += r18;
			r16 += r17;
			r16 = (r16 >> 15) | (r16 << (32 - 15));
			r0 = r16 & r17;
			r17 = ~r17;
			r6 = r18 & r17;
			r0 |= r6;
			r19 += r0;
			r16 = ~r16;

			r0 = (int) ((buffer[i + 0] << 24) | (buffer[i + 1] << 16) | (buffer[i + 2] << 8) | (buffer[i + 3] << 0));
			r19 ^= r0;
		}

		int hash = (int) (((long) ((r16 + r17 + r18 + r19) & 0x1FFFFFFF) | (long) context.getByte() << 29));

		ByteBuffer b = ByteBuffer.allocate(4);
		b.putInt(hash);

		return b.array();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RCQ) {
			RCQ rc = (RCQ) obj;
			return rc.equals(this);
		}
		return super.equals(obj);
	}

	public boolean equals(RCQ b) {
		return b.context == this.context && Utils.sequenceEquals(b.key, this.key);
	}

	@Override
	public String toString() {
		return "PS3_RC(" + context + ", " + Utils.bytesToString(key) + ")";
	}

}
