package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.AccountGetIdRequest;
import net.hashsploit.clank.server.medius.serializers.AccountGetIdResponse;
import net.hashsploit.clank.server.medius.serializers.AccountLoginRequest;
import net.hashsploit.clank.server.medius.serializers.AccountLoginResponse;
import net.hashsploit.clank.server.medius.serializers.AddToBuddyListRequest;
import net.hashsploit.clank.server.medius.serializers.AddToBuddyListResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusAddToBuddyListHandler extends MediusPacketHandler {

	private AddToBuddyListRequest reqPacket;
	private AddToBuddyListResponse respPacket;

	public MediusAddToBuddyListHandler() {
		super(MediusMessageType.AddToBuddyList, MediusMessageType.AddToBuddyListResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new AddToBuddyListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));

		respPacket = new AddToBuddyListResponse(reqPacket.getMessageID(), callbackStatus);

		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}

}
