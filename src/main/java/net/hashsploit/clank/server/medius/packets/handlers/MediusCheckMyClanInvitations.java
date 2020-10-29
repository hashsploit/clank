package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusClanInvitationResponseStatus;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.packets.serializers.CheckMyClanInvitationsRequest;
import net.hashsploit.clank.server.medius.packets.serializers.CheckMyClanInvitationsResponse;

public class MediusCheckMyClanInvitations extends MediusPacketHandler {

	private CheckMyClanInvitationsRequest reqPacket;
	private CheckMyClanInvitationsResponse respPacket;
	
	public MediusCheckMyClanInvitations() {
		super(MediusPacketType.CheckMyClanInvitations, MediusPacketType.CheckMyClanInvitationsResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new CheckMyClanInvitationsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.MediusNoResult;
		int clanInvitationId = 0;
		int clanId = 0;
		MediusClanInvitationResponseStatus clanInvitationResponseStatus = MediusClanInvitationResponseStatus.CLAN_INVITATION_UNDECIDED;
		String message = "Welcome to the club!";
		int leaderAccountId = 0;
		String leaderAccountName = "Squidward";
		boolean endOfList = true;
		
		respPacket = new CheckMyClanInvitationsResponse(reqPacket.getMessageId(), callbackStatus, clanInvitationId, clanId, clanInvitationResponseStatus, message, leaderAccountId, leaderAccountName, endOfList);
		
		return respPacket;
	}


}
