package net.hashsploit.clank.server.medius.objects;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.hashsploit.clank.utils.Utils;

public class NetConnectionInfo {
	
	private final NetConnectionType netConnectionType; // 4 bytes
	private final NetAddressList addressList; // 24 + 24 = 48 bytes
	private final int worldId; // 4 bytes
	private final byte[] serverKey; // 64 bytes (RSA KEY)
	private final byte[] sessionKey; // 17 bytes
	private final byte[] accessKey; // 17 bytes
	
	public NetConnectionInfo(NetConnectionType type, NetAddressList addressList, int worldId, byte[] serverKey, byte[] sessionKey, byte[] accessKey) {
		this.netConnectionType = type;
		this.addressList = addressList;
		this.worldId = worldId;
		this.serverKey = serverKey;
		this.sessionKey = sessionKey;
		this.accessKey = accessKey;
	}
	
	public String toString() {
		return "NetConnectionInfo: \n" + 
				"netConnectionType: " + Utils.intToBytesLittle(netConnectionType.getValue()) + '\n' + 
				"addressList.getFirst() : " + Utils.bytesToHex(addressList.getFirst().serialize()) + '\n' + 
				"addressList.getSecond(): " + Utils.bytesToHex(addressList.getSecond().serialize()) + '\n' +
				"worldId: " + Utils.bytesToHex(Utils.intToBytesLittle(worldId)) + '\n' + 
				"serverKey: " + Utils.bytesToHex(serverKey) + '\n' + 
				"sessionKey: " + Utils.bytesToHex(sessionKey) + '\n' + 
				"accessKey: " + Utils.bytesToHex(accessKey);
	}
	
	/**
	 * Get the Net Connection Type.
	 * @return
	 */
	public NetConnectionType getConnectionType() {
		return netConnectionType;
	}
	
	/**
	 * Get the Address List.
	 * @return
	 */
	public NetAddressList getAddressList() {
		return addressList;
	}
	
	/**
	 * Get the World Id.
	 * @return
	 */
	public int getWorldId() {
		return worldId;
	}
	
	/**
	 * Get the server RSA key.
	 * @return
	 */
	public byte[] getServerKey() {
		return serverKey;
	}
	
	/**
	 * Get the session key.
	 * @return
	 */
	public byte[] getSessionKey() {
		return sessionKey;
	}
	
	/**
	 * Get the key returned from 
	 * @return
	 */
	public byte[] getAccessKey() {
		return accessKey;
	}
	
	
	/**
	 * Serialize this object to be sent.
	 * @return
	 */
	public byte[] serialize() {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try {
			// NetConnectionType (4 bytes)
			outputStream.write(Utils.intToBytesLittle(netConnectionType.getValue()));
			
			// NetAddressList[0] (24 bytes)
			outputStream.write(addressList.getFirst().serialize());
			
			// NetAddressList[1] (24 bytes)
			outputStream.write(addressList.getSecond().serialize());
			
			// WorldId (4 bytes)
			outputStream.write(Utils.intToBytesLittle(worldId));
			
			// RSA Key / Server Key (64 bytes)
			outputStream.write(serverKey);
			
			// Session Key (17 bytes)
			outputStream.write(sessionKey);
			
			// Access Key (17 bytes)
			outputStream.write(accessKey);
			
			// 2 bytes of padding to be 4-byte aligned
			outputStream.write(Utils.hexStringToByteArray("0000"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}

}
