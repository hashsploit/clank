package net.hashsploit.clank.server.medius.packets.handlers;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.packets.serializers.ChatToggleRequest;
import net.hashsploit.clank.server.medius.packets.serializers.ChatToggleResponse;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusChatToggleHandler extends MediusPacketHandler {
	
	private ChatToggleRequest reqPacket;
	private ChatToggleResponse respPacket;
	
	public MediusChatToggleHandler() {
		super(MediusPacketType.ChatToggle, MediusPacketType.ChatToggleResponse);
	}
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new ChatToggleRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public MediusPacket write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.MediusSuccess.getValue()));
		
		respPacket = new ChatToggleResponse(reqPacket.getMessageID(), callbackStatus);
		
		return respPacket;
	}

}
