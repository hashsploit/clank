package net.hashsploit.clank.server.pipeline;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.utils.Utils;

/**
 * Handles a server-side channel.
 */
public class TestHandlerMLS extends ChannelInboundHandlerAdapter { // (1)
	
	private static final Logger logger = Logger.getLogger("");
	private final Client client;
	private final PacketReplayMLS replayMap;
	
	public TestHandlerMLS(final Client client) {
		super();
		this.client = client;
		this.replayMap = new PacketReplayMLS();
	}
	

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.fine(ctx.channel().remoteAddress() + ": channel active");
	}


	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.fine(ctx.channel().remoteAddress() + ": channel inactive");
	}
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars).toLowerCase();
	}
	
	/* s must be an even-length string. */
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        
		final ByteBuffer buffer = toNioBuffer((ByteBuf) msg);
		final byte[] data = new byte[buffer.remaining()];
		buffer.get(data);

		logger.finest("RAW: " + bytesToHex(data));

		// No valid packet is < 3 or > 2048 bytes, drop the connection
		if (data.length < 3 || data.length > 2048) {
			ctx.close();
			return;
		}

		// Get RT Packet ID
		RTPacketId rtid = null;
		
		for (RTPacketId p : RTPacketId.values()) {
			if (p.getByte() == data[0]) {
				rtid = p;
				break;
			}
		}
	    logger.fine("Packet ID: " + rtid.toString());
	    logger.fine("Packet ID: " + rtid.getByte());
		// =============================================
		//              IF STATEMENTS
		// =============================================	  
	    byte[] finalPayload = null; 
	    String res = replayMap.getResponse(data);
	    
	    if (bytesToHex(data).equals("006b000108010500bc2900000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000031330000000000000000000000000000005a657138626b494b77704d6632444f5000")) {
	    	byte[] firstPart = hexStringToByteArray("07170001081000000100");
			byte[] ipAddr = "192.168.1.99".getBytes();
			int numZeros = 16 - "192.168.1.99".length();
			String zeroString = new String(new char[numZeros]).replace("\0", "00");
			byte[] zeroTrail = hexStringToByteArray(zeroString);
			byte[] lastPart = hexStringToByteArray("1a02000100");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			try {
				outputStream.write(firstPart);
				outputStream.write(ipAddr);
				outputStream.write(zeroTrail);
				outputStream.write(lastPart);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finalPayload = outputStream.toByteArray();
	    }
	    else if (!res.equals("")) {
	    	logger.fine("Found matching response: " + res);
	    	finalPayload = hexStringToByteArray(res);
	    }
		
		if (finalPayload != null) {
			logger.fine("Final payload: " + bytesToHex(finalPayload));
	        msg = Unpooled.copiedBuffer(finalPayload);
	        ctx.write(msg); // (1)
	        ctx.flush(); // (2)
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
    
	private static ByteBuffer toNioBuffer(final ByteBuf buffer) {
		if (buffer.isDirect()) {
			return buffer.nioBuffer();
		}
		final byte[] bytes = new byte[buffer.readableBytes()];
		buffer.getBytes(buffer.readerIndex(), bytes);
		return ByteBuffer.wrap(bytes);
	}
}