package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.AccountLogoutRequest;
import net.hashsploit.clank.server.medius.serializers.AccountLogoutResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountLogoutHandler extends MediusPacketHandler {
	private AccountLogoutRequest reqPacket;

	public MediusAccountLogoutHandler() {
		super(MediusMessageType.AccountLogout, MediusMessageType.AccountLogoutResponse);
	}
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new AccountLogoutRequest(mm.getPayload());
		logger.finest(reqPacket.toString());	
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		AccountLogoutResponse respPacket = new AccountLogoutResponse(reqPacket.getMessageID(), Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue()));
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;		
	}

}
