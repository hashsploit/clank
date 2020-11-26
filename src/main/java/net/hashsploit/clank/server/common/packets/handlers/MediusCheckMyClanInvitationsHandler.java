package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusClanInvitationResponseStatus;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.CheckMyClanInvitationsRequest;
import net.hashsploit.clank.server.common.packets.serializers.CheckMyClanInvitationsResponse;

public class MediusCheckMyClanInvitationsHandler extends MediusPacketHandler {

	private CheckMyClanInvitationsRequest reqPacket;
	private CheckMyClanInvitationsResponse respPacket;
	
	public MediusCheckMyClanInvitationsHandler() {
		super(MediusMessageType.CheckMyClanInvitations, MediusMessageType.CheckMyClanInvitationsResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new CheckMyClanInvitationsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusMessage write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.NO_RESULT;
		int clanInvitationId = 1;
		int clanId = 1;
		MediusClanInvitationResponseStatus clanInvitationResponseStatus = MediusClanInvitationResponseStatus.CLAN_INVITATION_UNDECIDED;
		String message = "yo join us.";
		int leaderAccountId = 1;
		String leaderAccountName = "SCEA";
		boolean endOfList = true;
		
		respPacket = new CheckMyClanInvitationsResponse(reqPacket.getMessageId(), callbackStatus, clanInvitationId, clanId, clanInvitationResponseStatus, message, leaderAccountId, leaderAccountName, endOfList);
		
		return respPacket;
	}


}
