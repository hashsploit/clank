package net.hashsploit.clank.server.medius.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ServerSessionBeginRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] locationId = new byte[4]; // int
	private byte[] applicationId = new byte[4]; // int
	private byte[] serverType = new byte[4]; // int
	private byte[] serverVersion = new byte[16]; // string
	private byte[] serverPort = new byte[4]; // int
	
	public ServerSessionBeginRequest(byte[] data) {
		super(MediusMessageType.MediusServerSessionBeginRequest, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageId);
		buf.get(); // 1 byte padding
		buf.getShort(); // 2 byte padding
		buf.get(locationId);
		buf.get(applicationId);
		buf.get(serverType);
		buf.get(serverVersion);
		buf.get(serverPort);
	}
	
	@Override
	public String toString() {
		return "MediusServerSessionBeginRequest: \n" + 
			"messageId: " + Utils.bytesToHex(messageId) + '\n' + 
			"locationId: " + Utils.bytesToHex(locationId) + '\n' +
			"applicationId: " + Utils.bytesToHex(applicationId) + '\n' +
			"serverType: " + Utils.bytesToHex(serverType) + '\n' +
			"serverVersion: " + Utils.bytesToHex(serverVersion) + '\n' +
			"serverPort: " + Utils.bytesToHex(serverPort);
	}

	public byte[] getMessageId() {
		return messageId;
	}

	public byte[] getLocationId() {
		return locationId;
	}

	public byte[] getApplicationId() {
		return applicationId;
	}

	public byte[] getServerType() {
		return serverType;
	}

	public byte[] getServerVersion() {
		return serverVersion;
	}

	public byte[] getServerPort() {
		return serverPort;
	}
	
}
