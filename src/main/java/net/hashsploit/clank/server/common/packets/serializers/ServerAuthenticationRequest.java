package net.hashsploit.clank.server.common.packets.serializers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class ServerAuthenticationRequest extends MediusMessage {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] trustLevel = new byte[4]; // int
	private byte[] addressList = new byte[48]; // NetAddressList
	
	public ServerAuthenticationRequest(byte[] data) {
		super(MediusMessageType.MediusServerAuthenticationRequest, data);
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		buf.get(messageId);
		buf.get(); // 1 byte padding
		buf.getShort(); // 2 byte padding
		buf.get(trustLevel); 
		buf.get(addressList);
	}
	
	@Override
	public String toString() {
		return "ServerAuthenticationRequest: \n" + 
			"messageId: " + Utils.bytesToHex(messageId) + '\n' + 
			"trustLevel: " + Utils.bytesToHex(trustLevel) + '\n' +
			"addressList: " + Utils.bytesToHex(addressList);
	}

	public byte[] getMessageId() {
		return messageId;
	}

	public byte[] getLocationId() {
		return trustLevel;
	}

	public byte[] getNetAddressList() {
		return addressList;
	}
	
}