package net.hashsploit.clank.server.medius.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusUpdateLadderStatsWideHandler extends MediusPacketHandler {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	private byte[] ladderType = new byte[4];
	private byte[] stats = new byte[MediusConstants.LADDERSTATSWIDE_MAXLEN.value];
	
	public MediusUpdateLadderStatsWideHandler() {
		super(MediusMessageType.UpdateLadderStatsWide,MediusMessageType.UpdateLadderStatsWideResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		// Process the packet
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(ladderType);
		buf.get(stats);
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		byte[] statsResponse = Utils.buildByteArrayFromString("0000000", 7); // empty 7 byte array
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(statsResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(new MediusMessage(responseType, outputStream.toByteArray()));
		return response;
	}

}
