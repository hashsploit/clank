package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MlsConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.GameHostType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusWorldStatus;
import net.hashsploit.clank.server.medius.objects.NetAddress;
import net.hashsploit.clank.server.medius.objects.NetAddressList;
import net.hashsploit.clank.server.medius.objects.NetAddressType;
import net.hashsploit.clank.server.medius.objects.NetConnectionInfo;
import net.hashsploit.clank.server.medius.objects.NetConnectionType;
import net.hashsploit.clank.server.medius.serializers.JoinGameRequest;
import net.hashsploit.clank.server.medius.serializers.JoinGameResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusJoinGameHandler extends MediusPacketHandler {

	private JoinGameRequest reqPacket;
	private JoinGameResponse respPacket;
	
	public MediusJoinGameHandler() {
		super(MediusMessageType.JoinGame, MediusMessageType.JoinGameResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new JoinGameRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		// RESPONSE
		
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		MediusWorldStatus gameStatus = server.getGameStatus(Utils.bytesToIntLittle(reqPacket.getWorldIdToJoin()));
		
		MediusCallbackStatus callbackStatus;
		if (gameStatus == MediusWorldStatus.WORLD_CLOSED || gameStatus == MediusWorldStatus.WORLD_INACTIVE || gameStatus == MediusWorldStatus.WORLD_ACTIVE) {
			callbackStatus = MediusCallbackStatus.REQUEST_DENIED;
		} 
		else {
			callbackStatus = MediusCallbackStatus.SUCCESS;
		}

		final GameHostType gameHostType = GameHostType.HOST_CLIENT_SERVER_AUX_UDP;
		
		final NetAddress firstNetAddress = new NetAddress(NetAddressType.NET_ADDRESS_TYPE_EXTERNAL, ((MlsConfig) Clank.getInstance().getConfig()).getDmeAddress(), ((MlsConfig) Clank.getInstance().getConfig()).getDmePort());
		//final NetAddress secondNetAddress = new NetAddress(NetAddressType.NET_ADDRESS_NAT_SERVICE, Clank.getInstance().getConfig().getAddress(), 10080); // SET TO MLS.CONF/MLS.JSON NAT ADDR
		final NetAddress secondNetAddress = new NetAddress(NetAddressType.NET_ADDRESS_NONE, ((MlsConfig) Clank.getInstance().getConfig()).getNatAddress(), ((MlsConfig) Clank.getInstance().getConfig()).getDmePort());
		
		final NetAddressList netAddressList = new NetAddressList(firstNetAddress, secondNetAddress);
		
		byte[] rsaKey = new byte[64]; // 00's
		
		byte[] sessionKey = new byte[17];
		sessionKey = Utils.hexStringToByteArray("4242424242424242424242424242424242");
		
		byte[] accessKey = new byte[17];
		//accessKey = Utils.hexStringToByteArray(Clank.getInstance().getDatabase().getMlsToken(client.getPlayer().getAccountId()));
		accessKey = client.getPlayer().getSessionKey().getBytes();
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
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}

}
