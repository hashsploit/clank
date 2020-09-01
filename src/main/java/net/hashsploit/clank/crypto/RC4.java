package net.hashsploit.clank.crypto;

import net.hashsploit.clank.utils.Utils;

/**
 * PlayStation 2's custom RC4 Medius implementation 
 * @author hashsploit
 */
public class RC4 implements ICipher {

	private static final int STATE_LENGTH = 256;

	private byte[] engineState;
	private int x;
	private int y;
	private byte[] workingKey;
	private CipherContext context;

	/**
	 * Initialize crypto with a 512-bit key
	 * 
	 * @param key
	 * @param packetId
	 */
	public RC4(byte[] key, CipherContext context) {
		this.context = context;
		setKey(key);
	}

	public void reset() {
		setKey(workingKey);
	}

	private void setKey(byte[] keyBytes) {
		setKey(keyBytes, null);
	}

	private void setKey(byte[] key, byte[] hash) {
		workingKey = key;
		x = 0;
		y = 0;

		int keyIndex = 0;
		int li = 0;
		int cipherIndex = 0;
		int idIndex = 0;

		// Initialize engine state
		if (engineState == null) {
			engineState = new byte[STATE_LENGTH];
		}

		// reset the state of the engine
		// Normally this initializes values 0,1..254,255 but UYA does this in reverse.
		for (int i = 0; i < STATE_LENGTH; i++) {
			engineState[i] = (byte) ((STATE_LENGTH - 1) - i);
		}

		if (hash != null && hash.length == 4) {
			// Apply hash
			do {
				int v1 = hash[idIndex];
				idIndex = (idIndex + 1) & 3;

				byte temp = engineState[cipherIndex];
				v1 += li;
				li = (temp + v1) & 0xFF;

				engineState[cipherIndex] = engineState[li];
				engineState[li] = temp;

				cipherIndex = (cipherIndex + 5) & 0xFF;

			} while (cipherIndex != 0);

			// Reset
			keyIndex = 0;
			li = 0;
			cipherIndex = 0;
			idIndex = 0;
		}

		// Apply key
		do {
			int keyByte = key[keyIndex];
			keyByte += li;
			keyIndex += 1;
			keyIndex &= 0x3F;

			int cipherByte = engineState[cipherIndex];
			byte cipherValue = (byte) (cipherByte & 0xFF);

			cipherByte += keyByte;
			li = cipherByte & 0xFF;

			byte t0 = engineState[li];
			engineState[cipherIndex] = t0;
			engineState[li] = cipherValue;

			cipherIndex += 3;
			cipherIndex &= 0xFF;
		} while (cipherIndex != 0);
	}

	private byte[] _decrypt(byte[] input, int inOff, int length, int outOff) {
		final byte[] output = new byte[length];
		
		for (int i = 0; i < length; ++i) {
			y = (y + 5) & 0xFF;

			int v0 = engineState[y];
			byte a2 = (byte) (v0 & 0xFF);
			v0 += x;
			x = (int) (v0 & 0xFF);

			v0 = engineState[x];
			engineState[y] = (byte) (v0 & 0xFF);
			engineState[x] = a2;

			byte a0 = input[i];

			v0 += a2;
			v0 &= 0xFF;
			int v1 = engineState[v0];

			a0 ^= (byte) v1;
			output[i] = a0;

			v1 = engineState[a0] + x;
			x = v1 & 0xFF;
		}
		
		return output;
	}

	@Override
	public MediusDecryptedMessage decrypt(byte[] data, byte[] hash) {

		byte[] plain = new byte[data.length];

		// Check if empty hash
		// If hash is 0, the data is already in plaintext
		if (hash[0] == 0 && hash[1] == 0 && hash[2] == 0 && (hash[3] & 0x1F) == 0) {
			System.arraycopy(data, 0, plain, 0, data.length);
			return new MediusDecryptedMessage(plain, true);
		}

		// Set seed
		setKey(workingKey, hash);

		plain = _decrypt(data, 0, data.length, 0);
		final byte[] chkHash = hash(plain);

		return new MediusDecryptedMessage(plain, Utils.sequenceEquals(hash, chkHash));
	}
	
	private byte[] _encrypt(byte[] input, int inOff, int length, int outOff) {
		byte[] output = new byte[length];
		
		for (int i = 0; i < length; ++i) {
			x = (x + 5) & 0xff;
			y = (y + engineState[x]) & 0xff;

			// Swap
			final byte temp = engineState[x];
			engineState[x] = engineState[y];
			engineState[y] = temp;

			// Xor
			final byte a = input[i + inOff];
			final byte b = engineState[(engineState[x] + engineState[y]) & 0xff];
			output[i + outOff] = (byte) (a ^ b);

			
			y = (engineState[input[i + inOff]] + y) & 0xff;
		}
		
		return output;
	}

	/**
	 * Encrypt data using the last key provided
	 */
	@Override
	public MediusEncryptedMessage encrypt(byte[] data) {
		
		 // Set seed
        byte[] hash = Checksum.hash(data, context);
        setKey(workingKey, hash);

        byte[] cipher = new byte[data.length];
        cipher = _encrypt(data, 0, data.length, 0);
        
        return new MediusEncryptedMessage(cipher, hash, true);
	}
	
	@Override
	public CipherContext getContext() {
		return context;
	}

	protected void setContext(CipherContext context) {
		this.context = context;
	}

	public byte[] hash(byte[] input) {
        return Checksum.hash(input, context);
    }
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RC4) {
			RC4 rc = (RC4) obj;
			return rc.equals(this);
		}
		return super.equals(obj);
	}
	
	public boolean equals(RC4 b) {
		return b.context == this.context && Utils.sequenceEquals(b.workingKey, this.workingKey);
	}

	@Override
	public String toString() {
		return "PS2_RC4(" + context + ", " + Utils.bytesToString(workingKey) + ")";
	}
	
}
