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
import io.netty.handler.codec.MessageToMessageDecoder;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.rt.serializers.RT_ClientConnectTcpAuxUdp;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.dme.DmeServer;
import net.hashsploit.clank.server.dme.DmeTcpClient;
import net.hashsploit.clank.server.dme.DmeWorldManager;
import net.hashsploit.clank.utils.Utils;

/**
 * Handles a server-side channel.
 */
public class TestHandlerDmeTcp extends MessageToMessageDecoder<ByteBuf> { // (1)

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
//		logger.fine("Updating player status to MLS...");		
//		logger.fine("Done!");
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		logger.fine("[TCP]" + ctx.channel().remoteAddress() + ": channel inactive");
	}


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    	logger.fine("======================================================");
    	logger.fine("======================================================");
    	logger.fine("======================================================");
		
		RTMessage packet = new RTMessage(msg);

		logger.finest("TOTAL RAW INCOMING DATA: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(packet.getFullMessage())));

		processSinglePacket(ctx, packet);
		checkBroadcast(packet);
	}
    
    
    
	private void checkBroadcast(RTMessage m) {
		DmeWorldManager dmeWorldManager = ((DmeServer) client.getServer()).getDmeWorldManager();

		if (m.getId().toString().equals("CLIENT_APP_BROADCAST")) {
			dmeWorldManager.broadcast(client.getPlayer(), Utils.nettyByteBufToByteArray(m.getFullMessage()));
		}
		else if (m.getId().toString().equals("CLIENT_APP_SINGLE")) {
			dmeWorldManager.clientAppSingle(client.getPlayer(), Utils.nettyByteBufToByteArray(m.getFullMessage()));
		}
	}
    
    private void processSinglePacket(ChannelHandlerContext ctx, RTMessage packet) {
		logger.finest("RAW Single packet: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(packet.getFullMessage())));
		
	    logger.fine("Packet ID: " + packet.getId().toString());
	    logger.fine("Packet ID: " + packet.getId().getValue());
	    
	    
	    checkForTcpAuxUdpConnect(ctx, packet);
	    	    
		checkClientReady(ctx, Utils.nettyByteBufToByteArray(packet.getFullMessage()));
		
		checkEcho(ctx, packet);
    }
    
    private void checkClientReady(ChannelHandlerContext ctx, byte[] data) {
    	if (Utils.bytesToHex(data).equals("170000")) {		  //
    		// SERVER CONNECT COMPLETE
    		
    		//int playerId = client.getServer().getDmeWorldManager().add(client);
    		// ---------- The player is now fully connected
    		DmeWorldManager dmeWorldManager = ((DmeServer) client.getServer()).getDmeWorldManager();
    		
    		logger.info(dmeWorldManager.toString());
    		logger.info("### SERVER_CONNECT_COMPLETE (fully connected): " + client.getPlayer().toString());
    		dmeWorldManager.playerFullyConnected(client.getPlayer());
    		logger.info(dmeWorldManager.toString());
    		
    		int playerId = client.getPlayer().getAccountId();
		
    		//byte [] t1 = Utils.shortToBytesLittle(((short) (playerId)));
    		byte [] t1 = Utils.shortToBytesLittle(((short) dmeWorldManager.getPlayerCount(client.getPlayer())));
    		RTMessage c1 = new RTMessage(RtMessageId.SERVER_CONNECT_COMPLETE, t1);
    		logger.finest("Final Payload: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(c1.getFullMessage())));
    		ByteBuf msg1 = c1.getFullMessage();
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
    		
    		RTMessage c2 = new RTMessage(RtMessageId.SERVER_APP, baos.toByteArray());
    		logger.finest("Final Payload: " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(c2.getFullMessage())));
    		ByteBuf msg2 = Unpooled.copiedBuffer(c2.getFullMessage().array());
    		ctx.write(msg2); // (1)
    		ctx.flush(); // 
    	
    	}
    }
    
    private void checkForTcpAuxUdpConnect(ChannelHandlerContext ctx, RTMessage packet) {
    	
    	if (packet.getId() == RtMessageId.CLIENT_CONNECT_TCP_AUX_UDP) {
    		RT_ClientConnectTcpAuxUdp connectPacket = new RT_ClientConnectTcpAuxUdp(packet.getFullMessage());
    		
    		short dmeWorldId = (short) connectPacket.getTargetWorldId();
    		//logger.info("Detected TCP AUX UDP CONNECT. Requested world ID: " + Integer.toString((int) dmeWorldId));
    		//logger.info("Detected TCP AUX UDP CONNECT. mlsToken: " + Utils.bytesToHex(connectPacket.getAccessToken()));

    		// ---------- Add the player to the world
    		// Set accountId for this player -> client
    		String mlsToken = Utils.bytesToStringClean(connectPacket.getAccessToken()) + "\0";
    		client.getPlayer().setMlsToken(mlsToken);
    		
    		DmeWorldManager dmeWorldManager = ((DmeServer) client.getServer()).getDmeWorldManager();
    		logger.info(dmeWorldManager.toString());
    		logger.info("### CLIENT_CONNECT_TCP_AUX_UDP: [SessionKey: " + Utils.bytesToStringClean(connectPacket.getAccessToken()) + ", dmeWorldId: " + Integer.toString((int) dmeWorldId) + "]");
    		dmeWorldManager.addPlayer(dmeWorldId, client.getPlayer());
    		logger.info(dmeWorldManager.toString());
    		
    		int dmePlayerId = client.getPlayer().getAccountId();
    		int playerCount = dmeWorldManager.getPlayerCount(client.getPlayer());
    		
    		if (dmePlayerId == 0 || dmePlayerId == -1) {
    			logger.severe("Unknon accountId found for: " + "[SessionKey: " + Utils.bytesToStringClean(connectPacket.getAccessToken()) + ", dmeWorldId: " + Integer.toString((int) dmeWorldId) + "]");
    		}
    		
    		// First crypto leave empty
    		byte [] emptyCrypto1 = Utils.buildByteArrayFromString("", 64);
    		RTMessage c1 = new RTMessage(RtMessageId.SERVER_CRYPTKEY_GAME, emptyCrypto1);
    		logger.finest("Final Payload: " + Utils.bytesToHex(c1.getFullMessage().array()));
    		ByteBuf msg1 = c1.getFullMessage();
    		ctx.write(msg1); // (1)
    		ctx.flush(); // 
    		
    		// Second crypto leave empty
    		RTMessage c2 = new RTMessage(RtMessageId.SERVER_CRYPTKEY_PEER, emptyCrypto1);
    		logger.finest("Final Payload: " + Utils.bytesToHex(c2.getFullMessage().array()));
    		ByteBuf msg3 = c2.getFullMessage();
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
    		RTMessage d = new RTMessage(RtMessageId.SERVER_CONNECT_ACCEPT_TCP, buffer.array());
    		logger.finest("Final Payload: " + Utils.bytesToHex(d.getFullMessage().array()));
    		ByteBuf msg = d.getFullMessage();
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

    		RTMessage da = new RTMessage(RtMessageId.SERVER_INFO_AUX_UDP, buf.array());
    		logger.finest("Final Payload: " + Utils.bytesToHex(da.getFullMessage().array()));
    		ByteBuf msg2 = da.getFullMessage();
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
			 if (packet.getId() == RtMessageId.CLIENT_ECHO) {
				// Combine RT id and len
				RTMessage packetResponse = new RTMessage(RtMessageId.CLIENT_ECHO, 1, packet.getPayload());
				byte[] payload = packetResponse.getFullMessage().array();
				logger.fine("Final payload: " + Utils.bytesToHex(payload));
				ByteBuf msg = Unpooled.copiedBuffer(payload);
				ctx.write(msg);
				ctx.flush();
		}
	}

}
