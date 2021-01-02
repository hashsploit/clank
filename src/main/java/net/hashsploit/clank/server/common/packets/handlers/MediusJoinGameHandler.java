package net.hashsploit.clank.server.common.packets.handlers;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MlsConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusGameHostType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.NetAddress;
import net.hashsploit.clank.server.common.objects.NetAddressList;
import net.hashsploit.clank.server.common.objects.NetAddressType;
import net.hashsploit.clank.server.common.objects.NetConnectionInfo;
import net.hashsploit.clank.server.common.objects.NetConnectionType;
import net.hashsploit.clank.server.common.packets.serializers.JoinGameRequest;
import net.hashsploit.clank.server.common.packets.serializers.JoinGameResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusJoinGameHandler extends MediusPacketHandler {

	private JoinGameRequest reqPacket;
	private JoinGameResponse respPacket;
	
	public MediusJoinGameHandler() {
		super(MediusMessageType.JoinGame, MediusMessageType.JoinGameResponse);
	}

	public void read(MediusMessage mm) {
		reqPacket = new JoinGameRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public void write(MediusClient client) {
		// RESPONSE

		final MediusCallbackStatus callbackStatus = MediusCallbackStatus.SUCCESS;

		final MediusGameHostType gameHostType = MediusGameHostType.HOST_CLIENT_SERVER_AUX_UDP;
		
		final NetAddress firstNetAddress = new NetAddress(NetAddressType.NET_ADDRESS_TYPE_EXTERNAL, ((MlsConfig) Clank.getInstance().getConfig()).getDmeAddress(), ((MlsConfig) Clank.getInstance().getConfig()).getDmePort());
		//final NetAddress secondNetAddress = new NetAddress(NetAddressType.NET_ADDRESS_NAT_SERVICE, Clank.getInstance().getConfig().getAddress(), 10080); // SET TO MLS.CONF/MLS.JSON NAT ADDR
		final NetAddress secondNetAddress = new NetAddress(NetAddressType.NET_ADDRESS_NONE, ((MlsConfig) Clank.getInstance().getConfig()).getNatAddress(), ((MlsConfig) Clank.getInstance().getConfig()).getDmePort());
		
		final NetAddressList netAddressList = new NetAddressList(firstNetAddress, secondNetAddress);
		
		byte[] rsaKey = new byte[64]; // 00's
		
		byte[] sessionKey = new byte[17];
		sessionKey = Utils.hexStringToByteArray("4242424242424242424242424242424242");
		
		byte[] accessKey = new byte[17];
		accessKey = Utils.hexStringToByteArray(Clank.getInstance().getDatabase().getMlsToken(client.getPlayer().getAccountId()));
		final NetConnectionInfo netConnectionInfo = new NetConnectionInfo(
				NetConnectionType.NET_CONNECTION_TYPE_CLIENT_SERVER_TCP_AUX_UDP,
				netAddressList,
				Utils.bytesToIntLittle(reqPacket.getWorldIdToJoin()),
				rsaKey,
				sessionKey,
				accessKey
			);
		
		respPacket = new JoinGameResponse(reqPacket.getMessageID(), Utils.intToBytesLittle(callbackStatus.getValue()), Utils.intToBytesLittle(gameHostType.getValue()),
				netConnectionInfo);
		
		client.getPlayer().setGameWorldId(Utils.bytesToIntLittle(reqPacket.getWorldIdToJoin()));
		client.sendMediusMessage(respPacket);
	}

}
