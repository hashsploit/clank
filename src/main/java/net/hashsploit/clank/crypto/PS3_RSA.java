package net.hashsploit.clank.crypto;

import java.math.BigInteger;

/**
 * PS3 SCERT Textbook RSA implementation.
 * @author hashsploit
 */
public class PS3_RSA extends PS2_RSA {
	
	public PS3_RSA(BigInteger n, BigInteger e, BigInteger d) {
		super(n, e, d);
	}
	
	@Override
	public byte[] hash(byte[] input) {
		return RCQ.hash(input, getContext());
	}
	
}
