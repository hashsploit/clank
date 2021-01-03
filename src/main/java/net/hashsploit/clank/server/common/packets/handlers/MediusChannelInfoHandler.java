package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusLobbyServer;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.ChannelInfoRequest;
import net.hashsploit.clank.server.common.packets.serializers.ChannelInfoResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusChannelInfoHandler extends MediusPacketHandler {

	private ChannelInfoRequest reqPacket;
	private ChannelInfoResponse respPacket;

	public MediusChannelInfoHandler() {
		super(MediusMessageType.ChannelInfo, MediusMessageType.ChannelInfoResponse);
	}

	@Override
	public void read(MediusMessage mm) {
		reqPacket = new ChannelInfoRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public void write(MediusClient client) {

		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));

		MediusLobbyServer server = (MediusLobbyServer) client.getServer();

		byte[] lobbyName = Utils.buildByteArrayFromString(server.getChannelById(Utils.bytesToIntLittle(reqPacket.getWorldID())).getName(), MediusConstants.LOBBYNAME_MAXLEN.value);
		byte[] activePlayerCount = Utils.intToBytesLittle(server.getChannelActivePlayerCountById(Utils.bytesToIntLittle(reqPacket.getWorldID())));

		// FIXME: hardcoded (bad)
		byte[] maxPlayers = Utils.intToBytesLittle(224);

		respPacket = new ChannelInfoResponse(reqPacket.getMessageID(), callbackStatus, lobbyName, activePlayerCount, maxPlayers);
		client.sendMediusMessage(respPacket);
	}
}
