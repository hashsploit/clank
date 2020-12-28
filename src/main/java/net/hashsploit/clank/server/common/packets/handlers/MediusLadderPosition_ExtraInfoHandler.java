package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.LadderPosition_ExtraInfoRequest;
import net.hashsploit.clank.server.common.packets.serializers.LadderPosition_ExtraInfoResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusLadderPosition_ExtraInfoHandler extends MediusPacketHandler {
	
	private LadderPosition_ExtraInfoRequest reqPacket;
	private LadderPosition_ExtraInfoResponse respPacket;
		
	public MediusLadderPosition_ExtraInfoHandler() {
		super(MediusMessageType.LadderPosition_ExtraInfo, MediusMessageType.LadderPosition_ExtraInfoResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		reqPacket = new LadderPosition_ExtraInfoRequest(mm.getPayload());
	}
	
	@Override
	public void write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
		byte[] ladderPosition = Utils.intToBytesLittle(1);
		byte[] totalRankings = Utils.intToBytesLittle(10);
		
		respPacket = new LadderPosition_ExtraInfoResponse(reqPacket.getMessageID(), callbackStatus, ladderPosition, totalRankings);
		
       	client.sendMediusMessage(respPacket);
	}
	
}
