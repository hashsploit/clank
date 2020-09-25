package net.hashsploit.clank.server.medius.objects;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.utils.Utils;

public class MediusMessage {

	private MediusPacketType packetType;
	private byte[] data;
	private static final Logger logger = Logger.getLogger("");

	public MediusMessage(byte[] incomingData) {
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
	
	public MediusMessage(MediusPacketType type, byte[] data) {
		// Get medius packet type
	    this.packetType = type;
	    this.data = data;
	}
	
	public byte[] getPayload() {
		return data;
	}
	
	public MediusPacketType getMediusPacketType() {
		return packetType;
	}
	
	public byte[] toBytes() {
		ByteBuffer bb = ByteBuffer.allocate(2+data.length);
		bb.put(packetType.getShortByte());
		bb.put(data);
		bb.flip();
		return bb.array();
	}

	
	
}
