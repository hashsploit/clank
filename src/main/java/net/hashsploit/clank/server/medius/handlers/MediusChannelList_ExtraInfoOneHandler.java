package net.hashsploit.clank.server.medius.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.ChannelList_ExtraInfoOneRequest;
import net.hashsploit.clank.server.medius.serializers.ChannelList_ExtraInfoOneResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusChannelList_ExtraInfoOneHandler extends MediusPacketHandler {

	private ChannelList_ExtraInfoOneRequest reqPacket;
	private ChannelList_ExtraInfoOneResponse respPacket;

	public MediusChannelList_ExtraInfoOneHandler() {
		super(MediusMessageType.ChannelList_ExtraInfo1, MediusMessageType.ChannelList_ExtraInfoResponse);
	}

	@Override
	public void read(MediusMessage mm) {
		reqPacket = new ChannelList_ExtraInfoOneRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public void write(MediusClient client) {

		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		int cityWorldId = server.getChannel().getId();

		byte[] callbackStatus = Utils.intToBytes(MediusCallbackStatus.SUCCESS.getValue());
		byte[] mediusWorldID = Utils.intToBytesLittle(cityWorldId);
		
		// FIXME: bad
		byte[] playerCount = Utils.shortToBytesLittle((short) server.getChannelActivePlayerCountById(cityWorldId));
		byte[] maxPlayers = Utils.shortToBytesLittle((short) 224);
		
		byte[] worldSecurityLevelType = Utils.intToBytesLittle(0);
		byte[] genericField1 = Utils.intToBytesLittle(1);
		byte[] genericField2 = Utils.intToBytesLittle(1);
		byte[] genericField3 = Utils.intToBytesLittle(0);
		byte[] genericField4 = Utils.intToBytesLittle(0);
		byte[] genericFieldFilter = Utils.intToBytesLittle(32);
		byte[] lobbyName = Utils.buildByteArrayFromString(server.getChannelById(cityWorldId).getName(), MediusConstants.LOBBYNAME_MAXLEN.getValue());
		byte[] endOfList = Utils.hexStringToByteArray("01000000");

		respPacket = new ChannelList_ExtraInfoOneResponse(reqPacket.getMessageID(), callbackStatus, mediusWorldID, playerCount, maxPlayers, worldSecurityLevelType, genericField1, genericField2, genericField3, genericField4, genericFieldFilter, lobbyName, endOfList);

		client.sendMediusMessage(respPacket);
	}
}
