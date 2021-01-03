package net.hashsploit.clank.server.common.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.utils.Utils;

public class MediusMessage {

	private MediusMessageType packetType;
	private byte[] data;

	public MediusMessage(byte[] incomingData) {
		// Get medius packet type
		short testingShortVal = Utils.bytesToShortLittle(incomingData[0], incomingData[1]);

		// TODO: Make this not O(n)
		for (MediusMessageType p : MediusMessageType.values()) {
			if (p.getShort() == testingShortVal) {
				packetType = p;
				break;
			}
		}
		data = Arrays.copyOfRange(incomingData, 2, incomingData.length);
	}

	public MediusMessage(MediusMessageType type, byte[] data) {
		// Get medius packet type
		this.packetType = type;
		this.data = data;
	}

	public MediusMessage(MediusMessageType type) {
		this.packetType = type;
	}

	public byte[] getPayload() {
		return data;
	}

	public MediusMessageType getMediusPacketType() {
		return packetType;
	}

	public byte[] toBytes() {
		ByteBuffer bb = ByteBuffer.allocate(2 + getPayload().length);
		bb.put(packetType.getShortByte());
		bb.put(getPayload());
		bb.flip();
		return bb.array();
	}
	
	public String getDebugString() {
		return null;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}

}
