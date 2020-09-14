package net.hashsploit.clank.server.scert;

public interface ISCERTObject {
	
	public byte[] serialize();
	
	public void deserialize(byte[] data);
	
}
