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
import net.hashsploit.clank.utils.Utils;

public class MediusGetMyClansHandler extends MediusPacketHandler {

	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	
	public MediusGetMyClansHandler() {
		super(MediusPacketType.GetMyClans, MediusPacketType.GetMyClansResponse);
	}
	
	public void read(MediusPacket mm) {
		// Process the packet
		logger.fine("Get my clans: " + Utils.bytesToHex(mm.getPayload()));

		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(sessionKey);
	}

	@Override
	public MediusPacket write(MediusClient client) {
		logger.fine("Message ID : " + Utils.bytesToHex(messageID));
		logger.fine("Session Key: " + Utils.bytesToHex(sessionKey));

		byte[] statusCode = Utils.intToBytesLittle(0);
		byte[] clanID = Utils.intToBytesLittle(5);
		byte[] applicationID = Utils.intToBytesLittle(1);
		byte[] clanName = Utils.buildByteArrayFromString("Test clan name", MediusConstants.CLANNAME_MAXLEN.getValue()); // 32
		byte[] leaderAccountID = Utils.intToBytesLittle(1);
		byte[] leaderAccountName = Utils.buildByteArrayFromString("LeaderAccountName", MediusConstants.ACCOUNTNAME_MAXLEN.getValue());
		byte[] stats = Utils.buildByteArrayFromString("0", MediusConstants.CLANSTATS_MAXLEN.getValue());
		//byte[] clanStatus = Utils.intToBytesLittle(MediusCallbackStatus.MediusSuccess.getValue());
		byte[] clanStatus = Utils.intToBytesLittle(MediusCallbackStatus.MediusSuccess.getValue());
		// byte[] endOfList = Utils.hexStringToByteArray("01000000");
		byte[] endOfList = Utils.hexStringToByteArray("01000000");

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID); // 21
			outputStream.write(Utils.hexStringToByteArray("000000")); // 3
			outputStream.write(statusCode); // 4
			outputStream.write(clanID); // 4
			outputStream.write(applicationID); // 4
			outputStream.write(clanName); // 32
			outputStream.write(leaderAccountID); // 4
			outputStream.write(leaderAccountName); // 32
			outputStream.write(stats); // 256
			outputStream.write(clanStatus); // 4
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new MediusPacket(responseType, outputStream.toByteArray());
	}

}
