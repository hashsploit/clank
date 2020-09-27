package net.hashsploit.clank.server.medius.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountLogin extends MediusPacket {

	private byte[] messageID = new byte[MediusConstants.MESSAGEID_MAXLEN.getValue()];
	private byte[] sessionKey = new byte[MediusConstants.SESSIONKEY_MAXLEN.getValue()];
	private byte[] usernameBytes = new byte[14];
	private byte[] passwordBytes = new byte[14];
	
	public MediusAccountLogin() {
		super(MediusPacketType.AccountLogin, MediusPacketType.AccountLoginResponse);
	}
	
	@Override
	public void read(MediusMessage mm) {
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(messageID);
		buf.get(sessionKey);
		// TODO: Username most likely is 32-bytes (14+18).
		buf.get(usernameBytes);
		buf.get(new byte[18]);
		// TODO: Password is also most likely 32-bytes, probably has 18-bytes trailing.
		buf.get(passwordBytes);
		
		logger.finest(String.format("MediusAccountLogin username:'%s' password:'%s'", Utils.bytesToStringClean(usernameBytes), Utils.bytesToStringClean(passwordBytes)));
	}
	
	@Override
	public MediusMessage write(MediusClient client) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.MediusLoginFailed.getValue()));
		byte[] mlsToken = Utils.hexStringToByteArray("00000000000000000000000000000000000000");
		
		
		// TODO: handle logic elsewhere (controller/model design)
		final String username = Utils.bytesToStringClean(usernameBytes);
		final String password = Utils.bytesToStringClean(passwordBytes);
		
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
		
		try {
			outputStream.write(messageID);
			outputStream.write(Utils.hexStringToByteArray("000000"));
			outputStream.write(callbackStatus); 
			outputStream.write(accountID); 
			outputStream.write(accountType); 
			outputStream.write(worldID); 
			outputStream.write(Utils.intToBytesLittle(1));
			outputStream.write(Utils.intToBytesLittle(1));
			
			outputStream.write(mlsAddress); // MLS Server Addr TODO: replace with server IP
			outputStream.write(mlsZeroTrail); // Zero padding based on server IP size 
			outputStream.write(Utils.hexStringToByteArray("5e27000003000000")); // MLS Port + padding
			
			outputStream.write(natAddress); // NAT server Addr
			outputStream.write(natZeroTrail); // Padding for address
			outputStream.write(Utils.hexStringToByteArray("5627000005000000")); // NAT Port + padding
			outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));
			
			// TODO: These last two byte arrays might be part of the smae thing, a session key perhaps.
			outputStream.write(Utils.hexStringToByteArray("3133")); // World ID and Player ID
			outputStream.write(Utils.hexStringToByteArray("000000000000000000000000000000")); // Padding 
			outputStream.write(mlsToken); // MLS Acess Token
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return new MediusMessage(responseType, outputStream.toByteArray());
	}


}
