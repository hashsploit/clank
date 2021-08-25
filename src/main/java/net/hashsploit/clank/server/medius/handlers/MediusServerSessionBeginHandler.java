package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.NetAddress;
import net.hashsploit.clank.server.medius.objects.NetAddressList;
import net.hashsploit.clank.server.medius.objects.NetAddressType;
import net.hashsploit.clank.server.medius.objects.NetConnectionInfo;
import net.hashsploit.clank.server.medius.objects.NetConnectionType;
import net.hashsploit.clank.server.medius.serializers.ServerSessionBeginRequest;
import net.hashsploit.clank.server.medius.serializers.ServerSessionBeginResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusServerSessionBeginHandler extends MediusPacketHandler {

	private ServerSessionBeginRequest reqPacket;
	private ServerSessionBeginResponse respPacket;

	public MediusServerSessionBeginHandler() {
		super(MediusMessageType.MediusServerSessionBeginRequest, MediusMessageType.MediusServerSessionBeginResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new ServerSessionBeginRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		byte confirmation = 0x00;

		String natAddress = ((MasConfig) Clank.getInstance().getConfig()).getNatConfig().getAddress();
		final int natPort = ((MasConfig) Clank.getInstance().getConfig()).getNatConfig().getPort();
		String mlsAddress = ((MasConfig) Clank.getInstance().getConfig()).getMlsConfig().getAddress();
		final int mlsPort = ((MasConfig) Clank.getInstance().getConfig()).getMlsConfig().getPort();
		
		if (natAddress == null) {
			natAddress = Utils.getPublicIpAddress();
		}
		
		if (mlsAddress == null) {
			mlsAddress = Utils.getPublicIpAddress();
		}
		
		int worldId = 0;
		
		// FIXME: which channel?
		/*
		if (client.getServer().getEmulationMode() == EmulationMode.MEDIUS_AUTHENTICATION_SERVER) {
			worldId = ((MediusAuthenticationServer) client.getServer()).getChannel().getId();
		} else if (client.getServer().getEmulationMode() == EmulationMode.MEDIUS_LOBBY_SERVER) {
			worldId = ((MediusLobbyServer) client.getServer()).getChannel().getId();
		}
		*/

		byte[] serverKey = new byte[64];
		byte[] sessionKey = Utils.buildByteArrayFromString("123456789", MediusConstants.SESSIONKEY_MAXLEN.value);
		byte[] accessKey = new byte[MediusConstants.ACCESSKEY_MAXLEN.value];

		// FIXME: hardcoded values
		byte[] connectInfo = new NetConnectionInfo(
				//NetConnectionType.NET_CONNECTION_TYPE_PEER_TO_PEER_UDP,
				NetConnectionType.NET_CONNECTION_TYPE_CLIENT_SERVER_TCP_AUX_UDP,
				new NetAddressList(
						new NetAddress(NetAddressType.NET_ADDRESS_NAT_SERVICE, natAddress, natPort),
						new NetAddress(NetAddressType.NET_ADDRESS_TYPE_EXTERNAL, mlsAddress, mlsPort)
				),
				worldId, serverKey, sessionKey, accessKey
		).serialize();

		respPacket = new ServerSessionBeginResponse(reqPacket.getMessageId(), confirmation, connectInfo);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}

}
