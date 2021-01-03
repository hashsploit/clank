package net.hashsploit.clank.server.medius.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.ChannelInfoRequest;
import net.hashsploit.clank.server.medius.serializers.ChannelInfoResponse;
import net.hashsploit.clank.server.medius.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.serializers.GameInfoZeroResponse;
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
		
    	byte[] lobbyName = Utils.buildByteArrayFromString(server.getChannelById(Utils.bytesToIntLittle(reqPacket.getWorldID())).getName(), MediusConstants.LOBBYNAME_MAXLEN.getValue());
		byte[] activePlayerCount = Utils.intToBytesLittle(server.getChannelActivePlayerCountById(Utils.bytesToIntLittle(reqPacket.getWorldID())));
		
		// FIXME: hardcoded (bad)
		byte[] maxPlayers = Utils.intToBytesLittle(224);

		respPacket = new ChannelInfoResponse(reqPacket.getMessageID(), callbackStatus, lobbyName, activePlayerCount, maxPlayers);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}
}
