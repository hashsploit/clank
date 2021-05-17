package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.ServerHelloRequest;
import net.hashsploit.clank.server.medius.serializers.ServerHelloResponse;

public class MediusServerHelloHandler extends MediusPacketHandler {

	private ServerHelloRequest reqPacket;
	private ServerHelloResponse respPacket;

	public MediusServerHelloHandler() {
		super(MediusMessageType.MediusServerHelloRequest, MediusMessageType.MediusServerHelloResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new ServerHelloRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		String serverHello = "Server Hello";

		respPacket = new ServerHelloResponse(reqPacket.getMessageId(), serverHello);

		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}
}