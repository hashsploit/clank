package net.hashsploit.clank.server.pipeline;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.utils.Utils;

/**
 * Handles a server-side channel.
 */
public class TestHandlerMLS extends ChannelInboundHandlerAdapter { // (1)

	private static final Logger logger = Logger.getLogger("");
	private final MediusClient client;

	public TestHandlerMLS(final MediusClient client) {
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

		logger.fine("TOTAL RAW INCOMING DATA: " + Utils.bytesToHex(data));

		// Get the packets
		List<RTMessage> packets = Utils.decodeRTMessageFrames(data);

		for (RTMessage packet : packets) {
			processSinglePacket(ctx, packet);
		}

	}

	private void processSinglePacket(ChannelHandlerContext ctx, RTMessage packet) {
		// Get the raw data
		logger.fine("RAW Single packet: " + Utils.bytesToHex(packet.toBytes()));

		logger.fine("Packet ID: " + packet.getId().toString());
		logger.fine("Packet ID: " + packet.getId().getValue());

		// Check echo
		checkEcho(ctx, packet);

		// Check initial connection TODO: take it out of here!
		checkInitialConnect(ctx, packet);

		// Handle cities reconnect TODO: take it out of here!
		//checkForCitiesReconnect(ctx, packet);

		// Medius packets
		checkMediusPackets(packet);
	}

	private void checkMediusPackets(RTMessage packet) {
		// ALL OTHER PACKETS THAT ARE MEDIUS PACKETS
		if (packet.getId().toString().contains("APP")) {

			MediusMessage incomingMessage = new MediusMessage(packet.getPayload());

			logger.fine("Found Medius Packet ID: " + Utils.bytesToHex(incomingMessage.getMediusPacketType().getShortByte()));
			logger.fine("Found Medius Packet ID: " + incomingMessage.getMediusPacketType().toString());

			// Detect which medius packet is being parsed
			MediusPacketHandler mediusPacket = client.getServer().getMediusMessageMap().get(incomingMessage.getMediusPacketType());

			// Process this medius packet
			mediusPacket.read(client, incomingMessage);
			mediusPacket.write(client);
		}
	}

	public void checkInitialConnect(ChannelHandlerContext ctx, RTMessage packet) {
		byte[] data = packet.toBytes();

		// RT ID 00, Length 6b00
		// if
		// (Utils.bytesToHex(data).equals("006b000108010500bc2900000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000031330000000000000000000000000000005a657138626b494b77704d6632444f5000"))
		// {
		// if
		// (Utils.bytesToHex(data).equals("006b000108010500bc2900000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000031330000000000000000000000000000005a657138626b494b77704d6632444f5000"))
		// {
		if (data[0] == 0x00 && data[1] == 0x6b) {
			// TODO: Don't hard code the player ID (0x33 in this example)
			logger.fine("Initial connect to MLS!");
			String mlsToken = Utils.bytesToHex(Arrays.copyOfRange(data, 186/2, (186+32)/2));
			
			MediusLobbyServer server = (MediusLobbyServer) client.getServer();
			
			logger.finest("Players before:");
			logger.finest(server.playersToString());

			// Update the player with the MLS playerlist
			logger.info("MLS TOKEN: " + mlsToken);
			int accountId = Clank.getInstance().getDatabase().getAccountIdFromMlsToken(mlsToken);
			logger.info("Player account id detected:" + Integer.toString(accountId));
			client.getPlayer().setAccountId(accountId);
			int worldIdToJoin = (int) Utils.bytesToShortLittle(data[6], data[7]);
			logger.info("World Id To Join: " + Integer.toString(worldIdToJoin));
			client.getPlayer().setChatWorld(worldIdToJoin);
			server.updatePlayerStatus(client.getPlayer(), MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD);
			logger.info("Player username detected:" + client.getPlayer().getUsername());

			
			logger.finest("Players after:");
			logger.finest(server.playersToString());
			
			logger.fine(Utils.bytesToHex(data));
			byte[] firstPart = Utils.hexStringToByteArray("07170001081000000100");
			logger.severe(client.getIPAddress());
			byte[] ipAddr = client.getIPAddress().getBytes();
			int numZeros = 16 - client.getIPAddress().getBytes().length;
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

			byte[] finalPayload = outputStream.toByteArray();

			logger.fine("Final payload: " + Utils.bytesToHex(finalPayload));
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

	public void checkEcho(ChannelHandlerContext ctx, RTMessage packet) {
		if (packet.getId() == RtMessageId.CLIENT_ECHO) {
			// Combine RT id and len
			RTMessage packetResponse = new RTMessage(RtMessageId.CLIENT_ECHO, packet.getPayload());
			byte[] payload = packetResponse.toBytes();
			logger.fine("Final payload: " + Utils.bytesToHex(payload));
			ByteBuf msg = Unpooled.copiedBuffer(payload);
			ctx.write(msg);
			ctx.flush();
		}
	}

}
