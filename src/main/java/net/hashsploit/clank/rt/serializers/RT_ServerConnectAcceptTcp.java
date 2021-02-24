package net.hashsploit.clank.rt.serializers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.utils.Utils;

public class RT_ServerConnectAcceptTcp extends RTMessage {

	private final short unk00;
	private final int unk02;
	private final short unk06;
	private final String ipAddress;

	public RT_ServerConnectAcceptTcp(ByteBuf payload) {
		super(RtMessageId.SERVER_CONNECT_ACCEPT_TCP, payload.readableBytes(), payload);
		unk00 = payload.readShortLE();
		unk02 = payload.readIntLE();
		unk06 = payload.readByte();
		
		final byte[] ipAddrBytes = new byte[16];
		
		payload.readBytes(ipAddrBytes);
		ipAddress = Utils.bytesToStringClean(ipAddrBytes);
	}
	
	public static RT_ServerConnectAcceptTcp build(short unk00, int unk02, byte unk06, String ipAddress) {
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeShortLE(unk00);
		buffer.writeIntLE(unk02);
		buffer.writeByte(unk06); // TODO: this is a short in 1.9+
		buffer.writeBytes(Utils.buildByteArrayFromString(ipAddress, 16));
		return new RT_ServerConnectAcceptTcp(buffer);
	}

	/**
	 * Get the ip address 
	 * @return
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	
	@Override
	public String toString() {
		return Utils.generateDebugPacketString(RT_ServerConnectAcceptTcp.class.getName(),
			new String[] {
				"unk00",
				"unk02",
				"unk06",
				"ipAddress"
			},
			new String[] {
				"" + unk00,
				"" + unk02,
				"" + unk06,
				ipAddress
			}
		);
	}
}
