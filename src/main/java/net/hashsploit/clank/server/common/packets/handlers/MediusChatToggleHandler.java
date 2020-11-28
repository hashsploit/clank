package net.hashsploit.clank.server.common.packets.handlers;

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
import net.hashsploit.clank.server.common.packets.serializers.ChatToggleRequest;
import net.hashsploit.clank.server.common.packets.serializers.ChatToggleResponse;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusChatToggleHandler extends MediusPacketHandler {
	
	private ChatToggleRequest reqPacket;
	private ChatToggleResponse respPacket;
	
	public MediusChatToggleHandler() {
		super(MediusMessageType.ChatToggle, MediusMessageType.ChatToggleResponse);
	}
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new ChatToggleRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public MediusMessage write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));
		
		respPacket = new ChatToggleResponse(reqPacket.getMessageID(), callbackStatus);
		
		return respPacket;
	}

}
