package net.hashsploit.clank.rt.serializers;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;

public class RT_ClientConnectTcpAuxUdp extends RTMessage {

	private final short dmeWorldId;
	private final byte[] mlsToken;

	public RT_ClientConnectTcpAuxUdp(ByteBuf payload) {
		super(RtMessageId.CLIENT_CONNECT_TCP_AUX_UDP, payload);
		
		payload.skipBytes(5);
		
		dmeWorldId = payload.readShortLE();
		mlsToken = new byte[0];
		
		//dmeWorldId = Utils.bytesToShortLittle(bytes[6], bytes[7]);
		//mlsToken = Arrays.copyOfRange(bytes, bytes.length - 17, bytes.length - 1);
	}

	public short getDmeWorldId() {
		return dmeWorldId;
	}

	public byte[] getMlsToken() {
		return mlsToken;
	}

}
