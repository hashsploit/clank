package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GetAnnouncementsResponse extends MediusMessage {

	private final byte[] messageId;
	private final byte[] padding;
	private final MediusCallbackStatus callbackStatus;
	private final int announcementId;
	private final byte[] announcement;
	private final boolean endOfList;
	
	private final byte[] payload;

	public GetAnnouncementsResponse(byte[] messageId, MediusCallbackStatus callbackStatus, int announcementId, byte[] announcement, boolean endOfList) {
		super(MediusMessageType.GetAnnouncementsResponse);
		this.messageId = messageId;
		this.padding = new byte[3];
		this.callbackStatus = callbackStatus;
		this.announcementId = announcementId;
		this.announcement = announcement;
		this.endOfList = endOfList;
		
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		try {
			buffer.write(messageId);
			buffer.write(padding);
			buffer.write(Utils.intToBytesLittle(callbackStatus.getValue()));
			buffer.write(Utils.intToBytesLittle(announcementId));
			buffer.write(announcement);
			buffer.write(Utils.intToBytesLittle(endOfList ? 1 : 0));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		payload = buffer.toByteArray();
		
	}

	@Override
	public byte[] getPayload() {
		return payload;
	}
	
	@Override
	public String getDebugString() {
		return Utils.generateDebugPacketString(GetAnnouncementsResponse.class.getName(),
			new String[] {
				"messageId",
				"padding",
				"callbackStatus",
				"announcementId",
				"announcement",
				"endOfList",
			},
			new String[] {
				"0x" + Utils.bytesToHex(messageId),
				"0x" + Utils.bytesToHex(padding),
				callbackStatus.name() + " (0x" + Utils.intToHex(callbackStatus.getValue()) + ")",
				"" + announcementId + "(0x" + Utils.intToHex(announcementId) + ")",
				Utils.bytesToStringClean(announcement),
				"" + endOfList + " (0x" + Utils.intToHex(endOfList ? 1 : 0) + ")"
			}
		);
	}
	
	public byte[] getMessageId() {
		return messageId;
	}

}
