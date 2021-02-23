package net.hashsploit.clank.server.medius.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.server.ChatColor;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusAuthenticationServer;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.Location;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusGetLocationsHandler extends MediusPacketHandler {

	private byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.value];

	public MediusGetLocationsHandler() {
		super(MediusMessageType.GetLocations, MediusMessageType.GetLocationsResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageId);
		buf.get(sessionKey);
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		List<Location> locations = new ArrayList<Location>();
		List<MediusMessage> responses = new ArrayList<MediusMessage>();

		if (client.getServer().getEmulationMode() == EmulationMode.MEDIUS_AUTHENTICATION_SERVER) {
			locations = ((MediusAuthenticationServer) client.getServer()).getLocations();
		} else if (client.getServer().getEmulationMode() == EmulationMode.MEDIUS_LOBBY_SERVER) {
			locations = ((MediusLobbyServer) client.getServer()).getLocations();
		}

		MediusCallbackStatus callbackStatus = MediusCallbackStatus.NO_RESULT;
		boolean endOfList = true;

		if (locations.size() > 0) {
			for (int i = 0; i < locations.size(); i++) {
				final Location location = locations.get(i);
				callbackStatus = MediusCallbackStatus.SUCCESS;

				endOfList = i == locations.size() - 1;

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				try {
					outputStream.write(messageId);
					outputStream.write(Utils.hexStringToByteArray("000000"));
					outputStream.write(Utils.intToBytesLittle(location.getId()));
					outputStream.write(Utils.padByteArray(ChatColor.parse(location.getName()), MediusConstants.LOCATIONNAME_MAXLEN.value));
					outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
					outputStream.write(Utils.intToBytesLittle(endOfList ? 1 : 0));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				responses.add(new MediusMessage(MediusMessageType.GetLocationsResponse, outputStream.toByteArray()));
			}
		} else {

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				outputStream.write(messageId);
				outputStream.write(Utils.hexStringToByteArray("000000"));
				outputStream.write(Utils.intToBytesLittle(0));
				outputStream.write(new byte[MediusConstants.LOCATIONNAME_MAXLEN.value]);
				outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
				outputStream.write(Utils.intToBytesLittle(1));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			responses.add(new MediusMessage(MediusMessageType.GetLocationsResponse, outputStream.toByteArray()));
		}

		return responses;
	}

}
