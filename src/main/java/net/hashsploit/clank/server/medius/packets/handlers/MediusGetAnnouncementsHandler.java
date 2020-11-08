package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.packets.serializers.GetAnnouncementsRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GetAnnouncementsResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGetAnnouncementsHandler extends MediusPacketHandler {

	private GetAnnouncementsRequest reqPacket;
	private GetAnnouncementsResponse respPacket;
	
	public MediusGetAnnouncementsHandler() {
		super(MediusPacketType.GetAnnouncements,MediusPacketType.GetAnnouncementsResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new GetAnnouncementsRequest(mm.getPayload());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue());
		byte[] announcementID = Utils.intToBytesLittle(10);
		byte[] announcement = Utils.buildByteArrayFromString("Announcment TEST", MediusConstants.ANNOUNCEMENT_MAXLEN.getValue());
		byte[] endOfList = Utils.hexStringToByteArray("01000000");
		
		respPacket = new GetAnnouncementsResponse(reqPacket.getMessageId(), callbackStatus, announcementID, announcement, endOfList);
		
		return respPacket;
	}

}