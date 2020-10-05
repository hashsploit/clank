package net.hashsploit.clank.server.pipeline;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Logger;

import org.bouncycastle.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusPacket;
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
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
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
		RTPacketId rtid = null;
		
		for (RTPacketId p : RTPacketId.values()) {
			if (p.getByte() == data[0]) {
				rtid = p;
				break;
			}
		}
		
		logger.finest("TOTAL RAW INCOMING DATA: " + Utils.bytesToHex(data));

		// Get the packets
		List<DataPacket> packets = Utils.decodeRTMessageFrames(data);

		for (DataPacket packet: packets) {
			//DataPacket packet = new DataPacket(rtid, Arrays.copyOfRange(data, 3, data.length));
			logger.finest("RAW: " + Utils.bytesToString(packet.toBytes()));

		    logger.fine("Packet ID: " + rtid.toString());
		    logger.fine("Packet ID: " + rtid.getByte());
			// =============================================
			//              IF STATEMENTS
			// =============================================	  	    
		    checkRTConnect(ctx, packet);
			
		    checkMediusPackets(ctx, packet);
		}

    }
    
    private void checkMediusPackets(ChannelHandlerContext ctx, DataPacket packet) {
	    // ALL OTHER PACKETS THAT ARE MEDIUS PACKETS
    	MediusPacket mm = null;
	    if (packet.getId().toString().contains("APP")) {
	    	
	    	MediusPacket incomingMessage = new MediusPacket(packet.getPayload());

			logger.fine("Found Medius Packet ID: " + Utils.bytesToHex(incomingMessage.getMediusPacketType().getShortByte()));
			logger.fine("Found Medius Packet ID: " + incomingMessage.getMediusPacketType().toString());
			
			// Detect which medius packet is being parsed
		    MediusPacketHandler mediusPacket = client.getMediusMap().get(incomingMessage.getMediusPacketType());
		    
		    // Process this medius packet
		    mediusPacket.read(incomingMessage);
		    mm = mediusPacket.write(client);	    
		    if (mm != null) {
		    	DataPacket responsepacket = new DataPacket(RTPacketId.SERVER_APP, mm.toBytes());
	
				byte[] finalPayload = responsepacket.toBytes();
				logger.finest("Final payload: " + Utils.bytesToHex(finalPayload));
				ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
				ctx.write(msg);
				ctx.flush();	
		    }
	    }
    }
    
    private void checkRTConnect(ChannelHandlerContext ctx, DataPacket packet) {
		if (packet.getId().getByte() == (byte) 0x00) {
			// =============================================
			// CLIENT_CONNECT_TCP (0x00)
			// =============================================
			String clientIP = client.getIPAddress().substring(1);
			logger.fine(clientIP);
			RTPacketId resultrtid = RTPacketId.SERVER_CONNECT_ACCEPT_TCP;
			
			byte[] header = hexStringToByteArray("01081000000100");
			byte[] ipAddr = clientIP.getBytes();
			int numZeros = 16 - client.getIPAddress().substring(1).length();
			String zeroString = new String(new char[numZeros]).replace("\0", "00");
			byte[] zeroTrail = hexStringToByteArray(zeroString);
			byte[] allByteArray = new byte[header.length + ipAddr.length + zeroTrail.length];
			ByteBuffer buff = ByteBuffer.wrap(allByteArray);
			buff.put(header);
			buff.put(ipAddr);
			buff.put(zeroTrail);
			
			logger.fine("Data header: " + bytesToHex(header));
			logger.fine("IP: " + bytesToHex(ipAddr));
			logger.fine("IP Padding encode/decode: " + bytesToHex(zeroTrail));

			byte[] payload = buff.array();
			
			logger.fine("Payload: " + bytesToHex(payload));
			
			DataPacket dataPacket = new DataPacket(resultrtid, payload);
			byte[] dataPacketBuffer = dataPacket.toBytes();
			
			byte[] extraPacket = hexStringToByteArray("1A02000100");
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			try {
				outputStream.write(dataPacketBuffer);
				outputStream.write(extraPacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			byte[] finalPayload = outputStream.toByteArray();
			logger.fine("Final payload: " + bytesToHex(finalPayload));
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