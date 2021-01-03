package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.GetAnnouncementsRequest;
import net.hashsploit.clank.server.medius.serializers.GetAnnouncementsResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGetAnnouncementsHandler extends MediusPacketHandler {

	private GetAnnouncementsRequest reqPacket;

	public MediusGetAnnouncementsHandler() {
		super(MediusMessageType.GetAnnouncements, MediusMessageType.GetAnnouncementsResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new GetAnnouncementsRequest(mm.getPayload());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		final List<String> announcements = ((MediusConfig) Clank.getInstance().getConfig()).getAnnouncements();
		List<MediusMessage> response = new ArrayList<MediusMessage>();

		if (announcements != null && announcements.size() > 0) {
			for (int i = 0; i < announcements.size(); i++) {
				String announcementString = announcements.get(i);

				final int maxSegmentedAnnouncementLength = MediusConstants.ANNOUNCEMENT_MAXLEN.getValue() - 1; // leave one byte of room for the null terminator

				final List<String> segmentedAnnouncementStrings = new ArrayList<String>();

				if (announcementString.length() > maxSegmentedAnnouncementLength) {
					for (int j = 0; j < announcementString.length(); j += maxSegmentedAnnouncementLength) {
						int remaining = j + maxSegmentedAnnouncementLength;
						if (remaining > announcementString.length()) {
							remaining = announcementString.length();
						}

						String segment = announcementString.substring(j, remaining);

						segmentedAnnouncementStrings.add(segment);
					}
				} else {
					segmentedAnnouncementStrings.add(announcementString);
				}

				// TODO: when queues are added, create a for-loop to iterate through 64-byte
				// length chunks of the policy set in the config.
				// enqueue each of the chunks respectively.
				for (int j = 0; j < segmentedAnnouncementStrings.size(); j++) {

					final MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
					final int announcementId = i;
					final byte[] announcement = Utils.buildByteArrayFromString(segmentedAnnouncementStrings.get(j), maxSegmentedAnnouncementLength + 1);
					final boolean endOfList = announcements.size() - 1 == i;

					final GetAnnouncementsResponse announcementResponse = new GetAnnouncementsResponse(reqPacket.getMessageId(), callbackStatus, announcementId, announcement, endOfList);

					logger.finest(announcementResponse.getDebugString());
					response.add(announcementResponse);
				}

			}
		} else {
			final MediusCallbackStatus callbackStatus = MediusCallbackStatus.NO_RESULT;
			final int announcementId = 1;
			final byte[] announcement = Utils.padByteArray(new byte[0], MediusConstants.ANNOUNCEMENT_MAXLEN.getValue());
			final boolean endOfList = true;

			final GetAnnouncementsResponse announcementResponse = new GetAnnouncementsResponse(reqPacket.getMessageId(), callbackStatus, announcementId, announcement, endOfList);
			
			response.add(announcementResponse);
		}
		
		
		return response;
	}

}