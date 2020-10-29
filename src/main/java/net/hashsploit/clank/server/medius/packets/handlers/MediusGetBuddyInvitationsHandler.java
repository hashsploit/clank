package net.hashsploit.clank.server.medius.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusBuddyAddType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.packets.serializers.GetBuddyInvitationsRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GetBuddyInvitationsResponse;

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
		
		int accountId = 100;
		String accountName = "Badger41";
		MediusBuddyAddType addType = MediusBuddyAddType.ADD_SINGLE;
		boolean endOfList = true;
		
		
		respPacket = new GetBuddyInvitationsResponse(reqPacket.getMessageId(), callbackStatus, accountId, accountName, addType, endOfList);
		
		return respPacket;
	}


}
