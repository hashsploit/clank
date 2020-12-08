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
import net.hashsploit.clank.server.dme.DmeServer;
import net.hashsploit.clank.server.dme.DmeTcpClient;
import net.hashsploit.clank.server.dme.DmeUdpClient;
import net.hashsploit.clank.server.dme.DmeUdpServer;
import net.hashsploit.clank.server.dme.DmeWorldManager;
import net.hashsploit.clank.utils.Utils;

/**
 * Handles a server-side channel.
 */
public class TestHandlerDmeUdp extends ChannelInboundHandlerAdapter { // (1)

	private static final Logger logger = Logger.getLogger(TestHandlerDmeUdp.class.getName());
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
		
		List<RTMessage> packets = Utils.decodeRTMessageFrames(buff);

		for (RTMessage rtmsg: packets) {
			logger.finest("RAW UDP Single packet: " + Utils.bytesToHex(rtmsg.toBytes()));
			
		    logger.fine("Packet ID: " + rtmsg.getId().toString());
		    logger.fine("Packet ID: " + rtmsg.getId().getValue());
			
			checkFirstPacket(ctx, datagram, rtmsg.toBytes(), datagram.sender().getPort(), datagram.sender().getAddress().toString());
			
			checkBroadcast(ctx, datagram, rtmsg.toBytes());
		}

	}

	private void checkFirstPacket(ChannelHandlerContext ctx, DatagramPacket requestDatagram, byte[] data, int port, String clientAddr) {	
		if (Utils.bytesToHex(data).length() >= 56 && (Utils.bytesToHex(data).substring(56,60).equals("5f27"))) { 
			// example: 161d000108017b00bc2900003139322e3136382e312e3939000000005f270100
			logger.info("UDP CONNECT REQ DETECTED!:");
			clientAddr = clientAddr.substring(1);
			logger.info("Client Addr: " + clientAddr);
			logger.info("Client port: " + port);
			ByteBuffer buffer = ByteBuffer.allocate(25);
			
    		DmeWorldManager dmeWorldManager = ((DmeUdpServer) client.getServer()).getDmeWorldManager();
    		logger.info(dmeWorldManager.toString());
			int dmeWorldId = (int) Utils.bytesToShortLittle(data[6], data[7]);
    		int playerId = (int) Utils.bytesToShortLittle(data[30], data[31]);
    		
			logger.info("Dme World Id: " + Utils.bytesToHex(Utils.intToBytesLittle(dmeWorldId)));
			logger.info("playerId: " + Utils.bytesToHex(Utils.intToBytesLittle(playerId)));

    		dmeWorldManager.playerUdpConnected(dmeWorldId, playerId, requestDatagram.sender());
			
			short playerCount = (short) dmeWorldManager.getPlayerCount(dmeWorldId);
			
			String hex = "010801" + 
					Utils.bytesToHex(Utils.shortToBytesLittle((short) playerId)) + // DME PLAYER IN WORLD ID
					Utils.bytesToHex(Utils.shortToBytesLittle(playerCount)); // PLAYER COUNT

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

	private void checkBroadcast(ChannelHandlerContext ctx, DatagramPacket requestDatagram, byte[] data) {
		List<RTMessage> packets = Utils.decodeRTMessageFrames(data);
		for (RTMessage m: packets) {
			logger.fine("UDP Packet ID: " + m.getId().toString());
			logger.fine("UDP Packet ID: " + m.getId().getValue());
			if (m.getId().toString().equals("CLIENT_APP_BROADCAST")) {
				DmeWorldManager dmeWorldManager = ((DmeUdpServer) client.getServer()).getDmeWorldManager();
				dmeWorldManager.broadcastUdp(ctx, requestDatagram.sender(), m.toBytes());	
			}
			else if (m.getId().toString().equals("CLIENT_APP_SINGLE")) {
				DmeWorldManager dmeWorldManager = ((DmeUdpServer) client.getServer()).getDmeWorldManager();
				dmeWorldManager.clientAppSingleUdp(ctx, requestDatagram.sender(), m.toBytes());
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
