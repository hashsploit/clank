package net.hashsploit.clank.server.pipeline;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.dme.DmeUdpClient;
import net.hashsploit.clank.server.dme.DmeUdpServer;
import net.hashsploit.clank.server.dme.DmeWorldManager;
import net.hashsploit.clank.utils.RtBufferDeframer;
import net.hashsploit.clank.utils.Utils;

/**
 * Handles a server-side channel.
 */
public class TestHandlerDmeUdp extends ChannelInboundHandlerAdapter { // (1)

	private static final Logger logger = Logger.getLogger(TestHandlerDmeUdp.class.getName());
	private final DmeUdpClient client;
	private RtBufferDeframer deframer;

	
	public TestHandlerDmeUdp(final DmeUdpClient client) {
		super();
		this.deframer = new RtBufferDeframer();
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
		final DatagramPacket datagram = (DatagramPacket) msg;
		
		ByteBuf allData = datagram.content();
		List<ByteBuf> deframedPackets = deframer.deframe(allData);
		
		List<RTMessage> packets = new ArrayList<RTMessage>();
		for (ByteBuf b: deframedPackets) {
			packets.add(new RTMessage(b));
		}

		for (RTMessage rtmsg: packets) {
			if (rtmsg.getId() != RtMessageId.CLIENT_ECHO && 
				rtmsg.getId() != RtMessageId.CLIENT_APP_BROADCAST &&
				rtmsg.getId() != RtMessageId.CLIENT_CONNECT_AUX_UDP &&
				rtmsg.getId() != RtMessageId.CLIENT_FLUSH_SINGLE &&
				rtmsg.getId() != RtMessageId.CLIENT_FLUSH_ALL &&
				rtmsg.getId() != RtMessageId.CLIENT_DISCONNECT &&
				rtmsg.getId() != RtMessageId.CLIENT_APP_SINGLE
				) {
				logger.severe("UNKNOWN DME UDP PACKET: " + Utils.bytesToHex(rtmsg.getFullMessage().array()));
			}
			
			checkFirstPacket(ctx, datagram, rtmsg.getFullMessage().array(), datagram.sender().getPort(), datagram.sender().getAddress().toString());
			
			checkBroadcast(ctx, datagram, rtmsg);
		}

	}

	private void checkFirstPacket(ChannelHandlerContext ctx, DatagramPacket requestDatagram, byte[] data, int port, String clientAddr) {	
		if (Utils.bytesToHex(data).length() >= 56 && (Utils.bytesToHex(data).substring(56,60).equals("5f27"))) { 
			// example: 161d000108017b00bc2900003139322e3136382e312e3939000000005f270100
			clientAddr = clientAddr.substring(1);
			//logger.info("Client Addr: " + clientAddr);
			//logger.info("Client port: " + port);
			ByteBuffer buffer = ByteBuffer.allocate(25);
			
    		DmeWorldManager dmeWorldManager = ((DmeUdpServer) client.getServer()).getDmeWorldManager();
			int dmeWorldId = (int) Utils.bytesToShortLittle(data[6], data[7]);
    		int playerId = (int) Utils.bytesToShortLittle(data[30], data[31]);
    		
    		client.setDmeWorldId(dmeWorldId);
    		client.setPlayerId(playerId);
    		
			logger.info(dmeWorldManager.toString());
			logger.info("### UDP_CONNECT_REQ: Dme World Id: " + Utils.bytesToHex(Utils.intToBytesLittle(dmeWorldId)) + " PlayerId: " + Utils.bytesToHex(Utils.intToBytesLittle(playerId)));
    		dmeWorldManager.playerUdpConnected(dmeWorldId, playerId, (DatagramChannel) ctx.channel(), requestDatagram.sender());
    		logger.info(dmeWorldManager.toString());

			short playerCount = (short) dmeWorldManager.getPlayerCount(dmeWorldId);
			
			String hex = "010801" + 
					Utils.bytesToHex(Utils.shortToBytesLittle((short) playerId)) + // DME PLAYER IN WORLD ID
					Utils.bytesToHex(Utils.shortToBytesLittle(playerCount)); // PLAYER COUNT

			buffer.put(Utils.hexStringToByteArray(hex));
			byte[] ad = Utils.buildByteArrayFromString(clientAddr, 16);
			buffer.put(ad);
			buffer.put(Utils.shortToBytesLittle((short) port));
			
			RTMessage packetResponse = new RTMessage(RtMessageId.SERVER_CONNECT_ACCEPT_AUX_UDP, buffer.array());
			byte[] payload = packetResponse.getFullMessage().array();
			logger.fine("Final payload: " + Utils.bytesToHex(payload));
			ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(payload), requestDatagram.sender()));
		}
	}

	private void checkBroadcast(ChannelHandlerContext ctx, DatagramPacket requestDatagram, RTMessage m) {
			if (m.getId().toString().equals("CLIENT_APP_BROADCAST")) {
				logger.finest("UDP BROADCAST From Addr: " + requestDatagram.sender() + " DmeWorldId: " + client.getDmeWorldId() + " PlayerIndex: " + client.getPlayerId() + " | " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(m.getFullMessage())));
				DmeWorldManager dmeWorldManager = ((DmeUdpServer) client.getServer()).getDmeWorldManager();
				dmeWorldManager.broadcastUdp(requestDatagram.sender(), m.getFullMessage().array());	
			}
			else if (m.getId().toString().equals("CLIENT_APP_SINGLE")) {
				logger.finest("UDP CLIENT APP SINGLE From Addr: " + requestDatagram.sender() + " DmeWorldId: " + client.getDmeWorldId() + " PlayerIndex: " + client.getPlayerId() + " | " + Utils.bytesToHex(Utils.nettyByteBufToByteArray(m.getFullMessage())));
				DmeWorldManager dmeWorldManager = ((DmeUdpServer) client.getServer()).getDmeWorldManager();
				dmeWorldManager.clientAppSingleUdp(requestDatagram.sender(), m.getFullMessage().array());
			}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}

	public void checkEcho(ChannelHandlerContext ctx,  DatagramPacket requestDatagram, RTMessage packet) {
		if (packet.getId() == RtMessageId.CLIENT_ECHO) {
			// Combine RT id and len
			logger.finest("UDP ECHO From Addr: " + requestDatagram.sender() + " DmeWorldId: " + client.getDmeWorldId() + " PlayerIndex: " + client.getPlayerId() + " | " + Utils.bytesToHex(packet.getFullMessage().array()));
			RTMessage packetResponse = new RTMessage(RtMessageId.CLIENT_ECHO, packet.getPayload());
			byte[] payload = packetResponse.getFullMessage().array();
			logger.finest("Return echo " + Utils.bytesToHex(payload));
			ByteBuf msg = Unpooled.copiedBuffer(payload);
			ctx.write(msg);
			ctx.flush();
		}
	}

}
