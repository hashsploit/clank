package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusServer;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.NetAddress;
import net.hashsploit.clank.server.common.objects.NetAddressList;
import net.hashsploit.clank.server.common.objects.NetAddressType;
import net.hashsploit.clank.server.common.objects.NetConnectionInfo;
import net.hashsploit.clank.server.common.objects.NetConnectionType;
import net.hashsploit.clank.server.common.packets.serializers.ServerAuthenticationRequest;
import net.hashsploit.clank.server.common.packets.serializers.ServerAuthenticationResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusServerAuthenticationHandler extends MediusPacketHandler {

	private ServerAuthenticationRequest reqPacket;
	private ServerAuthenticationResponse respPacket;

	public MediusServerAuthenticationHandler() {
		super(MediusMessageType.MediusServerAuthenticationRequest, MediusMessageType.MediusServerAuthenticationResponse);
	}

	@Override
	public void read(MediusMessage mm) {
		reqPacket = new ServerAuthenticationRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public void write(MediusClient client) {

		byte confirmation = 0x00; // 0x00 = success, 0x01 = failure

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

		// FIXME: fucking bad
		int worldId = ((MediusServer) Clank.getInstance().getServer()).getLogicHandler().getChannel().getId();

		byte[] serverKey = new byte[64];
		byte[] sessionKey = Utils.buildByteArrayFromString("123456789", MediusConstants.SESSIONKEY_MAXLEN.getValue());
		byte[] accessKey = new byte[MediusConstants.ACCESSKEY_MAXLEN.getValue()];

		byte[] connectInfo = new NetConnectionInfo(
				NetConnectionType.NET_CONNECTION_TYPE_CLIENT_SERVER_TCP_AUX_UDP,
				new NetAddressList(
						new NetAddress(NetAddressType.NET_ADDRESS_NAT_SERVICE, natAddress, natPort),
						new NetAddress(NetAddressType.NET_ADDRESS_TYPE_EXTERNAL, mlsAddress, mlsPort)
				),
				worldId, serverKey, sessionKey, accessKey
		).serialize();

		respPacket = new ServerAuthenticationResponse(reqPacket.getMessageId(), confirmation, connectInfo);
		
		client.sendMediusMessage(respPacket);
	}

}
