package net.hashsploit.clank.server.medius.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusClanInvitationResponseStatus;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.utils.Utils;

public class CheckMyClanInvitationsResponse extends MediusPacket {

	private byte[] messageId;
	private MediusCallbackStatus callbackStatus;
	private int clanInvitationId;
	private int clanId;
	private MediusClanInvitationResponseStatus clanInvitationStatus;
	private String message;
	private int leaderAccountId;
	private String leaderAccountName;
	private boolean endOfList;

	public CheckMyClanInvitationsResponse(byte[] messageId, MediusCallbackStatus callbackStatus, int clanInvitationId, int clanId, MediusClanInvitationResponseStatus clanInvitationResponseStatus, String message, int leaderAccountId, String leaderAccountName, boolean endOfList) {
		super(MediusPacketType.CheckMyClanInvitationsResponse);

		this.messageId = messageId;
		this.callbackStatus = callbackStatus;
		this.clanInvitationId = clanInvitationId;
		this.clanId = clanId;
		this.clanInvitationStatus = clanInvitationResponseStatus;
		this.message = message;
		this.leaderAccountId = leaderAccountId;
		this.leaderAccountName = leaderAccountName;
		this.endOfList = endOfList;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
			outputStream.write(Utils.intToBytesLittle(clanInvitationId));
			outputStream.write(Utils.intToBytesLittle(clanId));
			outputStream.write(Utils.intToBytesLittle(clanInvitationStatus.getValue()));
			outputStream.write(Utils.buildByteArrayFromString(message, MediusConstants.CLANINVITEMSG_MAXLEN.getValue()));
			outputStream.write(Utils.intToBytesLittle(leaderAccountId));
			outputStream.write(Utils.buildByteArrayFromString(leaderAccountName, MediusConstants.USERNAME_MAXLEN.getValue()));
			outputStream.write(Utils.hexStringToByteArray(endOfList ? "01" : "00")); // EndOfList (char) 00 or 01
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
}
