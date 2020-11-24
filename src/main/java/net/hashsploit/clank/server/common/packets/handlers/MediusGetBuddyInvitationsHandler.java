package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.objects.MediusBuddyAddType;
import net.hashsploit.clank.server.common.objects.MediusPacket;
import net.hashsploit.clank.server.common.packets.serializers.GetBuddyInvitationsRequest;
import net.hashsploit.clank.server.common.packets.serializers.GetBuddyInvitationsResponse;

public class MediusGetBuddyInvitationsHandler extends MediusPacketHandler {

	private GetBuddyInvitationsRequest reqPacket;
	private GetBuddyInvitationsResponse respPacket;
	
	public MediusGetBuddyInvitationsHandler() {
		super(MediusPacketType.GetBuddyInvitations, MediusPacketType.GetBuddyInvitationsResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new GetBuddyInvitationsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.MediusNoResult;
		
		int accountId = 0;
		String accountName = "";
		MediusBuddyAddType addType = MediusBuddyAddType.ADD_SYMMETRIC;
		boolean endOfList = true;
		
		
		respPacket = new GetBuddyInvitationsResponse(reqPacket.getMessageId(), callbackStatus, accountId, accountName, addType, endOfList);
		
		return respPacket;
	}


}
