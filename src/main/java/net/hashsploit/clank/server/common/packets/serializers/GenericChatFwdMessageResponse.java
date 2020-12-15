package net.hashsploit.clank.server.common.packets.serializers;

import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusChatMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GenericChatFwdMessageResponse extends MediusMessage {

	private final int timestamp;
	private final int senderAccountId;
	private final MediusChatMessageType mediusChatMessageType;
	private final byte[] accountName;
	private final byte[] message;
	
	public GenericChatFwdMessageResponse(int timestamp, int senderAccountId, MediusChatMessageType mediusChatMessageType, byte[] accountName, byte[] message) {
		super(MediusMessageType.GenericChatFwdMessage);
		
		this.timestamp = timestamp;
		this.senderAccountId = senderAccountId;
		this.mediusChatMessageType = mediusChatMessageType;
		this.accountName = accountName;
		this.message = message;
	}
	
	@Override
	public String getDebugString() {
		return Utils.generateDebugPacketString(GenericChatFwdMessageResponse.class.getName(),
			new String[] {
				"timestamp",
				"senderAccountId",
				"mediusChatMessageType",
				"accountName",
				"message"
			},
			new String[] {
				"" + timestamp,
				"" + senderAccountId,
				mediusChatMessageType.name() + " (" + Utils.intToHex(mediusChatMessageType.getValue()) + ")",
				Utils.bytesToStringClean(accountName),
				Utils.bytesToStringClean(message)
			}
		);
	}
	
	@Override
	public String toString() {
		return "GenericChatFwdMessageResponse: \n" + 
				"timestamp: " + Utils.intToHex(timestamp) + '\n' + 
				"senderAccountId: " + Utils.intToHex(senderAccountId) + '\n' + 
				"mediusChatMessageType: " + mediusChatMessageType.name() + "(" + Utils.intToHex(mediusChatMessageType.getValue()) + ")" + "\n" + 
				"accountName: " + Utils.bytesToHex(accountName) + '\n' + 
				"message: " + Utils.bytesToHex(message);		
	}

	
	
}
