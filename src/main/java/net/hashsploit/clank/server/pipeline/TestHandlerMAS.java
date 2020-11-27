package net.hashsploit.clank.server.pipeline;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.utils.Utils;

/**
 * Handles a server-side channel.
 */
public class TestHandlerMAS extends ChannelInboundHandlerAdapter { // (1)
	
	private static final Logger logger = Logger.getLogger("");
	private final MediusClient client;
	
	public TestHandlerMAS(final MediusClient client) {
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) {      
		final ByteBuffer buffer = toNioBuffer((ByteBuf) msg);
		final byte[] data = new byte[buffer.remaining()];
		buffer.get(data);


		// No valid packet is < 3 or > 2048 bytes, drop the connection
		if (data.length < 3 || data.length > 2048) {
			ctx.close();
			return;
		}

		// Get RT Packet ID
		RTMessageId rtid = null;
		
		for (RTMessageId p : RTMessageId.values()) {
			if (p.getValue() == data[0]) {
				rtid = p;
				break;
			}
		}
		
		logger.finest("TOTAL RAW INCOMING DATA: " + Utils.bytesToHex(data));

		// Get the packets
		List<RTMessage> packets = Utils.decodeRTMessageFrames(data);

		for (RTMessage packet: packets) {

		    logger.fine("Packet ID: " + rtid.toString());
		    logger.fine("Packet ID: " + rtid.getValue());
			// =============================================
			//              IF STATEMENTS
			// =============================================	  	    
		    checkRTConnect(ctx, packet);
			
		    checkMediusPackets(ctx, packet);
		}

    }
    
    private void checkMediusPackets(ChannelHandlerContext ctx, RTMessage packet) {
	    // ALL OTHER PACKETS THAT ARE MEDIUS PACKETS
    	MediusMessage mm = null;
	    if (packet.getId().toString().contains("APP")) {
	    	
	    	MediusMessage incomingMessage = new MediusMessage(packet.getPayload());

			logger.fine("Found Medius Packet ID: " + Utils.bytesToHex(incomingMessage.getMediusPacketType().getShortByte()));
			logger.fine("Found Medius Packet ID: " + incomingMessage.getMediusPacketType().toString());
			
			// Detect which medius packet is being parsed
		    MediusPacketHandler mediusPacket = client.getServer().getMediusMessageMap().get(incomingMessage.getMediusPacketType());
		    
		    // Process this medius packet
		    mediusPacket.read(incomingMessage);
		    mm = mediusPacket.write(client);	    
		    if (mm != null) {
		    	RTMessage responsepacket = new RTMessage(RTMessageId.SERVER_APP, mm.toBytes());
	
				byte[] finalPayload = responsepacket.toBytes();
				logger.finest("Final payload: " + Utils.bytesToHex(finalPayload));
				ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
				ctx.write(msg);
				ctx.flush();	
		    }
	    }
    }
    
    private void checkRTConnect(ChannelHandlerContext ctx, RTMessage packet) {
		if (packet.getId().getValue() == (byte) 0x00) {
			// =============================================
			// CLIENT_CONNECT_TCP (0x00)
			// =============================================
			String clientIP = client.getIPAddress().substring(1);
			logger.fine(clientIP);
			RTMessageId resultrtid = RTMessageId.SERVER_CONNECT_ACCEPT_TCP;
			
			byte[] header = Utils.hexStringToByteArray("01081000000100");
			byte[] ipAddr = clientIP.getBytes();
			int numZeros = 16 - client.getIPAddress().substring(1).length();
			String zeroString = new String(new char[numZeros]).replace("\0", "00");
			byte[] zeroTrail = Utils.hexStringToByteArray(zeroString);
			byte[] allByteArray = new byte[header.length + ipAddr.length + zeroTrail.length];
			ByteBuffer buff = ByteBuffer.wrap(allByteArray);
			buff.put(header);
			buff.put(ipAddr);
			buff.put(zeroTrail);
			
			logger.fine("Data header: " + Utils.bytesToHex(header));
			logger.fine("IP: " + Utils.bytesToHex(ipAddr));
			logger.fine("IP Padding encode/decode: " + Utils.bytesToHex(zeroTrail));

			byte[] payload = buff.array();
			
			logger.fine("Payload: " + Utils.bytesToHex(payload));
			
			RTMessage dataPacket = new RTMessage(resultrtid, payload);
			byte[] dataPacketBuffer = dataPacket.toBytes();
			
			byte[] extraPacket = Utils.hexStringToByteArray("1A02000100");
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			try {
				outputStream.write(dataPacketBuffer);
				outputStream.write(extraPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			byte[] finalPayload = outputStream.toByteArray();
			logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
	        ctx.write(Unpooled.copiedBuffer(finalPayload)); // (1)
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