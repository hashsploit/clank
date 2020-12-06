package net.hashsploit.clank.server.pipeline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.common.MediusDMEWorldHandler;
import net.hashsploit.clank.server.dme.DmeTcpClient;
import net.hashsploit.clank.server.dme.DmeUdpClient;
import net.hashsploit.clank.utils.Utils;

/**
 * Handles a server-side channel.
 */
public class TestHandlerDmeUdp extends ChannelInboundHandlerAdapter { // (1)

	private static final Logger logger = Logger.getLogger("");
	private final DmeUdpClient client;
	
	private static int udpConnId = 0;
	private static HashMap<Integer,InetSocketAddress> idMap = new HashMap<Integer,InetSocketAddress>();
	private static HashMap<InetSocketAddress,Integer> revidMap = new HashMap<InetSocketAddress,Integer>();
	
	private static HashSet<InetSocketAddress> clients = new HashSet<InetSocketAddress>();
		
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

		clients.add(datagram.sender());
		
		byte[] buff = new byte[datagram.content().readableBytes()];
		datagram.content().readBytes(buff);

		logger.finest("TOTAL RAW UDP INCOMING DATA: " + Utils.bytesToHex(buff));
		
		List<RTMessage> packets = Utils.decodeRTMessageFrames(buff);

		for (RTMessage rtmsg: packets) {
			checkFirstPacket(ctx, datagram, rtmsg.toBytes(), datagram.sender().getPort(), datagram.sender().getAddress().toString());
			
			checkBroadcast(ctx, datagram, rtmsg.toBytes());
		}

	}

	private void checkFirstPacket(ChannelHandlerContext ctx, DatagramPacket requestDatagram, byte[] data, int port, String clientAddr) {
//		if (Utils.bytesToHex(data).equals("161d000108017b00bc2900003139322e3136382e312e3939000000005f270100") || Utils.bytesToHex(data).equals("161d0001080165ebbc2900003139322e3136382e312e3939000000005f270100")
//				|| Utils.bytesToHex(data).equals("161d000108017b00bc2900003137322e31362e3232312e32353000005f270100")) { // this is UDP trying to connect
		if (Utils.bytesToHex(data).length() >= 56 && (Utils.bytesToHex(data).substring(56).equals("5f270100") || Utils.bytesToHex(data).substring(56).equals("5f270000"))) { // 161d000108017b00bc2900003139322e3136382e312e3939000000005f270100
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
			
			idMap.put(udpConnId, requestDatagram.sender());
			revidMap.put(requestDatagram.sender(), udpConnId);
			
			String hex = "010801" + 
					Utils.bytesToHex(Utils.shortToBytesLittle((short) udpConnId)) + // DME PLAYER IN WORLD ID
					Utils.bytesToHex(Utils.shortToBytesLittle((short) (udpConnId+1))); // PLAYER COUNT
			udpConnId += 1;

			buffer.put(Utils.hexStringToByteArray(hex));
			byte[] ad = Utils.buildByteArrayFromString(clientAddr, 16);
			buffer.put(ad);
			buffer.put(Utils.shortToBytesLittle((short) port));
			
			RTMessage packetResponse = new RTMessage(RTMessageId.SERVER_CONNECT_ACCEPT_AUX_UDP, buffer.array());
			byte[] payload = packetResponse.toBytes();
			logger.fine("Final payload: " + Utils.bytesToHex(payload));
			ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(payload), requestDatagram.sender()));
		}
	}
	
	private byte[] insertId(byte[] payload, byte id) {
		payload[0] = RTMessageId.CLIENT_APP_SINGLE.getValue();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(payload[0]);
			baos.write(payload[1]);
			baos.write(payload[2]);
			baos.write(id);
			baos.write((byte) 0);
			baos.write(Arrays.copyOfRange(payload, 3, payload.length));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] result = baos.toByteArray();
		// fix the length
		short curLength = Utils.bytesToShortLittle(result[1], result[2]);
		curLength += 2;
		byte[] newLen = Utils.shortToBytesLittle(curLength);
		result[1] = newLen[0];
		result[2] = newLen[1];
		return result;
	}

	private void checkBroadcast(ChannelHandlerContext ctx, DatagramPacket requestDatagram, byte[] data) {
		List<RTMessage> packets = Utils.decodeRTMessageFrames(data);
		for (RTMessage m: packets) {
			logger.fine("UDP Packet ID: " + m.getId().toString());
			logger.fine("UDP Packet ID: " + m.getId().getValue());
			if (m.getId().toString().equals("CLIENT_APP_BROADCAST")) {
				byte[] t = m.toBytes();
				t = insertId(t, revidMap.get(requestDatagram.sender()).byteValue());
				
				for (InetSocketAddress addr : clients) {	
					if (addr != requestDatagram.sender()) {
						ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(t), addr));
					}
				}	
			}
			else if (m.getId().toString().equals("CLIENT_APP_SINGLE")) {
				int targetId = (int) data[3];
				InetSocketAddress target = idMap.get(targetId);
				
				data[3] = revidMap.get(requestDatagram.sender()).byteValue();
				
				ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(data), target));
			}
		}
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
