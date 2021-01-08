package net.hashsploit.clank.rt.handlers;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.rt.serializers.RT_ClientCryptKeyPublic;
import net.hashsploit.clank.rt.serializers.RT_ServerCryptKeyPeer;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.medius.crypto.Utils;

public class RtMsgClientCryptKeyPublicHandler extends RtMessageHandler {
	
	RT_ClientCryptKeyPublic reqPacket;
	
	
	public RtMsgClientCryptKeyPublicHandler() {
		super(RtMessageId.CLIENT_CRYPTKEY_PUBLIC);
	}

	@Override
	public void read(ByteBuf buffer) {
		reqPacket = new RT_ClientCryptKeyPublic(buffer.array());
		logger.finest(reqPacket.toString());
	}
	
	@Override
	public List<RTMessage> write(MediusClient client) {
		// we already set the PS2_RSA key in the RtDecryptionHandler
		// return a packet with the RC4 key that is encrypted using the new PS2_RSA
		
		List<RTMessage> result = new ArrayList<RTMessage>();
		
		byte[] generatedKey = Utils.hexStringToByteArray("000000E7477438E0234BB8196D574F09337BE7A72971628C551C3373A68BE7F1F108181EAAC2419AFA7583215E79775E9D6DBC8D442545EF396F29C6294C69FC97E177");
		RT_ServerCryptKeyPeer response = new RT_ServerCryptKeyPeer(generatedKey);
		result.add(response);
		
		return result;
	}
	
}
