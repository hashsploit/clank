package net.hashsploit.clank.server.pipeline;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import net.hashsploit.clank.server.Client;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.EncryptedDataPacket;
import net.hashsploit.clank.server.ISCERTMessage;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.medius.MediusPacket;
import net.hashsploit.clank.server.medius.MediusPacketType;
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
	

	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        
    	logger.fine("======================================================");
    	logger.fine("======================================================");
    	logger.fine("======================================================");

    	final ByteBuffer buffer = toNioBuffer((ByteBuf) msg);
		final byte[] data = new byte[buffer.remaining()];
		buffer.get(data);

		logger.finest("RAW: " + Utils.bytesToHex(data));

		// Get the packets
		 List<DataPacket> packets = processData(ctx, data);
		for (DataPacket packet: packets) {
			processSinglePacket(ctx, packet.toData().array());
		}

    }
    
    private void processSinglePacket(ChannelHandlerContext ctx, byte[] data) {
		 logger.finest("RAW Single packet: " + Utils.bytesToHex(data));
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
	    
	    if (Utils.bytesToHex(data).equals("006b000108010500bc2900000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000031330000000000000000000000000000005a657138626b494b77704d6632444f5000")) {
	    	// TODO: Don't hard code the player ID (0x33 in this example)
	    	logger.fine(Utils.bytesToHex(data));
	    	byte[] firstPart = Utils.hexStringToByteArray("07170001081000000100");
			byte[] ipAddr = "192.168.1.99".getBytes();
			int numZeros = 16 - "192.168.1.99".length();
			String zeroString = new String(new char[numZeros]).replace("\0", "00");
			byte[] zeroTrail = Utils.hexStringToByteArray(zeroString);
			byte[] lastPart = Utils.hexStringToByteArray("1a02000100");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
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
	    
			logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
	        ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
	        ctx.write(msg); // (1)
	        ctx.flush(); // (2)
	    }
	    
	    
	    
	    // Handle cities reconnect
	    else if (Utils.bytesToHex(data).equals("0049000108010b00bc29000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")) {
	    	
	    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				
				// ID_14
				//byte[] p14 = Utils.hexStringToByteArray("BE2F79946D8FFFCA8D08671D329ACDB89A488F33ABEDD83C278E8C6F4FA68CBA0A66CEEC21EB8EE6C841B725FAA913E3A6982ECAF76B85977C36C1B4538C8850");
				final byte[] p14 = Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
				outputStream.write(RTPacketId.SERVER_CRYPTKEY_GAME.getByte());
				outputStream.write(Utils.shortToBytesLittle((short) p14.length));
				outputStream.write(p14);
				
				// ID_07
				//outputStream.write(Utils.hexStringToByteArray("0108D30000010039362E3234322E38382E333600000000")); // ip address to send back to the client
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
				//outputStream.write(Utils.hexStringToByteArray("0100"));
				final byte[] p1a = Utils.hexStringToByteArray("0100");
				outputStream.write(RTPacketId.SERVER_CONNECT_COMPLETE.getByte());
				outputStream.write(Utils.shortToBytesLittle((short) p1a.length));
				outputStream.write(p1a);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			finalPayload = outputStream.toByteArray();
	    
			logger.fine("Cities re-connect Final payload: " + Utils.bytesToHex(finalPayload));
	        ByteBuf msg = Unpooled.copiedBuffer(finalPayload);
	        ctx.write(msg); // (1)
	        ctx.flush(); // (2)
		}
	    
	    
	    
	    
	    
	    // ALL OTHER PACKETS THAT ARE MEDIUS PACKETS
	    if (rtid.toString().contains("APP")) {
		    
			// Get medius packet type
		    MediusPacketType mediusPacketType = null;
		    ByteBuffer bb = ByteBuffer.allocate(2);
		    bb.order(ByteOrder.LITTLE_ENDIAN);
		    bb.put(data[3]);
		    bb.put(data[4]);
		    short shortVal = bb.getShort(0);

		    // TODO: Make this not O(n)
			for (MediusPacketType p : MediusPacketType.values()) {
				if (p.getShort() == shortVal) {
					mediusPacketType = p;
					break;
				}
			}	
			
			ByteBuffer buffer = ByteBuffer.allocate(2);
			buffer.putShort(shortVal);
						
			logger.fine("Found Medius Packet ID: " + Utils.bytesToHex(buffer.array()));
			logger.fine("Found Medius Packet ID: " + mediusPacketType.toString());
			
			// Detect which medius packet is being parsed
		    MediusPacket mediusPacket = client.getMediusMap().get(mediusPacketType);
		    
		    // Remove RT-ID and length from the packet data
		    byte[] packetData = Arrays.copyOfRange(data,5,data.length);
		    
		    // Process this medius packet
		    mediusPacket.process(client, ctx, packetData);
		    		    
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
	
	private static List<DataPacket> processData(ChannelHandlerContext ctx, byte[] data) {
		final List<DataPacket> packets = new ArrayList<DataPacket>();

		int index = 0;

		try {
			while (index < data.length) {
				final byte id = data[index + 0];
				
				ByteBuffer bb = ByteBuffer.allocate(2);
				bb.order(ByteOrder.LITTLE_ENDIAN);
				bb.put(data[index+1]);
				bb.put(data[index+2]);
				short length = bb.getShort(0);
				
				logger.fine("Length: " + Integer.toString(length));
				byte[] finalData = new byte[length];
				int offset = 0;

				if (length > 0) {
					// ID(1) + Length(2)
					offset += 1 + 2;
				}

					//logger.warning("PLAIN DATA PACKET");
					System.arraycopy(data, index + offset, finalData, 0, finalData.length);
					
					RTPacketId rtid = null;
					
					for (RTPacketId p : RTPacketId.values()) {
						if (p.getByte() == id) {
							rtid = p;
							break;
						}
					}
					
					packets.add(new DataPacket(rtid, finalData));
				
				index += length + 3;
			}
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}

		return packets;
	}

}