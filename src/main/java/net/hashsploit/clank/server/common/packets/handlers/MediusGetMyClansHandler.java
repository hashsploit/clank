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
import net.hashsploit.clank.utils.Utils;

public class MediusGetMyClansHandler extends MediusPacketHandler {

	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.value];
	
	public MediusGetMyClansHandler() {
		super(MediusMessageType.GetMyClans, MediusMessageType.GetMyClansResponse);
	}
	
	public void read(MediusMessage mm) {
		// Process the packet
		logger.fine("Get my clans: " + Utils.bytesToHex(mm.getPayload()));

		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(sessionKey);
	}

	@Override
	public void write(MediusClient client) {
		logger.fine("Message ID : " + Utils.bytesToHex(messageID));
		logger.fine("Session Key: " + Utils.bytesToHex(sessionKey));

		byte[] statusCode = Utils.intToBytesLittle(MediusCallbackStatus.NO_RESULT.getValue());
		byte[] clanID = Utils.intToBytesLittle(0);
		byte[] applicationID = Utils.intToBytesLittle(0);
		byte[] clanName = Utils.buildByteArrayFromString("", MediusConstants.CLANNAME_MAXLEN.value); // 32
		byte[] leaderAccountID = Utils.intToBytesLittle(0);
		byte[] leaderAccountName = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTNAME_MAXLEN.value);
		byte[] stats = Utils.buildByteArrayFromString("", MediusConstants.CLANSTATS_MAXLEN.value);
		byte[] clanStatus = Utils.intToBytesLittle(0);
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

		client.sendMediusMessage(new MediusMessage(responseType, outputStream.toByteArray()));
	}

}
