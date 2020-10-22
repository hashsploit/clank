package net.hashsploit.clank.server.pipeline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.dme.DmeTcpClient;
import net.hashsploit.clank.server.dme.DmeUdpClient;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.scert.SCERTConstants;
import net.hashsploit.clank.utils.NetUtils;
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
    	if (Utils.bytesToHex(data).equals("161d000108017b00bc2900003139322e3136382e312e3939000000005f270100")) {		  // this is UDP trying to connect
	    	logger.info("UDP CONNECT REQ DETECTED!:");
	    	clientAddr = clientAddr.substring(1);
	    	logger.info("Client Addr: " + clientAddr);
	    	logger.info("Client port: " + port); 
    		ByteBuffer buffer = ByteBuffer.allocate(26);
    		buffer.put(Utils.hexStringToByteArray("0000"));
    		buffer.put(Utils.hexStringToByteArray("D452"));
    		buffer.put(Utils.hexStringToByteArray("00000100"));
    		byte[] ad = Utils.buildByteArrayFromString(clientAddr, 16);
    		buffer.put(ad);
    		buffer.put(Utils.shortToBytesLittle((short) port));
    		
    		DataPacket packetResponse = new DataPacket(RTPacketId.SERVER_CONNECT_ACCEPT_AUX_UDP, buffer.array());
			byte[] payload = packetResponse.toBytes();
			logger.fine("Final payload: " + Utils.bytesToHex(payload));
	        ctx.write(new DatagramPacket(
	                Unpooled.copiedBuffer(payload), requestDatagram.sender()));    	
    	}
    }
    
    private void processSinglePacket(ChannelHandlerContext ctx, DataPacket packet) {
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
