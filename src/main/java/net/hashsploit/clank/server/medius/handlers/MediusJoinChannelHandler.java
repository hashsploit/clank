package net.hashsploit.clank.server.medius.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.medius.objects.NetAddressType;
import net.hashsploit.clank.server.medius.objects.NetConnectionType;
import net.hashsploit.clank.utils.Utils;

public class MediusJoinChannelHandler extends MediusPacketHandler {

	byte[] messageId = new byte[MediusConstants.MESSAGEID_MAXLEN.value];
	byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.value];
	byte[] buffer = new byte[2];
	byte[] worldId = new byte[4];
	byte[] lobbyChannelPassword = new byte[MediusConstants.LOBBYPASSWORD_MAXLEN.value];

	public MediusJoinChannelHandler() {
		super(MediusMessageType.JoinChannel, MediusMessageType.JoinChannelResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());

		buf.get(messageId);
		buf.get(sessionKey);
		buf.get(buffer);
		buf.get(worldId);
		buf.get(lobbyChannelPassword);
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		// RESPONSE
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();

		// Update player status to joined channel
		client.getPlayer().setChatWorld(Utils.bytesToIntLittle(worldId));
		server.updatePlayerStatus(client.getPlayer(), MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD);
		
		logger.finest("Join channel world id to join: " + Utils.bytesToHex(worldId));


		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] callbackStatus = Utils.intToBytesLittle(0);
		String ipAddrStr = ((MediusConfig) Clank.getInstance().getConfig()).getAddress();
		short port = (short) ((MediusConfig) Clank.getInstance().getConfig()).getPort();

		if (ipAddrStr == null || ipAddrStr.isEmpty()) {
			ipAddrStr = Utils.getPublicIpAddress();
		}

		logger.finest("Joining addr: " + ipAddrStr);
		byte[] ipAddr = ipAddrStr.getBytes();
		int numZeros = 16 - ipAddrStr.length();
		String zeroString = new String(new char[numZeros]).replace("\0", "00");
		byte[] zeroTrail = Utils.hexStringToByteArray(zeroString);

		String mlsToken = Clank.getInstance().getDatabase().generateMlsToken(client.getPlayer().getAccountId());
		

		try {
			outputStream.write(messageId);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);

			outputStream.write(Utils.intToBytesLittle(NetConnectionType.NET_CONNECTION_TYPE_CLIENT_SERVER_TCP.getValue())); // net connection type (int/little endian)

			outputStream.write(Utils.intToBytesLittle(NetAddressType.NET_ADDRESS_TYPE_EXTERNAL.getValue())); // net address type (int/little endian)

			outputStream.write(ipAddr); // ip address
			outputStream.write(zeroTrail); // zero padding for ip address

			outputStream.write(Utils.shortToBytesLittle(port)); // port

			// ???
			outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000000000000000ffffffff"));

			// world id
			outputStream.write(worldId);

			// RSA_KEY 64
			outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")); // RSA_KEY 64

			outputStream.write(Utils.hexStringToByteArray("3333323837000000000000000000000000")); // aSessionKey

			// outputStream.write(Utils.hexStringToByteArray("782B6F2F532F71443453633243364B4E"));
			// aAccessKey
			outputStream.write(mlsToken.getBytes()); // aAccessKey
			outputStream.write(Utils.hexStringToByteArray("0000"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// WORKING
//		TRUE STRUCTURE:
//			310000000000000000000000000000000000000000
//			000000
//			00000000
//			01000000
//			01000000
//			3139322e3136382e302e393900000000
//			5e27
//			00000000000000000000000000000000000000000000ffffffff0b000000
//
//			00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000

//			3333323837000000000000000000000000782b6f2f532f71443453633243364b4e000000
		byte[] test = Utils.hexStringToByteArray(
				"3100000000000000000000000000000000000000000000000000000001000000010000003139322e3136382e302e3939000000005E2700000000000000000000000000000000000000000000FFFFFFFF0B000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003333323837000000000000000000000000782B6F2F532F71443453633243364B4E000000");
		// return new MediusMessage(responseType, test);
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(new MediusMessage(responseType, outputStream.toByteArray()));
		return response;
	}

}
