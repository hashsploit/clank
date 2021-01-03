package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.ClearGameListFilterResponse;
import net.hashsploit.clank.server.medius.serializers.ClearGameListFilterZeroRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusClearGameListFilterZeroHandler extends MediusPacketHandler {
	
	private ClearGameListFilterZeroRequest reqPacket;
	private ClearGameListFilterResponse respPacket;
	
	public MediusClearGameListFilterZeroHandler() {
		super(MediusMessageType.ClearGameListFilter0, MediusMessageType.ClearGameListFilterResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new ClearGameListFilterZeroRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		//byte[] callbackStatus = Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue());
		byte[] callbackStatus = Utils.hexStringToByteArray("2CFCFFFF");

		respPacket = new ClearGameListFilterResponse(reqPacket.getMessageID(), callbackStatus);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}


}
