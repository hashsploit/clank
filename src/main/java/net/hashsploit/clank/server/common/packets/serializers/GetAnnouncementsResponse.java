package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class GetAnnouncementsResponse extends MediusPacket {

	private byte[] messageID;
	private byte[] callbackStatus;
	private byte[] announcementID;
	private byte[] announcement;
	private byte[] endOfList;

	public GetAnnouncementsResponse(byte[] messageID, byte[] callbackStatus, byte[] announcementID, byte[] announcement, byte[] endOfList) {
		super(MediusPacketType.GetAnnouncementsResponse);
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.announcementID = announcementID;
		this.announcement = announcement;
		this.endOfList = endOfList;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(callbackStatus);
			outputStream.write(announcementID);
			outputStream.write(announcement);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "GetAllAnnouncementsResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"announcementID: " + Utils.bytesToHex(announcementID) + '\n' + 
				"announcement: " + Utils.bytesToHex(announcement) + '\n' + 
				"endOfList: " + Utils.bytesToHex(endOfList);
	}

	public synchronized byte[] getMessageID() {
		return messageID;
	}

	public synchronized void setMessageID(byte[] messageID) {
		this.messageID = messageID;
	}

	public synchronized byte[] getCallbackStatus() {
		return callbackStatus;
	}

	public synchronized void setCallbackStatus(byte[] callbackStatus) {
		this.callbackStatus = callbackStatus;
	}

	public synchronized byte[] getAnnouncementID() {
		return announcementID;
	}

	public synchronized void setAnnouncementID(byte[] announcementID) {
		this.announcementID = announcementID;
	}

	public synchronized byte[] getAnnouncement() {
		return announcement;
	}

	public synchronized void setAnnouncement(byte[] announcement) {
		this.announcement = announcement;
	}

	public synchronized byte[] getEndOfList() {
		return endOfList;
	}

	public synchronized void setEndOfList(byte[] endOfList) {
		this.endOfList = endOfList;
	}
		
	
}
