package net.hashsploit.clank.server.medius.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.medius.packets.serializers.GetAllAnnouncementsRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GetAnnouncementsResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGetAllAnnouncementsHandler extends MediusPacketHandler {

	private GetAllAnnouncementsRequest reqPacket;
	private GetAnnouncementsResponse respPacket;
	
	public MediusGetAllAnnouncementsHandler() {
		super(MediusPacketType.GetAllAnnouncements,MediusPacketType.GetAnnouncementsResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new GetAllAnnouncementsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue());
		byte[] announcementID = Utils.intToBytesLittle(10);
		byte[] announcement = Utils.buildByteArrayFromString("Announcment TEST", MediusConstants.ANNOUNCEMENT_MAXLEN.getValue());
		byte[] endOfList = Utils.hexStringToByteArray("01000000");
		
		respPacket = new GetAnnouncementsResponse(reqPacket.getMessageID(), callbackStatus, announcementID, announcement, endOfList);
		
		return respPacket;	}

}
