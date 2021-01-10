package net.hashsploit.clank.rt.serializers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;

public class RT_ServerConnectComplete extends RTMessage {

	private final short unk00;

	public RT_ServerConnectComplete(ByteBuf payload) {
		super(RtMessageId.SERVER_CONNECT_COMPLETE, payload.readableBytes(), payload);
		unk00 = payload.readShortLE();
	}
	
	public static RT_ServerConnectComplete build(short unk00) {
		ByteBuf buffer = Unpooled.buffer(2);
		buffer.writeShortLE(unk00);
		return new RT_ServerConnectComplete(buffer);
	}
	
	@Override
	public String toString() {
		return Utils.generateDebugPacketString(RT_ServerConnectComplete.class.getName(),
			new String[] {
				"unk00"
			},
			new String[] {
				"" + unk00
			}
		);
	}
}
