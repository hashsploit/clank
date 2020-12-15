package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusBuddyAddType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.GetBuddyInvitationsRequest;
import net.hashsploit.clank.server.common.packets.serializers.GetBuddyInvitationsResponse;

public class MediusGetBuddyInvitationsHandler extends MediusPacketHandler {

	private GetBuddyInvitationsRequest reqPacket;
	private GetBuddyInvitationsResponse respPacket;
	
	public MediusGetBuddyInvitationsHandler() {
		super(MediusMessageType.GetBuddyInvitations, MediusMessageType.GetBuddyInvitationsResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new GetBuddyInvitationsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.NO_RESULT;
		
		int accountId = 0;
		String accountName = "";
		MediusBuddyAddType addType = MediusBuddyAddType.ADD_SYMMETRIC;
		boolean endOfList = true;
		
		
		respPacket = new GetBuddyInvitationsResponse(reqPacket.getMessageId(), callbackStatus, accountId, accountName, addType, endOfList);
		
		client.sendMediusMessage(respPacket);
	}


}
