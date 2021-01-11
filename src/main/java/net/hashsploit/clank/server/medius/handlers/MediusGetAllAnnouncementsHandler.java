package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MlsConfig;
import net.hashsploit.clank.server.ChatColor;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.GetAllAnnouncementsRequest;
import net.hashsploit.clank.server.medius.serializers.GetAnnouncementsResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGetAllAnnouncementsHandler extends MediusPacketHandler {

	private GetAllAnnouncementsRequest reqPacket;
	private GetAnnouncementsResponse respPacket;

	public MediusGetAllAnnouncementsHandler() {
		super(MediusMessageType.GetAllAnnouncements, MediusMessageType.GetAnnouncementsResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new GetAllAnnouncementsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		
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
		final byte[] announcement = Utils.padByteArray(ChatColor.parse(announcementStr), MediusConstants.ANNOUNCEMENT_MAXLEN.value);
		
		respPacket = new GetAnnouncementsResponse(reqPacket.getMessageId(), callbackStatus, announcementId, announcement, true);
		
		logger.finest(respPacket.getDebugString());
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}

}
