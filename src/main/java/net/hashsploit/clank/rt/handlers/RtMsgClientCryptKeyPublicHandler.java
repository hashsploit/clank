package net.hashsploit.clank.rt.handlers;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.rt.serializers.RT_ClientCryptKeyPublic;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;
import net.hashsploit.medius.crypto.CipherContext;
import net.hashsploit.medius.crypto.rc.PS2_RC4;

public class RtMsgClientCryptKeyPublicHandler extends RtMessageHandler {
	
	private RT_ClientCryptKeyPublic reqPacket;
	
	
	public RtMsgClientCryptKeyPublicHandler() {
		super(RtMessageId.CLIENT_CRYPTKEY_PUBLIC);
	}

	@Override
	public void read(ByteBuf buffer) {
		reqPacket = new RT_ClientCryptKeyPublic(buffer);
	}
	
	@Override
	public List<RTMessage> write(MediusClient client) {
		// we already set the PS2_RSA key in the RtDecryptionHandler
		// return a packet with the RC4 key that is encrypted using the new PS2_RSA
		
		List<RTMessage> result = new ArrayList<RTMessage>();
		
		
		logger.severe("PAYLOAD: " + Utils.bytesToHex(reqPacket.getRSAKey()));
		
		
		//byte[] decryptedKey = decryptedData.getData();
		//decryptedKey = Utils.flipByteArray(decryptedKey);
		//PS2_RSA rsaKey = new PS2_RSA(new BigInteger(1, decryptedKey), new BigInteger("17", 10));
		
		// Update the RSA key
		//client.setRSAKey(rsaKey);
		
		
		
		
		String keyString = "E7477438E0234BB8196D574F09337BE7A72971628C551C3373A68BE7F1F108181EAAC2419AFA7583215E79775E9D6DBC8D442545EF396F29C6294C69FC97E177";
		
		byte[] key = Utils.hexStringToByteArray(keyString);
		PS2_RC4 rc4Key = new PS2_RC4(key, CipherContext.RC_CLIENT_SESSION);
		client.setRC4ClientSessionKey(rc4Key);
		
		byte[] generatedKey = Utils.hexStringToByteArray("000000" + keyString);
		//RT_ServerCryptKeyPeer response = new RT_ServerCryptKeyPeer(generatedKey);
		
		
		
		
		
		//result.add(response);
		return result;
	}
	
}
