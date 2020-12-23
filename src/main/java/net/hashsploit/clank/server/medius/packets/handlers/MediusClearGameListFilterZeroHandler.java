package net.hashsploit.clank.server.medius.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.packets.serializers.ClearGameListFilterResponse;
import net.hashsploit.clank.server.medius.packets.serializers.ClearGameListFilterZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusClearGameListFilterZeroHandler extends MediusPacketHandler {
	
	private ClearGameListFilterZeroRequest reqPacket;
	private ClearGameListFilterResponse respPacket;
	
	public MediusClearGameListFilterZeroHandler() {
		super(MediusMessageType.ClearGameListFilter0, MediusMessageType.ClearGameListFilterResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new ClearGameListFilterZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {
		//byte[] callbackStatus = Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue());
		byte[] callbackStatus = Utils.hexStringToByteArray("2CFCFFFF");

		respPacket = new ClearGameListFilterResponse(reqPacket.getMessageID(), callbackStatus);
		
		client.sendMediusMessage(respPacket);
	}


}
