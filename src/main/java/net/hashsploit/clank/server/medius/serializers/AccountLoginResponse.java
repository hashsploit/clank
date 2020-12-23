package net.hashsploit.clank.server.medius.serializers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

public class AccountLoginResponse extends MediusMessage {

	private byte[] messageID;
	private byte[] callbackStatus;
	private byte[] accountID;
	private byte[] accountType;
	private byte[] worldID;
	private byte[] mlsAddress;
	private byte[] mlsZeroTrail;
	private byte[] natAddress;
	private byte[] natZeroTrail;
	private byte[] mlsToken;
	
	public AccountLoginResponse(byte[] messageID, byte[] callbackStatus, byte[] accountID, byte[] accountType,
			byte[] worldID, byte[] mlsAddress, byte[] mlsZeroTrail, byte[]natAddress, byte[] natZeroTrail, byte[] mlsToken) {
		super(MediusMessageType.AccountLoginResponse);
		
		this.messageID = messageID;
		this.callbackStatus = callbackStatus;
		this.accountID = accountID;
		this.accountType = accountType;
		this.worldID = worldID;
		this.mlsAddress = mlsAddress;
		this.mlsZeroTrail = mlsZeroTrail;
		this.natAddress = natAddress;
		this.natZeroTrail = natZeroTrail;
		this.mlsToken = mlsToken;
	}
	
	@Override
	public byte[] getPayload() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
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
			outputStream.write(Utils.hexStringToByteArray("56270000")); // NAT Port + padding
			outputStream.write(Utils.hexStringToByteArray("00000000")); // World ID to join. Default is zero when you login
			outputStream.write(Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"));
			
			// TODO: These last two byte arrays might be part of the smae thing, a session key perhaps.
			outputStream.write(Utils.hexStringToByteArray("3133")); // ???
			outputStream.write(Utils.hexStringToByteArray("000000000000000000000000000000")); // Padding 
			outputStream.write(mlsToken); // MLS Acess Token
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
	
	public String toString() {
		return "AccountLoginResponse: \n" + 
				"messageID: " + Utils.bytesToHex(messageID) + '\n' + 
				"callbackStatus: " + Utils.bytesToHex(callbackStatus) + '\n' + 
				"accountID: " + Utils.bytesToHex(accountID) + '\n' + 
				"worldID: " + Utils.bytesToHex(worldID) + '\n' + 
				"mlsAddress: " + Utils.bytesToHex(mlsAddress) + '\n' + 
				"accountType: " + Utils.bytesToHex(accountType) + '\n' + 
				"natAddress: " + Utils.bytesToHex(natAddress) + '\n' + 

				"mlsToken: " + Utils.bytesToHex(mlsToken);
	}

	
	
}
