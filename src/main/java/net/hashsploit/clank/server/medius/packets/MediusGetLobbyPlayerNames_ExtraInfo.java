package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPlayerOnlineState;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.utils.Utils;

public class MediusGetLobbyPlayerNames_ExtraInfo extends MediusPacket {

	private static final Logger logger = Logger.getLogger("");

	public MediusGetLobbyPlayerNames_ExtraInfo() {
		super(MediusPacketType.GetLobbyPlayerNames_ExtraInfo);
	}

	@Override
	public void process(Client client, ChannelHandlerContext ctx, byte[] packetData) {

		ByteBuffer buf = ByteBuffer.wrap(packetData);

		byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
		byte[] empty = new byte[3];
		byte[] worldId = new byte[4];

		buf.get(messageID);
		buf.get(empty);
		buf.get(worldId);

		// RESPONSE

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] callbackStatus = Utils.intToBytesLittle(0);

		try {
			outputStream.write(MediusPacketType.GetLobbyPlayerNames_ExtraInfoResponse.getShortByte());
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);
			
			// LIST OF ACCOUNTS
			
			// Account ID
			outputStream.write(Utils.intToBytesLittle(101));
			
			// Account name (32)
			outputStream.write(Utils.buildByteArrayFromString("Smily", MediusConstants.ACCOUNTNAME_MAXLEN.getValue()));
			
			// Online State
			//MediusPlayerOnlineState onlineState = new MediusPlayerOnlineState(MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD, 0, 0, "Aquatos v2", "Aquatos v2");
			MediusPlayerOnlineState onlineState = new MediusPlayerOnlineState(MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD, 0, 0, "Aquatos v2", "Aquatos v2");
			
				// connect state
				outputStream.write(Utils.intToBytesLittle(onlineState.getConnectionStatus().getValue()));
				// lobby world id
				outputStream.write(worldId);
				// game world id
				outputStream.write(Utils.intToBytesLittle(0));
				// lobby name
				outputStream.write(Utils.buildByteArrayFromString(onlineState.getLobbyName(), MediusConstants.WORLDNAME_MAXLEN.getValue()));
				// game name
				outputStream.write(Utils.buildByteArrayFromString("", MediusConstants.WORLDNAME_MAXLEN.getValue()));
			
			// End of list
			outputStream.write(Utils.hexStringToByteArray("01000000"));
			//outputStream.write(Utils.hexStringToByteArray("00000000A31913004D6173717561727200000000000000000000000000000000000000000000000002000000750B010000000000435930303030303030342D30300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000000"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Combine RT id and len
		byte[] data = outputStream.toByteArray();
		logger.fine("LENGTH: " + Integer.toString(data.length));
		DataPacket packet = new DataPacket(RTPacketId.SERVER_APP, data);

		byte[] finalPayload = packet.toData().array();
		logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
		ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
		ctx.write(msg);
		ctx.flush();

	}

}