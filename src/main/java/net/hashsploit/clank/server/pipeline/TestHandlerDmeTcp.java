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
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
import net.hashsploit.clank.server.scert.SCERTConstants;
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
	    logger.fine("Packet ID: " + packet.getId().getValue());
	    
	    
	    checkForTcpAuxUdpConnect(ctx, packet);
	    	    
		checkClientReady(ctx, packet.toBytes());
    }
    
    private void checkClientReady(ChannelHandlerContext ctx, byte[] data) {
    	if (Utils.bytesToHex(data).equals("170000")) {		  // this is UDP trying to connect
    		// SERVER CONNECT COMPLETE
    		byte [] t1 = Utils.hexStringToByteArray("0100");
    		DataPacket c1 = new DataPacket(RTPacketId.SERVER_CONNECT_COMPLETE, t1);
    		logger.finest("Final Payload: " + Utils.bytesToHex(c1.toBytes()));
    		ByteBuf msg1 = Unpooled.copiedBuffer(c1.toBytes());
    		ctx.write(msg1); // (1)
    		ctx.flush(); // 
    		
    		// DME VERSION ID THING
    		byte[] t2 = Utils.hexStringToByteArray("0000312E32322E3031343100000000000000");
    		DataPacket c2 = new DataPacket(RTPacketId.SERVER_APP, t2);
    		logger.finest("Final Payload: " + Utils.bytesToHex(c2.toBytes()));
    		ByteBuf msg2 = Unpooled.copiedBuffer(c2.toBytes());
    		ctx.write(msg2); // (1)
    		ctx.flush(); // 
    		
    		// send temporary thing to try to work
//    		byte[] temp1 = Utils.hexStringToByteArray("00180200000000000000C0A801429A1800004B14BC139A18000001000000001002004B14BC1300006B8F99EC1BAF06D2674284B5305EE6E38B1DE7331F2FBF31DE497228B7C52162F18DAE8913C40C43C0E890D14EEE16AD07C64FD9281D8B972D78BE78D1B290CE001605000300010000000100000000000000");
//    		//byte[] temp1 = Utils.hexStringToByteArray("00160500030001006CD501000000000000000009D771090006000000001703000000000400010000046E000000000000740D020000000000744E575F47616D6553657474696E6700506A00000000000000000000000000004433244B207B5257247D000000000000006576696E005F536D61736865720000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080460000000000000E4D6E4C00000000003C3300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003001300FFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFFFFFFFFFF00000200FFFFFFFFFFFFFFFFFFFFFFFF0000000000000000000000000000000002000000FFFFFFFFFFFFFFFF2800000000000000000000000101010101010001010000010101001403FF000000010501097D0F009C7C1000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF5881DA44AEF8D744000080BF000080BF000080BF000080BF000080BF000080BF0000C8420000DC42000080BF000080BF000080BF000080BF000080BF000080BF");
//    		DataPacket dp_temp1 = new DataPacket(RTPacketId.CLIENT_APP_SINGLE, temp1);
//    		logger.finest("Final Payload: " + Utils.bytesToHex(dp_temp1.toBytes()));
//    		ByteBuf temp1_bb = Unpooled.copiedBuffer(dp_temp1.toBytes());
//    		ctx.write(temp1_bb);
//    		ctx.flush();
    		// tnw game settings
//    		byte[] t3 = Utils.hexStringToByteArray("00160500030001006CD501000000000000000009D771090006000000001703000000000400010000046E000000000000740D020000000000744E575F47616D6553657474696E6700506A00000000000000000000000000004433244B207B5257247D000000000000006576696E005F536D61736865720000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080460000000000000E4D6E4C00000000003C3300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003001300FFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFFFFFFFFFF00000200FFFFFFFFFFFFFFFFFFFFFFFF0000000000000000000000000000000002000000FFFFFFFFFFFFFFFF2800000000000000000000000101010101010001010000010101001403FF000000010501097D0F009C7C1000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF5881DA44AEF8D744000080BF000080BF000080BF000080BF000080BF000080BF0000C8420000DC42000080BF000080BF000080BF000080BF000080BF000080BF");
//    		DataPacket c3 = new DataPacket(RTPacketId.CLIENT_APP_SINGLE, t3);
//    		logger.finest("Final Payload: " + Utils.bytesToHex(c3.toBytes()));
//    		ByteBuf msg3 = Unpooled.copiedBuffer(c3.toBytes());
//    		ctx.write(msg3); // (1)
//    		ctx.flush(); // 
    	}
    }
    
    private void checkForTcpAuxUdpConnect(ChannelHandlerContext ctx, DataPacket packet) {
    	
    	if (packet.getId() == RTPacketId.CLIENT_CONNECT_TCP_AUX_UDP) {
    		logger.info("Detected TCP AUX UDP CONNECT");

    		
    		// First crypto leave empty
    		byte [] emptyCrypto1 = Utils.buildByteArrayFromString("", 64);
    		DataPacket c1 = new DataPacket(RTPacketId.SERVER_CRYPTKEY_GAME, emptyCrypto1);
    		logger.finest("Final Payload: " + Utils.bytesToHex(c1.toBytes()));
    		ByteBuf msg1 = Unpooled.copiedBuffer(c1.toBytes());
    		ctx.write(msg1); // (1)
    		ctx.flush(); // 
    		
    		// Second crypto leave empty
    		DataPacket c2 = new DataPacket(RTPacketId.SERVER_CRYPTKEY_PEER, emptyCrypto1);
    		logger.finest("Final Payload: " + Utils.bytesToHex(c2.toBytes()));
    		ByteBuf msg3 = Unpooled.copiedBuffer(c2.toBytes());
    		ctx.write(msg3); // (1)
    		ctx.flush(); // 

    		// Server Accept TCP
    		ByteBuffer buffer = ByteBuffer.allocate(23);
    		//buffer.put(Utils.hexStringToByteArray("0108D301000300"));    		
    		buffer.put(Utils.hexStringToByteArray("0108D301000300"));    		
    		final byte[] ipAddr = Utils.buildByteArrayFromString(client.getIPAddress(), 16);
    		buffer.put(ipAddr);
    		DataPacket d = new DataPacket(RTPacketId.SERVER_CONNECT_ACCEPT_TCP, buffer.array());
    		logger.finest("Final Payload: " + Utils.bytesToHex(d.toBytes()));
    		ByteBuf msg = Unpooled.copiedBuffer(d.toBytes());
    		ctx.write(msg); // (1)
    		ctx.flush(); // 
    		
    		// Server AUX UDP Info (IP and port)
    		ByteBuffer buf = ByteBuffer.allocate(18);
    		final byte[] udpAddr = Utils.buildByteArrayFromString("192.168.1.99", 16);
    		buf.put(ipAddr);
    		buf.put(Utils.hexStringToByteArray("51C3"));
    		
    		DataPacket da = new DataPacket(RTPacketId.SERVER_INFO_AUX_UDP, buf.array());
    		logger.finest("Final Payload: " + Utils.bytesToHex(da.toBytes()));
    		ByteBuf msg2 = Unpooled.copiedBuffer(da.toBytes());
    		ctx.write(msg2); // (1)
    		ctx.flush(); // 
    		
    	}
    	
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
				outputStream.write(RTPacketId.SERVER_CRYPTKEY_GAME.getValue());
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

				outputStream.write(RTPacketId.SERVER_CONNECT_ACCEPT_TCP.getValue());
				outputStream.write(Utils.shortToBytesLittle((short) baos.size()));
				outputStream.write(baos.toByteArray());

				// ID_1a
				// outputStream.write(Utils.hexStringToByteArray("0100"));
				final byte[] p1a = Utils.hexStringToByteArray("0100");
				outputStream.write(RTPacketId.SERVER_CONNECT_COMPLETE.getValue());
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
