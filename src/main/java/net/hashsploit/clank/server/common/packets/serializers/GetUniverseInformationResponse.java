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
			
			/*
			 * InfoFilter is a bitmask.
			 * 
			 * Bit 1:   Always Clear
			 * Bit 2:   Always Clear
			 * Bit 3:   Set If the UniverseID field exists
			 * Bit 4:   Set If the UniverseName field exists
			 * Bit 5:   Set If the DNS & Port fields exist
			 * Bit 6:   Set If the UniverseDescription field exists
			 * Bit 7:   Set If the Status & UserCount & MaxUser fields exist
			 * Bit 8:   Set If the UniverseBilling & BillingSystemName fields exist
			 * Bit 9:   Set If the ExtendedInfo field exists
			 * Bit 10:  Set If the SvoURL field exists
			 */
			
			// If the 3rd bit is set, include UniverseId
			if ((infoFilter & 4) != 0) {
				outputStream.write(Utils.intToBytesLittle(universeId));
			}
			
			// If the 4th bit is set, include the UniverseName
			if ((infoFilter & 8) != 0) {
				outputStream.write(Utils.buildByteArrayFromString(universeName, MediusConstants.UNIVERSENAME_MAXLEN.value));
			}
			
			// If the 5th bit is set, include the DNS & Port
			if ((infoFilter & 16) != 0) {
				outputStream.write(Utils.buildByteArrayFromString(dns, MediusConstants.UNIVERSEDNS_MAXLEN.value));
				outputStream.write(Utils.intToBytesLittle(port));
			}
			
			// If the 6th bit is set, include UniverseDescription
			if ((infoFilter & 32) != 0) {
				outputStream.write(Utils.buildByteArrayFromString(universeDescription, MediusConstants.UNIVERSEDESCRIPTION_MAXLEN.value));
			}
			
			// If the 7th bit is set, include Status & UserCount & MaxUser
			if ((infoFilter & 64) != 0) {
				outputStream.write(Utils.intToBytesLittle(status));
				outputStream.write(Utils.intToBytesLittle(userCount));
				outputStream.write(Utils.intToBytesLittle(maxUsers));
			}
			
			// If the 8th bit is set, include UniverseBilling & BillingSystemName
			if ((infoFilter & 128) != 0) {
				outputStream.write(Utils.buildByteArrayFromString(bsp.value, MediusConstants.UNIVERSE_BSP_MAXLEN.value));
				outputStream.write(Utils.buildByteArrayFromString(billingSystemName, MediusConstants.UNIVERSE_BSP_NAME_MAXLEN.value));
			}

			// If the 9th bit is set, include ExtendedInfo
			if ((infoFilter & 256) != 0) {
				outputStream.write(Utils.buildByteArrayFromString(extendedInfo, MediusConstants.UNIVERSE_EXTENDED_INFO_MAXLEN.value));
			}

			// If the 10th bit is set, include ExtendedInfo
			if ((infoFilter & 512) != 0) {
				outputStream.write(Utils.buildByteArrayFromString(svoUrl, MediusConstants.UNIVERSE_SVO_URL_MAXLEN.value));
			}
			
			outputStream.write(Utils.hexStringToByteArray(endOfList ? "01" : "00")); // EndOfList (char) 00 or 01
			outputStream.write(Utils.hexStringToByteArray("000000")); // padding
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
}
