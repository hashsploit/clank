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
import net.hashsploit.clank.server.medius.objects.MediusApplicationType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusWorldSecurityLevelType;
import net.hashsploit.clank.server.medius.packets.serializers.ChannelInfoRequest;
import net.hashsploit.clank.server.medius.packets.serializers.ChannelInfoResponse;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.medius.packets.serializers.GetWorldSecurityLevelRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GetWorldSecurityLevelResponse;
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
		byte[] appType = Utils.intToBytesLittle(MediusApplicationType.MEDIUS_APP_TYPE_GAME.getValue());
		byte[] worldSecurityLevelType = Utils.intToBytesLittle(MediusWorldSecurityLevelType.WORLD_SECURITY_NONE.getValue()); // TODO: Get this from the actual world
		
		respPacket = new GetWorldSecurityLevelResponse(reqPacket.getMessageID(), callbackStatus, reqPacket.getWorldId(), appType, worldSecurityLevelType);
		client.sendMediusMessage(respPacket);
	}
}
