package net.hashsploit.clank.rt.serializers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RT_ClientHello extends RTMessage {
	
	public RT_ClientHello(ByteBuf payload) {
		super(RtMessageId.SERVER_HELLO); //Sends back 0x25, but doesn't give the proper response. Needs Fixing.
	}
}