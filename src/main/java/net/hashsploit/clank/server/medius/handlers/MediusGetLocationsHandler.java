package net.hashsploit.clank.server.medius.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.config.objects.LocationConfig;
import net.hashsploit.clank.server.ChatColor;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusGetLocationsHandler extends MediusPacketHandler {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.value];
		
	public MediusGetLocationsHandler() {
		super(MediusMessageType.GetLocations,MediusMessageType.GetLocationsResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		// Process the packet
		logger.fine("Get locations: " + Utils.bytesToHex(mm.getPayload()));

		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(sessionKey);
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		// FIXME: bad location
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		final LocationConfig location = server.getLocation();
		
		byte[] locationId = Utils.intToBytesLittle(location.getId()); // random location
		byte[] locationName = Utils.padByteArray(ChatColor.parse(location.getName()), MediusConstants.LOCATIONNAME_MAXLEN.value);
		byte[] statusCode = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
		byte[] endOfList = Utils.hexStringToByteArray("01000000");

		logger.fine("Message ID: " + Utils.bytesToHex(messageID));
		logger.fine("Padding: 000000");
		logger.fine("locationID: " + Utils.bytesToHex(locationId));
		logger.fine("locationName: " + Utils.bytesToHex(locationName));
		logger.fine("statusCode: " + Utils.bytesToHex(statusCode));
		logger.fine("endOfList: " + Utils.bytesToHex(endOfList));

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(locationId);
			outputStream.write(locationName);
			outputStream.write(statusCode);
			outputStream.write(endOfList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(new MediusMessage(responseType, outputStream.toByteArray()));
		return response;
		
	}

}
