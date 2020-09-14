package net.hashsploit.clank.server.medius.packets;

import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.utils.Utils;

public class MediusUpdateUserState extends MediusPacket {

	private static final Logger logger = Logger.getLogger("");

	public MediusUpdateUserState() {
		super(MediusPacketType.UpdateUserState);
	}

	@Override
	public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) {
		// Process the packet
		logger.fine("Update user state: " + Utils.bytesToHex(packetData));

		/*
		 * byte[] finalPayload = Utils.hexStringToByteArray(
		 * "0a1e00019731000000000000000000000000000000000000000000000000000000");
		 * 
		 * 
		 * logger.fine("Final payload: " + Utils.bytesToHex(finalPayload)); ByteBuf msg
		 * = Unpooled.copiedBuffer(finalPayload); ctx.write(msg); // (1) ctx.flush(); //
		 * (2)
		 */
	}

}
