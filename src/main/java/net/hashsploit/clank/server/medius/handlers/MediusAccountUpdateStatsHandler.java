package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.AccountUpdateStatsRequest;
import net.hashsploit.clank.server.medius.serializers.AccountUpdateStatsResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountUpdateStatsHandler extends MediusPacketHandler {
	
	private AccountUpdateStatsRequest reqPacket;
	private AccountUpdateStatsResponse respPacket;

	
	public MediusAccountUpdateStatsHandler() {
		super(MediusMessageType.AccountUpdateStats, MediusMessageType.AccountUpdateStatsResponse);
	}
	
	@Override 
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new AccountUpdateStatsRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<MediusMessage> write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytesLittle(0);

		respPacket = new AccountUpdateStatsResponse(reqPacket.getMessageID(), callbackStatus);
				
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		Clank.getInstance().getDatabase().updatePlayerStats(client.getPlayer().getAccountId(), Utils.bytesToHex(reqPacket.getStats()));
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}
	
}
