package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusGameInfoZero extends MediusPacket {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] worldID = new byte[4];
	
	public MediusGameInfoZero() {
		super(MediusPacketType.GameInfo0, MediusPacketType.GameInfoResponse0);
	}
	
	@Override
	public void read(MediusMessage mm) {
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(sessionKey);
		buf.getShort(); // buffer
		buf.get(worldID);
		
		logger.finest("MediusGameInfoZero data read:");
		logger.finest("Message ID : " + Utils.bytesToHex(messageID) + " | Length: " + Integer.toString(messageID.length));
		logger.finest("Session Key: " + Utils.bytesToHex(sessionKey) + " | Length: " + Integer.toString(sessionKey.length));
		logger.finest("WorldID: " + Utils.bytesToHex(worldID));
	}
	
	@Override
	public MediusMessage write(MediusClient client) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.MediusSuccess.getValue()));
		
		logger.finest("Writing GameInfo0 OUT:");
		logger.finest("WorldID : " + Utils.bytesToHex(worldID));
		
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000")); // Padding
			outputStream.write(callbackStatus);
			outputStream.write(Utils.hexStringToByteArray("bc290000")); // app id
			outputStream.write(Utils.hexStringToByteArray("00000000")); // minplayers
			outputStream.write(Utils.hexStringToByteArray("08000000")); // maxplayers
			//outputStream.write(worldID); //gamelevel
			outputStream.write(Utils.hexStringToByteArray("45070000")); // gameLevel (aquatos channel id)
			outputStream.write(Utils.hexStringToByteArray("00000000")); // playerSkillLevel -- SETTING THIS TO 01000000 TURNS FLUX OFF
			outputStream.write(Utils.hexStringToByteArray("01000000")); // playerCount
			outputStream.write(Utils.buildByteArrayFromString("", MediusConstants.GAMESTATS_MAXLEN.getValue()));
			outputStream.write(Utils.buildByteArrayFromString("Smily's", MediusConstants.GAMENAME_MAXLEN.getValue()));
			outputStream.write(Utils.hexStringToByteArray("00000000")); // rulesset -- weapons
			outputStream.write(Utils.hexStringToByteArray("28000000")); // genericField1 -- cities location ID
			outputStream.write(Utils.hexStringToByteArray("01000000")); // genericField2
			outputStream.write(Utils.hexStringToByteArray("0800e411")); // genericField3	
			
			outputStream.write(Utils.hexStringToByteArray("01000000")); // worldStatus (staging) // 0 to 5
			outputStream.write(Utils.hexStringToByteArray("04000000")); // gameHostType // 0 to 4
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return new MediusMessage(responseType, outputStream.toByteArray());
	}


}
