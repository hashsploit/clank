package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.GameHostType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.NetAddress;
import net.hashsploit.clank.server.medius.objects.NetAddressList;
import net.hashsploit.clank.server.medius.objects.NetAddressType;
import net.hashsploit.clank.server.medius.objects.NetConnectionInfo;
import net.hashsploit.clank.server.medius.objects.NetConnectionType;
import net.hashsploit.clank.utils.Utils;

public class MediusJoinGame extends MediusPacket {

	byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	byte[] worldIdToJoin = new byte[4];
	byte[] joinType = new byte[4];
	byte[] gamePassword = new byte[MediusConstants.GAMEPASSWORD_MAXLEN.getValue()];
	byte[] gameHostType = new byte[4];
	byte[] RSApubKey = new byte[10];

	public MediusJoinGame() {
		super(MediusPacketType.JoinGame, MediusPacketType.JoinGameResponse);

	}

	public void read(MediusMessage mm) {
		// Process the packet
		logger.fine("Get my clans: " + Utils.bytesToHex(mm.getPayload()));

		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(sessionKey);
		buf.get(new byte[2]);// padding
		buf.get(worldIdToJoin);
		buf.get(joinType);
		buf.get(gamePassword);
		buf.get(gameHostType);
		logger.fine("Message ID : " + Utils.bytesToHex(messageID));
		logger.fine("worldIdToJoin: " + Utils.bytesToHex(worldIdToJoin));
		logger.fine("joinType: " + Utils.bytesToHex(joinType));
		logger.fine("gamePassword: " + Utils.bytesToHex(gamePassword));
		logger.fine("gameHostType: " + Utils.bytesToHex(gameHostType));
		logger.fine("Data: " + Utils.bytesToHex(mm.getPayload()));
	}

	@Override
	public MediusMessage write(MediusClient client) {
		// RESPONSE

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding

			final MediusCallbackStatus callbackStatus = MediusCallbackStatus.MediusSuccess;
			outputStream.write(Utils.intToBytesLittle(callbackStatus.getValue()));
			
			final GameHostType gameHostType = GameHostType.HOST_CLIENT_SERVER_AUX_UDP;
			outputStream.write(Utils.intToBytesLittle(gameHostType.getValue()));
			
			final NetAddress firstNetAddress = new NetAddress(NetAddressType.NET_ADDRESS_TYPE_EXTERNAL, Clank.getInstance().getConfig().getAddress(), 10079);
			//final NetAddress secondNetAddress = new NetAddress(NetAddressType.NET_ADDRESS_NAT_SERVICE, Clank.getInstance().getConfig().getAddress(), 10080); // SET TO MLS.CONF/MLS.JSON NAT ADDR
			final NetAddress secondNetAddress = new NetAddress(NetAddressType.NET_ADDRESS_NONE, "", 0);
			
			final NetAddressList netAddressList = new NetAddressList(firstNetAddress, secondNetAddress);
			
			final byte[] rsaKey = new byte[64]; // 00's
			
			final NetConnectionInfo netConnectionInfo = new NetConnectionInfo(
				NetConnectionType.NET_CONNECTION_TYPE_CLIENT_SERVER_TCP_AUX_UDP,
				netAddressList,
				Utils.bytesToIntLittle(worldIdToJoin),
				rsaKey,
				Utils.buildByteArrayFromString("", 17),
				Utils.buildByteArrayFromString("", 17)
			);
			
			outputStream.write(netConnectionInfo.serialize());
			
			// outputStream.write(Utils.hexStringToByteArray("03000000")); //// gamehosttype
			// outputStream.write(Utils.hexStringToByteArray("01000000")); // net connection
			// type (int/little endian)
			// outputStream.write(Utils.hexStringToByteArray("01000000")); // net address
			// type (int/little endian)
			// outputStream.write(ipAddr); // ip address
			// outputStream.write(zeroTrail); // zero padding for ip address
			// outputStream.write(Utils.intToBytesLittle(10079)); // port
			// outputStream.write(worldIdToJoin); // world id
			// outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000FFFFFFFF0B000000")); // 24 bytes ???
			// // invalid rsa key ???
			//// outputStream.write(Utils.hexStringToByteArray("CF09ADE3EA5551113BBB519AC5BCB0CB45CD22DD399257E74E886684A12A3E68A865F487CE86777545D6CBFD90C2C6186F7D05E82419DB2E2230E7F73CCF8BB33333323837"));
			// // RSA_KEY 64
			// outputStream.write(Utils.hexStringToByteArray("000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));
			// // RSA_KEY 64
			// outputStream.write(Utils.hexStringToByteArray("0000000000000000000000000000000000")); // aSessionKey
			// outputStream.write(Utils.hexStringToByteArray("782B6F2F532F71443453633243364B4E00")); // aAccessKey

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new MediusMessage(responseType, outputStream.toByteArray());

	}

}
