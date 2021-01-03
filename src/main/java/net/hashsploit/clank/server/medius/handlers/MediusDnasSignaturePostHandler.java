package net.hashsploit.clank.server.medius.handlers;

import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.DnasSignaturePostRequest;

public class MediusDnasSignaturePostHandler extends MediusPacketHandler {

	private DnasSignaturePostRequest reqPacket;
	
	public MediusDnasSignaturePostHandler() {
		super(MediusMessageType.DnasSignaturePost, null);
	}
	
	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new DnasSignaturePostRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		return null;
	}


}
