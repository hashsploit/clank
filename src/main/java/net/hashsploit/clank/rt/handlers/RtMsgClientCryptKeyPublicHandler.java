package net.hashsploit.clank.rt.handlers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.rt.serializers.RT_ClientCryptKeyPublic;
import net.hashsploit.clank.rt.serializers.RT_ServerCryptKeyPeer;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;
import net.hashsploit.medius.crypto.CipherContext;
import net.hashsploit.medius.crypto.rc.PS2_RC4;
import net.hashsploit.medius.crypto.rsa.PS2_RSA;

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
		
		byte[] nn = reqPacket.getRSAKey();
		nn = Utils.flipByteArray(nn);
		
		client.setRSAKey(new PS2_RSA(new BigInteger(1, nn), new BigInteger("17", 10)));
		
		
		List<RTMessage> result = new ArrayList<RTMessage>();
		
		String keyString = "E7477438E0234BB8196D574F09337BE7A72971628C551C3373A68BE7F1F108181EAAC2419AFA7583215E79775E9D6DBC8D442545EF396F29C6294C69FC97E177";
		
		byte[] key = Utils.hexStringToByteArray(keyString);
		PS2_RC4 rc4Key = new PS2_RC4(key, CipherContext.RC_CLIENT_SESSION);
		client.setRC4ClientSessionKey(rc4Key);
				
		RT_ServerCryptKeyPeer response = new RT_ServerCryptKeyPeer(Unpooled.wrappedBuffer(key));

		result.add(response);
		return result;
	}
	
}
