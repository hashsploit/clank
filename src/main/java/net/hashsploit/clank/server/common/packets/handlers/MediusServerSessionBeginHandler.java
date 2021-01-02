package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusAuthenticationServer;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusLobbyServer;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.NetAddress;
import net.hashsploit.clank.server.common.objects.NetAddressList;
import net.hashsploit.clank.server.common.objects.NetAddressType;
import net.hashsploit.clank.server.common.objects.NetConnectionInfo;
import net.hashsploit.clank.server.common.objects.NetConnectionType;
import net.hashsploit.clank.server.common.packets.serializers.ServerSessionBeginRequest;
import net.hashsploit.clank.server.common.packets.serializers.ServerSessionBeginResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusServerSessionBeginHandler extends MediusPacketHandler {

	private ServerSessionBeginRequest reqPacket;
	private ServerSessionBeginResponse respPacket;

	public MediusServerSessionBeginHandler() {
		super(MediusMessageType.MediusServerSessionBeginRequest, MediusMessageType.MediusServerSessionBeginResponse);
	}

	@Override
	public void read(MediusMessage mm) {
		reqPacket = new ServerSessionBeginRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public void write(MediusClient client) {

		byte confirmation = 0x00;

		String natAddress = ((MasConfig) Clank.getInstance().getConfig()).getNatAddress();
		final int natPort = ((MasConfig) Clank.getInstance().getConfig()).getNatPort();
		String mlsAddress = ((MasConfig) Clank.getInstance().getConfig()).getMlsAddress();
		final int mlsPort = ((MasConfig) Clank.getInstance().getConfig()).getMlsPort();
		
		if (natAddress == null) {
			natAddress = Utils.getPublicIpAddress();
		}
		
		if (mlsAddress == null) {
			mlsAddress = Utils.getPublicIpAddress();
		}
		
		
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		int worldId = 0;//server.getChannel().getId();
		
		if (client.getServer().getEmulationMode() == EmulationMode.MEDIUS_AUTHENTICATION_SERVER) {
			worldId = ((MediusAuthenticationServer) client.getServer()).getChannel().getId();
		} else if (client.getServer().getEmulationMode() == EmulationMode.MEDIUS_LOBBY_SERVER) {
			worldId = ((MediusLobbyServer) client.getServer()).getChannel().getId();
		}

		byte[] serverKey = new byte[64];
		byte[] sessionKey = Utils.buildByteArrayFromString("123456789", MediusConstants.SESSIONKEY_MAXLEN.getValue());
		byte[] accessKey = new byte[MediusConstants.ACCESSKEY_MAXLEN.getValue()];

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
		
		client.sendMediusMessage(respPacket);
	}

}
