package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusJoinGame extends MediusPacket {

	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	byte[] worldIdToJoin = new byte[4];
	byte[] joinType = new byte[4];
	byte[] gamePassword = new byte[MediusConstants.GAMEPASSWORD_MAXLEN.getValue()];
	byte[] gameHostType = new byte[4];
	byte[] RSApubKey = new byte[10];
	
	public MediusJoinGame() {
		super(MediusPacketType.JoinGame, MediusPacketType.JoinGameResponse);
		
	}
	
	public void read(MediusMessage mm) {
		// Process the packet
		logger.fine("Get my clans: " + Utils.bytesToHex(mm.getPayload()));

		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(sessionKey);
		buf.get(new byte[2]);//padding
		buf.get(worldIdToJoin);
		buf.get(joinType);
		buf.get(gamePassword);
		buf.get(gameHostType);
		logger.fine("Message ID : " + Utils.bytesToHex(messageID));
		logger.fine("worldIdToJoin: " + Utils.bytesToHex(worldIdToJoin));
		logger.fine("joinType: " + Utils.bytesToHex(joinType));
		logger.fine("gamePassword: " + Utils.bytesToHex(gamePassword));
		logger.fine("gameHostType: " + Utils.bytesToHex(gameHostType));
		logger.fine("Data: " + Utils.bytesToHex(mm.getPayload()));
	}

	@Override
	public MediusMessage write(Client client) {
		logger.fine("Message ID : " + Utils.bytesToHex(messageID));
		logger.fine("Session Key: " + Utils.bytesToHex(sessionKey));

		return null;
	}

}
