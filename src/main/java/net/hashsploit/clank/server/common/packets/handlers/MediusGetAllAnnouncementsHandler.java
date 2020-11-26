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
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.common.packets.serializers.GetAllAnnouncementsRequest;
import net.hashsploit.clank.server.common.packets.serializers.GetAnnouncementsResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGetAllAnnouncementsHandler extends MediusPacketHandler {

	private GetAllAnnouncementsRequest reqPacket;
	private GetAnnouncementsResponse respPacket;
	
	public MediusGetAllAnnouncementsHandler() {
		super(MediusMessageType.GetAllAnnouncements,MediusMessageType.GetAnnouncementsResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new GetAllAnnouncementsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusMessage write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytes(MediusCallbackStatus.SUCCESS.getValue());
		byte[] announcementID = Utils.intToBytesLittle(10);
		byte[] announcement = Utils.buildByteArrayFromString("Announcment TEST", MediusConstants.ANNOUNCEMENT_MAXLEN.getValue());
		byte[] endOfList = Utils.hexStringToByteArray("01000000");
		
		respPacket = new GetAnnouncementsResponse(reqPacket.getMessageID(), callbackStatus, announcementID, announcement, endOfList);
		
		return respPacket;
	}

}
