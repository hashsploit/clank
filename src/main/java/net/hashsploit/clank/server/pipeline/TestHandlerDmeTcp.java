package net.hashsploit.clank.server.pipeline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.dme.DmeTcpClient;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

/**
 * Handles a server-side channel.
 */
public class TestHandlerDmeTcp extends ChannelInboundHandlerAdapter { // (1)

	private static final Logger logger = Logger.getLogger("");
	private final DmeTcpClient client;

	public TestHandlerDmeTcp(final DmeTcpClient client) {
		super();
		this.client = client;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.fine(ctx.channel().remoteAddress() + ": channel active");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.fine(ctx.channel().remoteAddress() + ": channel inactive");
	}

	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        
    	logger.fine("======================================================");
    	logger.fine("======================================================");
    	logger.fine("======================================================");
    
    	final ByteBuffer buffer = toNioBuffer((ByteBuf) msg);

		final byte[] data = new byte[buffer.remaining()];
		buffer.get(data);

		logger.finest("TOTAL RAW INCOMING DATA: " + Utils.bytesToHex(data));

		// Get the packets
		List<DataPacket> packets = Utils.decodeRTMessageFrames(data);

		for (DataPacket packet: packets) {
			processSinglePacket(ctx, packet);
		}
		

    }
    
    private void processSinglePacket(ChannelHandlerContext ctx, DataPacket packet) {
		logger.finest("RAW Single packet: " + Utils.bytesToHex(packet.toBytes()));
		
	    logger.fine("Packet ID: " + packet.getId().toString());
	    logger.fine("Packet ID: " + packet.getId().getByte());
	    
	    checkForBroadcast(ctx, packet);
	    
    	checkForCitiesReconnect(ctx, packet);
    }
    
    private void checkForBroadcast(ChannelHandlerContext ctx, DataPacket packet) {
    	DataPacket d = new DataPacket(RTPacketId.CLIENT_APP_SINGLE, packet.getPayload());
		logger.fine("Broadcast rally: " + Utils.bytesToHex(d.toBytes()));
		ByteBuf msg = Unpooled.copiedBuffer(d.toBytes());
		ctx.write(msg); // (1)
		ctx.flush(); // (2)
    }
    
    
    //
    private void checkForCitiesReconnect(ChannelHandlerContext ctx, DataPacket packet) {
		// TODO Auto-generated method stub
		byte[] data = packet.toBytes();

		if (Utils.bytesToHex(data).equals("0049000108010b00bc29000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")) {
	    	
	    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {

				// ID_14
				// byte[] p14 =
				// Utils.hexStringToByteArray("BE2F79946D8FFFCA8D08671D329ACDB89A488F33ABEDD83C278E8C6F4FA68CBA0A66CEEC21EB8EE6C841B725FAA913E3A6982ECAF76B85977C36C1B4538C8850");
				final byte[] p14 = Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
				outputStream.write(RTPacketId.SERVER_CRYPTKEY_GAME.getByte());
				outputStream.write(Utils.shortToBytesLittle((short) p14.length));
				outputStream.write(p14);

				// ID_07
				// outputStream.write(Utils.hexStringToByteArray("0108D30000010039362E3234322E38382E333600000000"));
				// // ip address to send back to the client
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				baos.write(Utils.hexStringToByteArray("0108D300000100"));

				byte[] ipAddr = client.getIPAddressAsBytes();
				int numZeros = 16 - client.getIPAddressAsBytes().length;
				String zeroString = new String(new char[numZeros]).replace("\0", "00");
				byte[] zeroTrail = Utils.hexStringToByteArray(zeroString);

				baos.write(ipAddr);
				baos.write(zeroTrail);
				baos.write(Utils.hexStringToByteArray("00"));

				outputStream.write(RTPacketId.SERVER_CONNECT_ACCEPT_TCP.getByte());
				outputStream.write(Utils.shortToBytesLittle((short) baos.size()));
				outputStream.write(baos.toByteArray());

				// ID_1a
				// outputStream.write(Utils.hexStringToByteArray("0100"));
				final byte[] p1a = Utils.hexStringToByteArray("0100");
				outputStream.write(RTPacketId.SERVER_CONNECT_COMPLETE.getByte());
				outputStream.write(Utils.shortToBytesLittle((short) p1a.length));
				outputStream.write(p1a);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			byte[] finalPayload = outputStream.toByteArray();

			logger.fine("Cities re-connect Final payload: " + Utils.bytesToHex(finalPayload));
			ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
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


	
	public void checkEcho(ChannelHandlerContext ctx, DataPacket packet) {
			 if (packet.getId() == RTPacketId.CLIENT_ECHO) {
				// Combine RT id and len
				DataPacket packetResponse = new DataPacket(RTPacketId.CLIENT_ECHO, packet.getPayload());
				byte[] payload = packetResponse.toBytes();
				logger.fine("Final payload: " + Utils.bytesToHex(payload));
				ByteBuf msg = Unpooled.copiedBuffer(payload);
				ctx.write(msg);
				ctx.flush();
		}
	}

}
