package net.hashsploit.clank.server.pipeline;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.dme.DmeUdpClient;
import net.hashsploit.clank.utils.Utils;

/**
 * Handles a server-side channel.
 */
public class TestHandlerDmeUdp extends ChannelInboundHandlerAdapter { // (1)

	private static final Logger logger = Logger.getLogger("");
	private final DmeUdpClient client;

	public TestHandlerDmeUdp(final DmeUdpClient client) {
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

		final DatagramPacket datagram = (DatagramPacket) msg;

		byte[] buff = new byte[datagram.content().readableBytes()];
		datagram.content().readBytes(buff);

		logger.finest("TOTAL RAW UDP INCOMING DATA: " + Utils.bytesToHex(buff));

		checkFirstPacket(ctx, datagram, buff, datagram.sender().getPort(), datagram.sender().getAddress().toString());

	}

	private void checkFirstPacket(ChannelHandlerContext ctx, DatagramPacket requestDatagram, byte[] data, int port, String clientAddr) {
		if (Utils.bytesToHex(data).equals("161d000108017b00bc2900003139322e3136382e312e3939000000005f270100") || Utils.bytesToHex(data).equals("161d0001080165ebbc2900003139322e3136382e312e3939000000005f270100")
				|| Utils.bytesToHex(data).equals("161d000108017b00bc2900003137322e31362e3232312e32353000005f270100")) { // this is UDP trying to connect

			// ----------------------------------------- DL VERSION. 1 packet larger for DL
			// than UYA (also different format)
//	    	logger.info("UDP CONNECT REQ DETECTED!:");
//	    	clientAddr = clientAddr.substring(1);
//	    	logger.info("Client Addr: " + clientAddr);
//	    	logger.info("Client port: " + port); 
//    		ByteBuffer buffer = ByteBuffer.allocate(26);
//    		buffer.put(Utils.hexStringToByteArray("0108D3010003"));
//    		byte[] ad = Utils.buildByteArrayFromString(clientAddr, 16);
//    		buffer.put(ad);
//    		buffer.put(Utils.shortToBytesLittle((short) port));
//    		
//    		DataPacket packetResponse = new DataPacket(RTPacketId.SERVER_CONNECT_ACCEPT_AUX_UDP, buffer.array());
//			byte[] payload = packetResponse.toBytes();
//			logger.fine("Final payload: " + Utils.bytesToHex(payload));
//	        ctx.writeAndFlush(new DatagramPacket(
//	                Unpooled.copiedBuffer(payload), requestDatagram.sender()));    	

			logger.info("UDP CONNECT REQ DETECTED!:");
			clientAddr = clientAddr.substring(1);
			logger.info("Client Addr: " + clientAddr);
			logger.info("Client port: " + port);
			ByteBuffer buffer = ByteBuffer.allocate(25);
			buffer.put(Utils.hexStringToByteArray("0108D301000300"));
			byte[] ad = Utils.buildByteArrayFromString(clientAddr, 16);
			buffer.put(ad);
			buffer.put(Utils.shortToBytesLittle((short) port));

			RTMessage packetResponse = new RTMessage(RTMessageId.SERVER_CONNECT_ACCEPT_AUX_UDP, buffer.array());
			byte[] payload = packetResponse.toBytes();
			logger.fine("Final payload: " + Utils.bytesToHex(payload));
			ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(payload), requestDatagram.sender()));
		}
	}

	private void processSinglePacket(ChannelHandlerContext ctx, RTMessage packet) {
		logger.finest("RAW Single packet: " + Utils.bytesToHex(packet.toBytes()));

		logger.fine("Packet ID: " + packet.getId().toString());
		logger.fine("Packet ID: " + packet.getId().getValue());

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}

	public void checkEcho(ChannelHandlerContext ctx, RTMessage packet) {
		if (packet.getId() == RTMessageId.CLIENT_ECHO) {
			// Combine RT id and len
			RTMessage packetResponse = new RTMessage(RTMessageId.CLIENT_ECHO, packet.getPayload());
			byte[] payload = packetResponse.toBytes();
			logger.fine("Final payload: " + Utils.bytesToHex(payload));
			ByteBuf msg = Unpooled.copiedBuffer(payload);
			ctx.write(msg);
			ctx.flush();
		}
	}

}
