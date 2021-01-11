package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MuisConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.BillingServiceProvider;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.GetUniverseInformationRequest;
import net.hashsploit.clank.server.medius.serializers.GetUniverseInformationResponse;
import net.hashsploit.clank.server.muis.UniverseInfo;
import net.hashsploit.clank.utils.Utils;

public class MediusGetUniverseInformationHandler extends MediusPacketHandler {

	private GetUniverseInformationRequest reqPacket;
	private GetUniverseInformationResponse respPacket;

	public MediusGetUniverseInformationHandler() {
		super(MediusMessageType.GetUniverseInformation, MediusMessageType.UniverseVariableInformationResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new GetUniverseInformationRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		byte[] messageId = reqPacket.getMessageId();
		
		// This is OK to cast without checking because this handler is only ever used in the MUIS.
		MuisConfig config = (MuisConfig) Clank.getInstance().getConfig();
		
		List<MediusMessage> responses = new ArrayList<MediusMessage>();
		
		// FIXME: A lot of hard-coded values.
		for (int i=0; i<config.getUniverseInformation().size(); i++) {
			final UniverseInfo universe = config.getUniverseInformation().get(i);
			
			MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;
			
			/*
			 * InfoFilter is a bitmask.
			 * 
			 * Bit 1:   Always Clear
			 * Bit 2:   Always Clear
			 * Bit 3:   Set If the UniverseID field exists
			 * Bit 4:   Set If the UniverseName field exists
			 * Bit 5:   Set If the DNS & Port fields exist
			 * Bit 6:   Set If the UniverseDescription field exists
			 * Bit 7:   Set If the Status & UserCount & MaxUser fields exist
			 * Bit 8:   Set If the UniverseBilling & BillingSystemName fields exist
			 * Bit 9:   Set If the ExtendedInfo field exists
			 * Bit 10:  Set If the SvoURL field exists  
			 */
			int infoFilter = Utils.bytesToIntLittle(reqPacket.getInfoType());
			
			int universeId = universe.getUniverseId();
			String universeName = universe.getName();
			String dns = universe.getHostname();
			int port = universe.getPort();
			String universeDescription = universe.getDescription();
			
			int status = 1; // Universe up/down status field
			int userCount = 1; // Number of users in this universe
			int maxUsers = 1000; // Maximum number of users allowed in this universe
			
			BillingServiceProvider bsp = BillingServiceProvider.SCEE;
			String billingSystemName = "Nah, this shit is free";
			String extendedInfo = "";
			String svoUrl = "";
			
			boolean endOfList = i == config.getUniverseInformation().size() - 1;
			
			respPacket = new GetUniverseInformationResponse(messageId, callbackStatus, infoFilter, universeId, universeName, dns, port, universeDescription, status, userCount, maxUsers, bsp, billingSystemName, extendedInfo, svoUrl, endOfList);
			responses.add(respPacket);
		}
		
		
		return responses;
	}
}
