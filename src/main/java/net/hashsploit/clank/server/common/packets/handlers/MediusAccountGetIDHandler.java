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
import net.hashsploit.clank.utils.Utils;

public class MediusAccountGetIDHandler extends MediusPacketHandler {

	private AccountGetIDRequest reqPacket;
	private AccountGetIDResponse respPacket;

	public MediusAccountGetIDHandler() {
		super(MediusMessageType.AccountGetID, MediusMessageType.AccountGetIDResponse);
	}

	@Override
	public void read(MediusMessage mm) {
		reqPacket = new AccountGetIDRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public void write(MediusClient client) {

		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));
		byte[] mlsToken = Utils.hexStringToByteArray("00000000000000000000000000000000000000");

		respPacket = new AccountGetIDResponse(reqPacket.getMessageID(), Utils.intToBytesLittle(9), callbackStatus);
		client.sendMediusMessage(respPacket);
	}

}
