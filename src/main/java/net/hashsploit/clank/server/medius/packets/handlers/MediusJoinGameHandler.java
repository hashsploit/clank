package net.hashsploit.clank.server.medius.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.objects.GameHostType;
import net.hashsploit.clank.server.medius.objects.NetAddress;
import net.hashsploit.clank.server.medius.objects.NetAddressList;
import net.hashsploit.clank.server.medius.objects.NetAddressType;
import net.hashsploit.clank.server.medius.objects.NetConnectionInfo;
import net.hashsploit.clank.server.medius.objects.NetConnectionType;
import net.hashsploit.clank.server.medius.packets.serializers.ChannelInfoResponse;
import net.hashsploit.clank.server.medius.packets.serializers.ChatMessageRequest;
import net.hashsploit.clank.server.medius.packets.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.medius.packets.serializers.CreateGameResponse;
import net.hashsploit.clank.server.medius.packets.serializers.JoinGameRequest;
import net.hashsploit.clank.server.medius.packets.serializers.JoinGameResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusJoinGameHandler extends MediusPacketHandler {

	private JoinGameRequest reqPacket;
	private JoinGameResponse respPacket;
	
	public MediusJoinGameHandler() {
		super(MediusPacketType.JoinGame, MediusPacketType.JoinGameResponse);
	}

	public void read(MediusPacket mm) {
		reqPacket = new JoinGameRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public MediusPacket write(MediusClient client) {
		// RESPONSE

		final MediusCallbackStatus callbackStatus = MediusCallbackStatus.MediusSuccess;

		final GameHostType gameHostType = GameHostType.HOST_CLIENT_SERVER_AUX_UDP;
		
		final NetAddress firstNetAddress = new NetAddress(NetAddressType.NET_ADDRESS_TYPE_EXTERNAL, Clank.getInstance().getConfig().getAddress(), 10079);
		//final NetAddress secondNetAddress = new NetAddress(NetAddressType.NET_ADDRESS_NAT_SERVICE, Clank.getInstance().getConfig().getAddress(), 10080); // SET TO MLS.CONF/MLS.JSON NAT ADDR
		final NetAddress secondNetAddress = new NetAddress(NetAddressType.NET_ADDRESS_NONE, "", 0);
		
		final NetAddressList netAddressList = new NetAddressList(firstNetAddress, secondNetAddress);
		
		byte[] rsaKey = new byte[64]; // 00's
		
		byte[] sessionKey = new byte[17];
		//sessionKey = Utils.hexStringToByteArray("4242424242424242424242424242424242");
		
		byte[] accessKey = new byte[17];
		//accessKey = Utils.hexStringToByteArray("782B6F2F532F71443453633243364B4E00");
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
		
		return respPacket;
	}

}
