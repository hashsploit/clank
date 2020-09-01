package net.hashsploit.clank.server.pipeline;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import net.hashsploit.clank.server.DataPacket;

public class RTEncoder extends MessageToMessageEncoder<DataPacket> {
	
	public RTEncoder() {
		
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, DataPacket msg, List<Object> out) throws Exception {
		
	}
	
	
	
}
