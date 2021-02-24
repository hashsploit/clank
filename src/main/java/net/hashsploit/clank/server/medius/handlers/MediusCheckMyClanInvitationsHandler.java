package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusClanInvitationResponseStatus;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.CheckMyClanInvitationsRequest;
import net.hashsploit.clank.server.medius.serializers.CheckMyClanInvitationsResponse;

public class MediusCheckMyClanInvitationsHandler extends MediusPacketHandler {

	private CheckMyClanInvitationsRequest reqPacket;
	private CheckMyClanInvitationsResponse respPacket;
	
	public MediusCheckMyClanInvitationsHandler() {
		super(MediusMessageType.CheckMyClanInvitations, MediusMessageType.CheckMyClanInvitationsResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new CheckMyClanInvitationsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.NO_RESULT;
		int clanInvitationId = 1;
		int clanId = 1;
		MediusClanInvitationResponseStatus clanInvitationResponseStatus = MediusClanInvitationResponseStatus.CLAN_INVITATION_UNDECIDED;
		String message = "yo join us.";
		int leaderAccountId = 1;
		String leaderAccountName = "SCEA";
		boolean endOfList = true;
		
		respPacket = new CheckMyClanInvitationsResponse(reqPacket.getMessageId(), callbackStatus, clanInvitationId, clanId, clanInvitationResponseStatus, message, leaderAccountId, leaderAccountName, endOfList);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}


}
