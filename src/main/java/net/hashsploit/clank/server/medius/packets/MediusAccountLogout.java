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

public class MediusAccountLogout extends MediusPacket {

	public MediusAccountLogout() {
		super(MediusPacketType.AccountLogout, MediusPacketType.AccountLogoutResponse);
	}
	@Override
	public void read(MediusMessage mm) {
		logger.fine("Logout data: " + Utils.bytesToHex(mm.getPayload()));
	}

	@Override
	public MediusMessage write(Client client) {
		return null;
	}

}
