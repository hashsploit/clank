package net.hashsploit.clank.server.common.objects;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.utils.Utils;

public class MediusPacket {

	private MediusPacketType packetType;
	private byte[] data;
	private static final Logger logger = Logger.getLogger("");

	public MediusPacket(byte[] incomingData) {
		// Get medius packet type
	    short testingShortVal = Utils.bytesToShortLittle(incomingData[0], incomingData[1]);

	    // TODO: Make this not O(n)
		for (MediusPacketType p : MediusPacketType.values()) {
			if (p.getShort() == testingShortVal) {
				packetType = p;
				break;
			}
		}	
		data = Arrays.copyOfRange(incomingData,2,incomingData.length);
	}
	
	public MediusPacket(MediusPacketType type, byte[] data) {
		// Get medius packet type
	    this.packetType = type;
	    this.data = data;
	}
	
	public MediusPacket(MediusPacketType type) {
	    this.packetType = type;
	}
	
	public byte[] getPayload() {
		return data;
	}
	
	public MediusPacketType getMediusPacketType() {
		return packetType;
	}
	
	public byte[] toBytes() {
		ByteBuffer bb = ByteBuffer.allocate(2+getPayload().length);
		bb.put(packetType.getShortByte());
		bb.put(getPayload());
		bb.flip();
		return bb.array();
	}

	
	
}
