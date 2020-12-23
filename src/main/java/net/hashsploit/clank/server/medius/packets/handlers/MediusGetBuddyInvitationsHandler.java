package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusBuddyAddType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.packets.serializers.GetBuddyInvitationsRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GetBuddyInvitationsResponse;

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
