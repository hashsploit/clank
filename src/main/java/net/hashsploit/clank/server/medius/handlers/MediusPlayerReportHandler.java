package net.hashsploit.clank.server.medius.handlers;

import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.PlayerReportRequest;

public class MediusPlayerReportHandler extends MediusPacketHandler {
	
	private PlayerReportRequest reqPacket;
	
    public MediusPlayerReportHandler() {
        super(MediusMessageType.PlayerReport, null);
    }
    
    @Override
    public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new PlayerReportRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
    }
    
    @Override
    public List<MediusMessage> write(MediusClient client) {
		return null;
    }

}
