package net.hashsploit.clank.crypto;

import java.math.BigInteger;

import net.hashsploit.clank.utils.Utils;

/**
 * PS2 SCERT Textbook RSA implementation.
 * 
 * n = p * q
 * 
 * e = public exponent
 * d = private exponent
 * 
 * Encrypt:
 * c = m^e * mod(n)
 * 
 * Decrypt:
 * m = c^d * mod(n)
 * 
 * 
 * @author hashsploit
 */
public class PS2_RSA implements ICipher {
	
	private CipherContext context;
	private BigInteger n;
	private BigInteger e;
	private BigInteger d;
	
	/**
	 * Create a new RSA key-pair
	 * @param n = p * q
	 * @param e = public exponent
	 * @param d = private exponent
	 */
	public PS2_RSA(BigInteger n, BigInteger e, BigInteger d) {
		this.n = n;
		this.e = e;
		this.d = d;
	}
	
	private BigInteger _encrypt(BigInteger m) {
		return m.modPow(e, n);
	}
	
	private BigInteger _decrypt(BigInteger c) {
		return c.modPow(d, n);
	}
	
	@Override
	public MediusDecryptedMessage decrypt(byte[] input, byte[] hash) {
		BigInteger plainBigInt = _decrypt(new BigInteger(input));
		byte[] plain = plainBigInt.toByteArray();

		byte[] ourHash = hash(plain);
		if (Utils.sequenceEquals(ourHash, hash)) {
			return new MediusDecryptedMessage(plain, true);
		}
		
		// Handle case where message > n
		plainBigInt = plainBigInt.add(n);
		plain = plainBigInt.toByteArray();
		ourHash = hash(plain);
		return new MediusDecryptedMessage(plain, Utils.sequenceEquals(ourHash, hash));
	}
	
	@Override
	public MediusEncryptedMessage encrypt(byte[] input) {
		byte[] hash = hash(input);
		byte[] cipher = _encrypt(new BigInteger(input)).toByteArray();
		return new MediusEncryptedMessage(cipher, hash, true);
	}
	
	protected void setContext(CipherContext context) {
		this.context = context;
	}
	
	@Override
	public CipherContext getContext() {
		return context;
	}
	
	@Override
	public byte[] hash(byte[] input) {
		return Checksum.hash(input, context);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PS2_RSA) {
			PS2_RSA rsa = (PS2_RSA) obj;
			return rsa.equals(this);
		}
		return super.equals(obj);
	}
	
	/**
	 * Get n (n = p * q)
	 * n = p * q
	 * @return
	 */
	public BigInteger getN() {
		return n;
	}
	
	/**
	 * Set n (n = p * q)
	 * @param n
	 */
	protected void setN(BigInteger n) {
		this.n = n;
	}
	
	/**
	 * Get e (public exponent)
	 * @return
	 */
	public BigInteger getE() {
		return e;
	}
	
	/**
	 * Set e (public exponent)
	 * @param e
	 */
	protected void setE(BigInteger e) {
		this.e = e;
	}
	
	/**
	 * Get d (private exponent)
	 * @return
	 */
	public BigInteger getD() {
		return d;
	}
	
	/**
	 * Set d (private exponent)
	 * @param d
	 */
	protected void setD(BigInteger d) {
		this.d = d;
	}
	
}
