package net.hashsploit.clank.server.medius.packets;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusChatToggle extends MediusPacket {

	public MediusChatToggle() {
		super(MediusPacketType.ChatToggle, MediusPacketType.ChatToggleResponse);
	}
	@Override
	public void read(MediusMessage mm) {
		// example input where username is GGGGGGGGGGGG
		// 0b2e0001963100474747474747474747474747000000000000003133000000000000000000007b24000000000000000000
		logger.fine("Chat toggle data: " + Utils.bytesToHex(mm.getPayload()));
	}

	@Override
	public MediusMessage write(Client client) {
		byte[] response = Utils.hexStringToByteArray("31000000000000000000000000000000000000000000000000000000");
		return new MediusMessage(responseType, response);
	}

}
