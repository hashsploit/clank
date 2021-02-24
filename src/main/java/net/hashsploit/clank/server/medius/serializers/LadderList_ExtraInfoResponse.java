package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class LadderList_ExtraInfoResponse extends MediusMessage {

	private byte[] messageID;
	private byte[] callbackStatus;
	private byte[] ladderPosition;
	private byte[] ladderStat;
	private byte[] accountId;
	private byte[] accountName;
	private byte[] accountStats;
	private byte[] onlineState;
	private byte[] endOfList;

	
	public LadderList_ExtraInfoResponse(byte[] messageID, byte[] callbackStatus, byte[] ladderPosition, byte[] ladderStat, byte[] accountId,
							byte[] accountName, byte[] accountStats, byte[] onlineState, byte[] endOfList) {
		super(MediusMessageType.LadderList_ExtraInfoResponse);
		
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.ladderPosition = ladderPosition;
		this.ladderStat = ladderStat;
		this.accountId = accountId;
		this.accountName = accountName;
		this.accountStats = accountStats;
		this.onlineState = onlineState;
		this.endOfList = endOfList;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(ladderPosition); 
			outputStream.write(ladderStat); 
			outputStream.write(accountId); 
			outputStream.write(accountName);
			outputStream.write(accountStats);
			outputStream.write(onlineState);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
	public String toString() {
		return "LadderList_ExtraInfoResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"ladderPosition: " + Utils.bytesToHex(ladderPosition) + '\n' + 
				"ladderStat: " + Utils.bytesToHex(ladderStat) + '\n' + 
				"accountId: " + Utils.bytesToHex(accountId) + '\n' + 
				"accountName: " + Utils.bytesToHex(accountName) + '\n' + 
				"accountStats: " + Utils.bytesToHex(accountStats) + '\n' + 
				"onlineState: " + Utils.bytesToHex(onlineState) + '\n' + 
				"endOfList: " + Utils.bytesToHex(endOfList);
	}

}
