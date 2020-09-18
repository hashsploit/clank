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
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusSetGameListFilterZero extends MediusPacket {

	private static final Logger logger = Logger.getLogger("");
	
	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] filterField = new byte[4];
	private byte[] mask = new byte[4];
	private byte[] comparisonOperator = new byte[4];
	private byte[] baselineValue = new byte[4];
	
	public MediusSetGameListFilterZero() {
		super(MediusPacketType.SetGameListFilter0, MediusPacketType.SetGameListFilterResponse0);
	}
	
	@Override
	public void read(MediusMessage mm) {
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());

		buf.get(messageID);
		buf.get(new byte[3]);
		buf.get(filterField);
		buf.get(mask);
		buf.get(comparisonOperator);
		buf.get(baselineValue);
		
		logger.finest("SetGameListFilter0 data read:");
		logger.finest("Message ID  : " + Utils.bytesToHex(messageID) + " | Length: " + Integer.toString(messageID.length));
		logger.finest("filterField : " + Utils.bytesToHex(filterField) + " | Length: " + Integer.toString(filterField.length));
		logger.finest("Mask        : " + Utils.bytesToHex(mask) + " | Length: " + Integer.toString(mask.length));
		logger.finest("ComparisonOp: " + Utils.bytesToHex(comparisonOperator) + " | Length: " + Integer.toString(comparisonOperator.length));
		logger.finest("BaselineVal : " + Utils.bytesToHex(baselineValue) + " | Length: " + Integer.toString(baselineValue.length));
	}
	
	@Override
	public MediusMessage write(Client client) {
		byte[] callbackStatus = Utils.intToBytes(MediusCallbackStatus.MediusSuccess.getValue());

		logger.finest("Writing SetGameListFilter OUT:");
		logger.finest("CallbackStatus : " + Utils.bytesToHex(callbackStatus) + " | Length: " + Integer.toString(callbackStatus.length));
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("B6FFBF"));
			outputStream.write(callbackStatus);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new MediusMessage(responseType, outputStream.toByteArray());
	}


}
