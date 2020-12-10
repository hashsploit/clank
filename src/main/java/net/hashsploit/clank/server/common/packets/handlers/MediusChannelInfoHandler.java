package net.hashsploit.clank.server.common.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.ChannelInfoRequest;
import net.hashsploit.clank.server.common.packets.serializers.ChannelInfoResponse;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
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

    	byte[] lobbyName = Utils.buildByteArrayFromString(client.getServer().getLogicHandler().getChannelLobbyName(Utils.bytesToIntLittle(reqPacket.getWorldID())), MediusConstants.LOBBYNAME_MAXLEN.getValue());
		byte[] activePlayerCount = Utils.intToBytesLittle(client.getServer().getLogicHandler().getChannelActivePlayerCount(Utils.bytesToIntLittle(reqPacket.getWorldID())));
		byte[] maxPlayers = Utils.intToBytesLittle(224);

		respPacket = new ChannelInfoResponse(reqPacket.getMessageID(), callbackStatus, lobbyName, activePlayerCount, maxPlayers);
		client.sendMediusMessage(respPacket);
	}
}
