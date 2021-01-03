package net.hashsploit.clank.server.medius.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusSetLobbyWorldFilterHandler extends MediusPacketHandler {

	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	byte[] filter1 = new byte[4];
	byte[] filter2 = new byte[4];
	byte[] filter3 = new byte[4];
	byte[] filter4 = new byte[4];
	byte[] lobbyFilterType = new byte[4];
	byte[] lobbyFilterMaskLevelType = new byte[4];
	
	public MediusSetLobbyWorldFilterHandler() {
		super(MediusMessageType.SetLobbyWorldFilter,MediusMessageType.SetLobbyWorldFilterResponse);
	}
	
	public void read(MediusClient client, MediusMessage mm) {
		// Process the packet
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(new byte[3]);
		buf.get(filter1);
		buf.get(filter2);
		buf.get(filter3);
		buf.get(filter4);
		buf.get(lobbyFilterType);
		buf.get(lobbyFilterMaskLevelType);
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(Utils.intToBytes(MediusCallbackStatus.SUCCESS.getValue()));
			outputStream.write(filter1);
			outputStream.write(filter2);
			outputStream.write(filter3);
			outputStream.write(filter4);
			outputStream.write(lobbyFilterType);
			outputStream.write(lobbyFilterMaskLevelType);
//			outputStream.write(Utils.hexStringToByteArray("00000000000000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF0000000020000000"));
//			outputStream.write(Utils.hexStringToByteArray("00000000000000010000000800000000000000000000000100000020000000"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(new MediusMessage(responseType, outputStream.toByteArray()));
		return response;
	}

}
