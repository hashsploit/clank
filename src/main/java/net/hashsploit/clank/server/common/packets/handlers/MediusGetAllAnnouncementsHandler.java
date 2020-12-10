package net.hashsploit.clank.server.common.packets.handlers;

import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MlsConfig;
import net.hashsploit.clank.server.ChatColor;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.GetAllAnnouncementsRequest;
import net.hashsploit.clank.server.common.packets.serializers.GetAnnouncementsResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGetAllAnnouncementsHandler extends MediusPacketHandler {

	private GetAllAnnouncementsRequest reqPacket;
	private GetAnnouncementsResponse respPacket;

	public MediusGetAllAnnouncementsHandler() {
		super(MediusMessageType.GetAllAnnouncements, MediusMessageType.GetAnnouncementsResponse);
	}

	@Override
	public void read(MediusMessage mm) {
		reqPacket = new GetAllAnnouncementsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public void write(MediusClient client) {
		
		final List<String> announcements = ((MlsConfig) Clank.getInstance().getConfig()).getAnnouncements();
		String announcementStr = "";
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.NO_RESULT;
		
		if (announcements != null && announcements.size() > 0) {
			final StringBuilder sb = new StringBuilder();
			callbackStatus = MediusCallbackStatus.SUCCESS;
			
			for (int i=0; i<announcements.size(); i++) {
				sb.append(announcements.get(i));
			}
			
			announcementStr = sb.toString();
		}
		
		
		final int announcementId = 1;
		final byte[] announcement = Utils.padByteArray(ChatColor.parse(announcementStr), MediusConstants.ANNOUNCEMENT_MAXLEN.getValue());
		
		final GetAnnouncementsResponse announcementResponse = new GetAnnouncementsResponse(reqPacket.getMessageId(), callbackStatus, announcementId, announcement, true);
		
		logger.finest(announcementResponse.getDebugString());
		
		client.sendMessage(new RTMessage(RTMessageId.SERVER_APP, announcementResponse.toBytes()));
	}

}
