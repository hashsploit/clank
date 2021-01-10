package net.hashsploit.clank.rt.serializers;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.utils.Utils;

public class RT_ClientConnectTcp extends RTMessage {
	
	private int targetWorldId;
	private int appId;
	private byte[] key;
	private byte[] sessionKey;
	private byte[] accessToken;

	public RT_ClientConnectTcp(ByteBuf payload) {
		super(payload);
			
		targetWorldId = payload.readIntLE();
		appId = payload.readIntLE();
		key = new byte[64]; // TODO: Make this a constant
		payload.readBytes(key);
		
		if (payload.readerIndex() < payload.readableBytes()) {
			sessionKey = payload.readBytes(MediusConstants.SESSIONKEY_MAXLEN.getValue()).array();
			accessToken = payload.readBytes(MediusConstants.ACCESSKEY_MAXLEN.getValue()).array();
		}
	}

	
	@Override
	public String toString() {
		return Utils.generateDebugPacketString(RT_ClientConnectTcp.class.getName(),
			new String[] {
				"key"
			},
			new String[] {
				Utils.bytesToHex(key)
			}
		);
	}


	public int getTargetWorldId() {
		return targetWorldId;
	}


	public int getAppId() {
		return appId;
	}


	public byte[] getKey() {
		return key;
	}


	public byte[] getSessionKey() {
		return sessionKey;
	}


	public byte[] getAccessToken() {
		return accessToken;
	}

}
