package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.LadderPosition_ExtraInfoRequest;
import net.hashsploit.clank.server.medius.serializers.LadderPosition_ExtraInfoResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusLadderPosition_ExtraInfoHandler extends MediusPacketHandler {
	
	private LadderPosition_ExtraInfoRequest reqPacket;
	private LadderPosition_ExtraInfoResponse respPacket;
		
	public MediusLadderPosition_ExtraInfoHandler() {
		super(MediusMessageType.LadderPosition_ExtraInfo, MediusMessageType.LadderPosition_ExtraInfoResponse);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage message) {
		reqPacket = new LadderPosition_ExtraInfoRequest(message.getPayload());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
		byte[] ladderPosition = Utils.intToBytesLittle(1);
		byte[] totalRankings = Utils.intToBytesLittle(10);
		
		respPacket = new LadderPosition_ExtraInfoResponse(reqPacket.getMessageID(), callbackStatus, ladderPosition, totalRankings);
		
		List<MediusMessage> responses = new ArrayList<MediusMessage>();
		responses.add(respPacket);
		return responses;
	}
	
}
