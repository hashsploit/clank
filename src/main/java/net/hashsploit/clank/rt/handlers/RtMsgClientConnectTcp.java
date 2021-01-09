package net.hashsploit.clank.rt.handlers;

import java.util.List;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.medius.crypto.Utils;
import net.hashsploit.medius.crypto.rc.PS2_RC4;
import net.hashsploit.medius.crypto.rsa.PS2_RSA;

public class RtMsgClientConnectTcp extends RtMessageHandler {

	private int targetWorldId;
	private int appId;
	public byte[] key;
	private byte[] sessionKey;
	private byte[] accessToken;
	
	public RtMsgClientConnectTcp() {
		super(RtMessageId.CLIENT_CONNECT_TCP);
	}

	@Override
	public void read(ByteBuf buffer) {
		
		byte[] bytes;
		int length = buffer.readableBytes();
		
		if (buffer.hasArray()) {
			bytes = buffer.array();
		} else {
			bytes = new byte[length];
			buffer.getBytes(buffer.readerIndex(), bytes);
		}
		
		logger.severe(Utils.bytesToHex(bytes));
		
		targetWorldId = buffer.readInt();
		appId = buffer.readInt();
		key = buffer.readBytes(128).array();
		
		if (buffer.readerIndex() < buffer.readableBytes()) {
			sessionKey = buffer.readBytes(MediusConstants.SESSIONKEY_MAXLEN.getValue()).array();
			accessToken = buffer.readBytes(MediusConstants.ACCESSKEY_MAXLEN.getValue()).array();
		}
		
	}
	
	@Override
	public List<RTMessage> write(MediusClient client) {
		
		if (client.getServer().getEmulationMode() == EmulationMode.MEDIUS_AUTHENTICATION_SERVER ||
			client.getServer().getEmulationMode() == EmulationMode.MEDIUS_LOBBY_SERVER ||
			client.getServer().getEmulationMode() == EmulationMode.MEDIUS_UNIVERSE_INFORMATION_SERVER) {
			MediusConfig cfg = (MediusConfig) Clank.getInstance().getConfig();
			
			if (cfg.isEncrypted()) {
				//PS2_RC4 rc4Key = client.setRC4ClientSessionKey(rc4Key);
				
				
				
			}
			
		} else if (client.getServer().getEmulationMode() == EmulationMode.DME_SERVER) {
			DmeConfig cfg = (DmeConfig) Clank.getInstance().getConfig();
		}
		
		
		
		
		return null;
	}
	
}
