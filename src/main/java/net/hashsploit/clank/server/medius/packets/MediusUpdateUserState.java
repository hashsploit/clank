package net.hashsploit.clank.server.medius.packets;

import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusUpdateUserState extends MediusPacket {

	public MediusUpdateUserState() {
		super(MediusPacketType.UpdateUserState, null);
	}

	@Override
	public MediusMessage write(Client client) {
		// Process the packet
		return null;
	}

}
