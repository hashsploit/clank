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
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.medius.packets.serializers.GameList_ExtraInfoZeroRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusGameList_ExtraInfoZeroHandler extends MediusPacket {
	
	private GameList_ExtraInfoZeroRequest reqPacket;
	
	public MediusGameList_ExtraInfoZeroHandler() {
		super(MediusPacketType.GameList_ExtraInfo0,MediusPacketType.GameList_ExtraInfoResponse0);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new GameList_ExtraInfoZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusMessage write(MediusClient client) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(reqPacket.getMessageID());
			outputStream.write(Utils.hexStringToByteArray("0000000000000065EB0000020000000800000045070000000000040000000028000000000000000800E41100000000010000000400000031763120662e6f20646f7820202020202020303030303030323830303030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000"));
		} catch (IOException e) {
			
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new MediusMessage(responseType, outputStream.toByteArray());
	}

}
