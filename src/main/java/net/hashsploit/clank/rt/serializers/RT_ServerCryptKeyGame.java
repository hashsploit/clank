package net.hashsploit.clank.rt.serializers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;

public class RT_ServerCryptKeyGame extends RTMessage {

	private final byte[] serverKey;
	
	public RT_ServerCryptKeyGame(ByteBuf payload) {
		super(RtMessageId.SERVER_CRYPTKEY_GAME, payload.readableBytes(), payload);
		serverKey = new byte[64];
		payload.readBytes(serverKey);
	}
	
	public static RT_ServerCryptKeyGame build(byte[] serverKey) {
		ByteBuf payload = Unpooled.wrappedBuffer(serverKey);
		return new RT_ServerCryptKeyGame(payload);
	}
	
	/**
	 * Returns the key that will be sent back to the 
	 * @return
	 */
	public byte[] getServerKey() {
		return serverKey;
	}

	@Override
	public String toString() {
		return Utils.generateDebugPacketString(RT_ServerCryptKeyGame.class.getName(),
			new String[] {
				"serverKey"
			},
			new String[] {
				Utils.bytesToHex(serverKey)
			}
		);
	}
}
