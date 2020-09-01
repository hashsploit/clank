package net.hashsploit.clank.server.medius;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.ISCERTMessage;
import net.hashsploit.clank.utils.Utils;

public abstract class BaseSCERTMessage implements ISCERTMessage {
	
	public static final int HEADER_SIZE = 3;
	public static final int CHECKSUM_SIZE = 4;
	
	public BaseSCERTMessage() {
		
	}
	
	/**
	 * Create a packet as a ordered list of messages.
	 * @return
	 */
	public List<byte[]> createPacket() {
		final List<byte[]> messages = new ArrayList<byte[]>();
		
		// Package data
		
		// Somehow handle data and encryption
		
		// handle packet splitting (Fragmenting)
		
		
		return messages;
	}
	
	
	@Override
	public String toString() {
		return "BaseSCERTMessage[ID: " + this.getId().name() + " Len: " + this.getLength() + " Payload: " + Utils.bytesToString(this.getPayload()) + "]";
	}
	
	
}
