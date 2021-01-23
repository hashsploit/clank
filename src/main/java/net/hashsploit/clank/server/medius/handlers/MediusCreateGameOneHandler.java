package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.medius.serializers.CreateGameResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusCreateGameOneHandler extends MediusPacketHandler {

	private CreateGameOneRequest reqPacket;
	private CreateGameResponse respPacket;

	public MediusCreateGameOneHandler() {
		super(MediusMessageType.CreateGame1, MediusMessageType.CreateGameResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new CreateGameOneRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		
		final int newWorldId = server.createGame();
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.FAILURE;
		
		if (newWorldId > 0) {
			callbackStatus = MediusCallbackStatus.SUCCESS;
		}
		
		respPacket = new CreateGameResponse(reqPacket.getMessageId(), Utils.intToBytesLittle(callbackStatus.getValue()), Utils.intToBytesLittle(newWorldId));

		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}

}
