package net.hashsploit.clank.server.common.packets.handlers;

import java.util.Random;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.AccountGetIDRequest;
import net.hashsploit.clank.server.common.packets.serializers.AccountGetIDResponse;
import net.hashsploit.clank.server.common.packets.serializers.AccountLoginRequest;
import net.hashsploit.clank.server.common.packets.serializers.AccountLoginResponse;
import net.hashsploit.clank.server.common.packets.serializers.AddToBuddyListRequest;
import net.hashsploit.clank.server.common.packets.serializers.AddToBuddyListResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusAddToBuddyListHandler extends MediusPacketHandler {

	private AddToBuddyListRequest reqPacket;
	private AddToBuddyListResponse respPacket;

	public MediusAddToBuddyListHandler() {
		super(MediusMessageType.AddToBuddyList, MediusMessageType.AddToBuddyListResponse);
	}

	@Override
	public void read(MediusMessage mm) {
		reqPacket = new AddToBuddyListRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public MediusMessage write(MediusClient client) {

		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));

		respPacket = new AddToBuddyListResponse(reqPacket.getMessageID(), callbackStatus);

		return respPacket;
	}

}
