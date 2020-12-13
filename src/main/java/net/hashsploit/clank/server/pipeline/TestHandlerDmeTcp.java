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
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.dme.DmeServer;
import net.hashsploit.clank.server.dme.DmeTcpClient;
import net.hashsploit.clank.server.dme.DmeWorldManager;
import net.hashsploit.clank.server.rpc.PlayerUpdateRequest.PlayerStatus;
import net.hashsploit.clank.utils.Utils;

/**
 * Handles a server-side channel.
 */
public class TestHandlerDmeTcp extends ChannelInboundHandlerAdapter { // (1)

	private static final Logger logger = Logger.getLogger(TestHandlerDmeTcp.class.getName());
	private final DmeTcpClient client;
	private int curUdpPort = ((DmeConfig) Clank.getInstance().getConfig()).getUdpStartingPort();

	public TestHandlerDmeTcp(final DmeTcpClient client) {
		super();
		this.client = client;
		
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.fine("[TCP]" + ctx.channel().remoteAddress() + ": channel active");
		logger.fine("Updating player status to MLS...");		
		logger.fine("Done!");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.fine("[TCP]" + ctx.channel().remoteAddress() + ": channel inactive");
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
		List<RTMessage> packets = Utils.decodeRTMessageFrames(data);

		for (RTMessage packet : packets) {
			processSinglePacket(ctx, packet);
			checkBroadcast(packet);
		}
		
    }
    
    
    
	private void checkBroadcast(RTMessage m) {
		DmeWorldManager dmeWorldManager = ((DmeServer) client.getServer()).getDmeWorldManager();

		if (m.getId().toString().equals("CLIENT_APP_BROADCAST")) {
			dmeWorldManager.broadcast(client.getSocket(), m.toBytes());
		}
		else if (m.getId().toString().equals("CLIENT_APP_SINGLE")) {
			dmeWorldManager.clientAppSingle(client.getSocket(), m.toBytes());
		}
	}
    
    private void processSinglePacket(ChannelHandlerContext ctx, RTMessage packet) {
		logger.finest("RAW Single packet: " + Utils.bytesToHex(packet.toBytes()));
		
	    logger.fine("Packet ID: " + packet.getId().toString());
	    logger.fine("Packet ID: " + packet.getId().getValue());
	    
	    
	    checkForTcpAuxUdpConnect(ctx, packet);
	    	    
		checkClientReady(ctx, packet.toBytes());
		
		checkEcho(ctx, packet);
    }
    
    private void checkClientReady(ChannelHandlerContext ctx, byte[] data) {
    	if (Utils.bytesToHex(data).equals("170000")) {		  //
    		// SERVER CONNECT COMPLETE
    		
    		//int playerId = client.getServer().getDmeWorldManager().add(client);
    		// ---------- The player is now fully connected
    		DmeWorldManager dmeWorldManager = ((DmeServer) client.getServer()).getDmeWorldManager();
    		dmeWorldManager.playerFullyConnected(client.getSocket());
    		int playerId = dmeWorldManager.getPlayerId(client.getSocket());

    		logger.info(dmeWorldManager.toString());

    		//client.updateDmePlayer(playerId, dmeWorldManager.getWorldId(playerId), PlayerStatus.CONNECTED);
    		
    		
    		//byte [] t1 = Utils.hexStringToByteArray("0100"); // THIS IS THE PLAYER ID IN THE DME WORLD (first player connected = 0x0100
    		byte [] t1 = Utils.shortToBytesLittle(((short) (playerId+1))); // THIS IS THE PLAYER ID IN THE DME WORLD (first player connected = 0x0100
    		RTMessage c1 = new RTMessage(RTMessageId.SERVER_CONNECT_COMPLETE, t1);
    		logger.finest("Final Payload: " + Utils.bytesToHex(c1.toBytes()));
    		ByteBuf msg1 = Unpooled.copiedBuffer(c1.toBytes());
    		ctx.write(msg1); // (1)
    		ctx.flush(); // 
    		
    		// DME Version (RT 00)
    		//byte[] t2 = Utils.hexStringToByteArray("0000312E32322E3031343100000000000000");
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		try {
    			//baos.write(MediusMessageType.DMEServerVersion.getShortByte());
    			//baos.write(Utils.buildByteArrayFromString(Clank.NAME + " v" + Clank.VERSION, MediusConstants.SERVERVERSION_MAXLEN.getValue()));
    			//baos.write(Utils.hexStringToByteArray("0000312E32322E3031343100000000000000"));
    			baos.write(Utils.hexStringToByteArray("0000322e31302e3030303900000000000000"));
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		
    		RTMessage c2 = new RTMessage(RTMessageId.SERVER_APP, baos.toByteArray());
    		logger.finest("Final Payload: " + Utils.bytesToHex(c2.toBytes()));
    		ByteBuf msg2 = Unpooled.copiedBuffer(c2.toBytes());
    		ctx.write(msg2); // (1)
    		ctx.flush(); // 
    	
    	}
    }
    
    private void checkForTcpAuxUdpConnect(ChannelHandlerContext ctx, RTMessage packet) {
    	
    	if (packet.getId() == RTMessageId.CLIENT_CONNECT_TCP_AUX_UDP) {
    		logger.info("Detected TCP AUX UDP CONNECT");
    		short dmeWorldId = Utils.bytesToShortLittle(packet.toBytes()[6], packet.toBytes()[7]);
    		logger.info("Dme world requested: " + Integer.toString((int) dmeWorldId));
    		
    		// ---------- Add the player to the world
    		DmeWorldManager dmeWorldManager = ((DmeServer) client.getServer()).getDmeWorldManager();
    		dmeWorldManager.addPlayer(dmeWorldId, client.getSocket(), ((DmeServer) client.getServer()).getRpcClient());
    		int dmePlayerId = dmeWorldManager.getPlayerId(client.getSocket());
    		int playerCount = dmeWorldManager.getPlayerCount(client.getSocket());
    		
    		logger.info(dmeWorldManager.toString());
			logger.info("Dme World Id: " + Utils.bytesToHex(Utils.intToBytesLittle(dmeWorldId)));
			logger.info("playerId: " + Utils.bytesToHex(Utils.intToBytesLittle(dmePlayerId)));
    		
    		// First crypto leave empty
    		byte [] emptyCrypto1 = Utils.buildByteArrayFromString("", 64);
    		RTMessage c1 = new RTMessage(RTMessageId.SERVER_CRYPTKEY_GAME, emptyCrypto1);
    		logger.finest("Final Payload: " + Utils.bytesToHex(c1.toBytes()));
    		ByteBuf msg1 = Unpooled.copiedBuffer(c1.toBytes());
    		ctx.write(msg1); // (1)
    		ctx.flush(); // 
    		
    		// Second crypto leave empty
    		RTMessage c2 = new RTMessage(RTMessageId.SERVER_CRYPTKEY_PEER, emptyCrypto1);
    		logger.finest("Final Payload: " + Utils.bytesToHex(c2.toBytes()));
    		ByteBuf msg3 = Unpooled.copiedBuffer(c2.toBytes());
    		ctx.write(msg3); // (1)
    		ctx.flush(); // 

    		// Server Accept TCP
    		ByteBuffer buffer = ByteBuffer.allocate(23);
    		//buffer.put(Utils.hexStringToByteArray("0108D301000300"));    		
    		String hex = "010810" + 
    				Utils.bytesToHex(Utils.shortToBytesLittle((short) dmePlayerId)) + // DME WORLD PLAYER ID (0,1,2 etc)
    				Utils.bytesToHex(Utils.shortToBytesLittle((short) playerCount)); // PLAYER COUNT
    		
    		buffer.put(Utils.hexStringToByteArray(hex));    		
    		final byte[] ipAddr = Utils.buildByteArrayFromString(client.getIPAddress(), 16);
    		buffer.put(ipAddr);
    		RTMessage d = new RTMessage(RTMessageId.SERVER_CONNECT_ACCEPT_TCP, buffer.array());
    		logger.finest("Final Payload: " + Utils.bytesToHex(d.toBytes()));
    		ByteBuf msg = Unpooled.copiedBuffer(d.toBytes());
    		ctx.write(msg); // (1)
    		ctx.flush(); // 
    		
    		// Server AUX UDP Info (IP and port)
    		ByteBuffer buf = ByteBuffer.allocate(18);
    		String udpAddress = ((DmeConfig) Clank.getInstance().getConfig()).getUdpAddress();
    		
    		if (udpAddress == null || udpAddress.isEmpty()) {
    			udpAddress = Utils.getPublicIpAddress();
    		}
    		
    		logger.finest("DME config ip: " + udpAddress + ":" + curUdpPort);
    		
    		final byte[] udpAddr = Utils.buildByteArrayFromString(udpAddress, 16);
    		
    		buf.put(udpAddr);
    		buf.put(Utils.shortToBytesLittle((short) curUdpPort));
    		curUdpPort += 1;

    		RTMessage da = new RTMessage(RTMessageId.SERVER_INFO_AUX_UDP, buf.array());
    		logger.finest("Final Payload: " + Utils.bytesToHex(da.toBytes()));
    		ByteBuf msg2 = Unpooled.copiedBuffer(da.toBytes());
    		ctx.write(msg2); // (1)
    		ctx.flush(); // 
    		
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


	
	private void checkEcho(ChannelHandlerContext ctx, RTMessage packet) {
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
