package net.hashsploit.clank.server.medius.packets.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.medius.packets.serializers.AccountLoginRequest;
import net.hashsploit.clank.server.medius.packets.serializers.AccountLoginResponse;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.medius.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountLoginHandler extends MediusPacketHandler {

	private AccountLoginRequest reqPacket;
	private AccountLoginResponse respPacket;
	
	public MediusAccountLoginHandler() {
		super(MediusPacketType.AccountLogin, MediusPacketType.AccountLoginResponse);
	}
	
	@Override
	public void read(MediusPacket mm) {
		reqPacket = new AccountLoginRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public MediusPacket write(MediusClient client) {
		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.MediusLoginFailed.getValue()));
		byte[] mlsToken = Utils.hexStringToByteArray("00000000000000000000000000000000000000");
		
		// TODO: handle logic elsewhere (controller/model design)
		final String username = Utils.bytesToStringClean(reqPacket.getUsernameBytes());
		final String password = Utils.bytesToStringClean(reqPacket.getPasswordBytes());
		
		if (Clank.getInstance().getDatabase().accountExists(username)) {
			if (Clank.getInstance().getDatabase().validateAccount(username, password)) {
				callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.MediusSuccess.getValue()));
				
				// TODO: Generate random auth token each connection, save in db for MLS to use.
				// db should have a expiration UNIX time-stamp as well that needs to be updated by MLS. 
				new Random().nextBytes(mlsToken);
				mlsToken[mlsToken.length-1] = (byte) 0x00;
				mlsToken[mlsToken.length-2] = (byte) 0x00;
				mlsToken[mlsToken.length-3] = (byte) 0x00;
				
			} else {
				callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.MediusInvalidPassword.getValue()));
			}
		}
		
		byte[] accountID = Utils.intToBytesLittle(50);
		byte[] accountType = Utils.intToBytesLittle(1);
		byte[] worldID = Utils.intToBytesLittle(123);
				
		byte[] mlsAddress = Clank.getInstance().getConfig().getProperties().get("MlsIpAddress").toString().getBytes();
		int mlsNumZeros = 16 - Clank.getInstance().getConfig().getProperties().get("MlsIpAddress").toString().length();
		String mlsZeroString = new String(new char[mlsNumZeros]).replace("\0", "00");
		byte[] mlsZeroTrail = Utils.hexStringToByteArray(mlsZeroString);
		
		byte[] natAddress = Clank.getInstance().getConfig().getProperties().get("NatIpAddress").toString().getBytes();
		int natNumZeros = 16 - Clank.getInstance().getConfig().getProperties().get("NatIpAddress").toString().length();
		String natZeroString = new String(new char[natNumZeros]).replace("\0", "00");
		byte[] natZeroTrail = Utils.hexStringToByteArray(natZeroString);
		
		respPacket = new AccountLoginResponse(reqPacket.getMessageID(), callbackStatus, accountID, accountType, worldID, mlsAddress, mlsZeroTrail, natAddress, natZeroTrail,
						mlsToken);

		return respPacket;
	}


}
