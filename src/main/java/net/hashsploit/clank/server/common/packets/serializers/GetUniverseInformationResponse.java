package net.hashsploit.clank.server.common.packets.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.BillingServiceProvider;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class GetUniverseInformationResponse extends MediusMessage {
	
	private byte[] messageId;
	private MediusCallbackStatus callbackStatus;
	private int infoFilter;
	private int universeId;
	private String universeName; // 128
	private String dns; // 128
	private int port;
	private String universeDescription; // 256
	private int status;
	private int userCount;
	private int maxUsers;
	private BillingServiceProvider bsp; // 8
	private String billingSystemName; // 128
	private String extendedInfo; // 128
	private String svoUrl; // 128
	private boolean endOfList;

	public GetUniverseInformationResponse(byte[] messageId, MediusCallbackStatus callbackStatus, int infoFilter, int universeId,
			String universeName, String dns, int port,
			String universeDescription, int status, int userCount,
			int maxUsers, BillingServiceProvider bsp, String billingSystemName,
			String extendedInfo, String svoUrl, boolean endOfList) {
		super(MediusMessageType.UniverseVariableInformationResponse);

		this.messageId = messageId;
		this.callbackStatus = callbackStatus;
		this.infoFilter = infoFilter;
		this.universeId = universeId;
		this.universeName = universeName;
		this.dns = dns;
		this.port = port;
		this.universeDescription = universeDescription;
		this.status = status;
		this.userCount = userCount;
		this.maxUsers = maxUsers;
		this.bsp = bsp;
		this.billingSystemName = billingSystemName;
		this.extendedInfo = extendedInfo;
		this.svoUrl = svoUrl;
		this.endOfList = endOfList;
	}

	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
			outputStream.write(Utils.intToBytesLittle(infoFilter));
			outputStream.write(Utils.intToBytesLittle(universeId));
			outputStream.write(Utils.buildByteArrayFromString(universeName, MediusConstants.UNIVERSENAME_MAXLEN.getValue()));
			outputStream.write(Utils.buildByteArrayFromString(dns, MediusConstants.UNIVERSEDNS_MAXLEN.getValue()));
			outputStream.write(Utils.intToBytesLittle(port));
			outputStream.write(Utils.buildByteArrayFromString(universeDescription, MediusConstants.UNIVERSEDESCRIPTION_MAXLEN.getValue()));
			outputStream.write(Utils.intToBytesLittle(status));
			outputStream.write(Utils.intToBytesLittle(userCount));
			outputStream.write(Utils.intToBytesLittle(maxUsers));
			outputStream.write(Utils.buildByteArrayFromString(bsp.value, MediusConstants.UNIVERSE_BSP_MAXLEN.getValue()));
			outputStream.write(Utils.buildByteArrayFromString(billingSystemName, MediusConstants.UNIVERSE_BSP_NAME_MAXLEN.getValue()));
			outputStream.write(Utils.buildByteArrayFromString(extendedInfo, MediusConstants.UNIVERSE_EXTENDED_INFO_MAXLEN.getValue()));
			outputStream.write(Utils.buildByteArrayFromString(svoUrl, MediusConstants.UNIVERSE_SVO_URL_MAXLEN.getValue()));
			outputStream.write(Utils.hexStringToByteArray(endOfList ? "01" : "00")); // EndOfList (char) 00 or 01
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
}
