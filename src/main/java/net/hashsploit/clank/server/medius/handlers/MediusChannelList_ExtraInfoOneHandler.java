package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.Channel;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusWorldSecurityLevelType;
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
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new ChannelList_ExtraInfoOneRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		List<Channel> channels = server.getChannels();
		List<MediusMessage> responses = new ArrayList<MediusMessage>();
		
		MediusCallbackStatus callbackStatus = MediusCallbackStatus.NO_RESULT;
		int mediusWorldId = 0;
		int playerCount = 0;
		int maxPlayers = 0;
		int worldSecurityLevelType = 0;
		byte[] genericField1 = new byte[4];
		byte[] genericField2 = new byte[4];
		byte[] genericField3 = new byte[4];
		byte[] genericField4 = new byte[4];
		byte[] genericFieldFilter = new byte[4];
		String lobbyName = "";
		boolean endOfList = true;
		
		if (channels.size() > 0) {
			for (int i=0; i<channels.size(); i++) {
				final Channel channel = channels.get(i);
				
				callbackStatus = MediusCallbackStatus.SUCCESS;
				mediusWorldId = channel.getId();
				playerCount = server.getChannelActivePlayerCountById(channel.getId());
				maxPlayers = channel.getCapacity();
				
				// FIXME: hardcoded
				worldSecurityLevelType = MediusWorldSecurityLevelType.WORLD_SECURITY_NONE.value;
				genericField1 = Utils.intToBytesLittle(1);
				genericField2 = Utils.intToBytesLittle(1);
				genericField3 = Utils.intToBytesLittle(0);
				genericField4 = Utils.intToBytesLittle(0);
				genericFieldFilter = Utils.intToBytesLittle(32);
				
				lobbyName = channel.getName();
				endOfList = i == channels.size() - 1;
				
				respPacket = new ChannelList_ExtraInfoOneResponse(reqPacket.getMessageId(), callbackStatus, mediusWorldId, (short) playerCount, (short) maxPlayers, worldSecurityLevelType, genericField1, genericField2, genericField3, genericField4, genericFieldFilter, lobbyName, endOfList);
				responses.add(respPacket);
			}
		} else {
			respPacket = new ChannelList_ExtraInfoOneResponse(reqPacket.getMessageId(), callbackStatus, mediusWorldId, (short) playerCount, (short) maxPlayers, worldSecurityLevelType, genericField1, genericField2, genericField3, genericField4, genericFieldFilter, lobbyName, endOfList);
			responses.add(respPacket);
		}
		
		return responses;
	}
}
