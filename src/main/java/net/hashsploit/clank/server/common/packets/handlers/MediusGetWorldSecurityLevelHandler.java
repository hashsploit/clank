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
import net.hashsploit.clank.server.common.objects.MediusApplicationType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.MediusWorldSecurityLevelType;
import net.hashsploit.clank.server.common.packets.serializers.ChannelInfoRequest;
import net.hashsploit.clank.server.common.packets.serializers.ChannelInfoResponse;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.common.packets.serializers.GetWorldSecurityLevelRequest;
import net.hashsploit.clank.server.common.packets.serializers.GetWorldSecurityLevelResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGetWorldSecurityLevelHandler extends MediusPacketHandler {

	private GetWorldSecurityLevelRequest reqPacket;
	private GetWorldSecurityLevelResponse respPacket;
	
	public MediusGetWorldSecurityLevelHandler() {
		super(MediusMessageType.GetWorldSecurityLevel, MediusMessageType.GetWorldSecurityLevelResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new GetWorldSecurityLevelRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public void write(MediusClient client) {
		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));
		byte[] appType = Utils.intToBytesLittle(MediusApplicationType.MEDIUS_APP_TYPE_GAME.value);
		byte[] worldSecurityLevelType = Utils.intToBytesLittle(MediusWorldSecurityLevelType.WORLD_SECURITY_NONE.getValue()); // TODO: Get this from the actual world
		
		respPacket = new GetWorldSecurityLevelResponse(reqPacket.getMessageID(), callbackStatus, reqPacket.getWorldId(), appType, worldSecurityLevelType);
		client.sendMediusMessage(respPacket);
	}
}
