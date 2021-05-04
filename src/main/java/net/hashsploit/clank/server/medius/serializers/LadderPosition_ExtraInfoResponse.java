package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class LadderPosition_ExtraInfoResponse extends MediusMessage {

	private byte[] messageID;
	private byte[] callbackStatus;
	private byte[] ladderPosition;
	private byte[] totalRankings;
	
	public LadderPosition_ExtraInfoResponse(byte[] messageID, byte[] callbackStatus, byte[] ladderPosition,
			byte[] totalRankings) {
		super(MediusMessageType.LadderPosition_ExtraInfoResponse);
		
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.ladderPosition = ladderPosition;
		this.totalRankings = totalRankings;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(callbackStatus); 
			outputStream.write(ladderPosition); 
			outputStream.write(totalRankings);  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "LadderPosition_ExtraInfoResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"ladderPosition: " + Utils.bytesToHex(ladderPosition) + '\n' + 
				"totalRankings: " + Utils.bytesToHex(totalRankings);
	}

}