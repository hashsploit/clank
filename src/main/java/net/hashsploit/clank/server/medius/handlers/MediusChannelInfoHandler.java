package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.Channel;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.ChannelInfoRequest;
import net.hashsploit.clank.server.medius.serializers.ChannelInfoResponse;
import net.hashsploit.clank.utils.Utils;



public class MediusChannelInfoHandler extends MediusPacketHandler {

	private ChannelInfoRequest reqPacket;
	private ChannelInfoResponse respPacket;

	public MediusChannelInfoHandler() {
		super(MediusMessageType.ChannelInfo, MediusMessageType.ChannelInfoResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new ChannelInfoRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));

		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		Channel channel = server.getChannelById(Utils.bytesToIntLittle(reqPacket.getWorldID()));
		
		if (channel == null) {
			logger.severe("Requested channel: " + Utils.bytesToIntLittle(reqPacket.getWorldID()) + " does not exist! Using default...");
			channel = server.getChannelById(1);
		}
		
		byte[] lobbyName = Utils.buildByteArrayFromString(channel.getName(), MediusConstants.LOBBYNAME_MAXLEN.value);
		byte[] activePlayerCount = Utils.intToBytesLittle(server.getChannelActivePlayerCountById(Utils.bytesToIntLittle(reqPacket.getWorldID())));
		byte[] maxPlayers = Utils.intToBytesLittle(channel.getCapacity());

		respPacket = new ChannelInfoResponse(reqPacket.getMessageID(), callbackStatus, lobbyName, activePlayerCount, maxPlayers);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}
}
