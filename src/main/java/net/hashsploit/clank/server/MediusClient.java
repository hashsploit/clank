package net.hashsploit.clank.server;

import java.nio.ByteOrder;
import java.util.Random;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusServer;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.pipeline.MasHandler;
import net.hashsploit.clank.server.pipeline.RtDecryptionHandler;
import net.hashsploit.clank.server.pipeline.RtEncryptionHandler;
import net.hashsploit.clank.server.pipeline.RtFrameDecoderHandler;
import net.hashsploit.clank.server.pipeline.RtFrameEncoderHandler;
import net.hashsploit.clank.server.pipeline.TestHandlerMLS;
import net.hashsploit.clank.utils.Utils;
import net.hashsploit.medius.crypto.CipherContext;
import net.hashsploit.medius.crypto.rc.PS2_RC4;
import net.hashsploit.medius.crypto.rsa.PS2_RSA;

public class MediusClient implements IClient {

	private static final Logger logger = Logger.getLogger(MediusClient.class.getName());

	private final MediusServer server;
	private final SocketChannel socketChannel;
	private ClientState state;
	private boolean encrypted;
	private int applicationId;
	private long unixConnectTime;
	private long txPacketCount;
	private long rxPacketCount;

	private PS2_RSA rsaKey;
	private PS2_RC4 rc4Key;

	private Player player;

	public MediusClient(MediusServer server, SocketChannel channel) {
		this.server = server;
		this.socketChannel = channel;
		this.state = ClientState.UNAUTHENTICATED;
		this.encrypted = true;
		this.unixConnectTime = System.currentTimeMillis();
		this.txPacketCount = 0L;
		this.rxPacketCount = 0L;
		this.player = null;

		logger.fine(String.format("Client connected: %s:%d", getIPAddress(), getPort()));

		// Decode the packet into frames
		channel.pipeline().addLast(new RtFrameDecoderHandler(ByteOrder.LITTLE_ENDIAN, MediusConstants.MEDIUS_MESSAGE_MAXLEN.getValue(), 1, 2, 0, 0, false));

		// Decrypt the packet
		if (((MediusConfig) Clank.getInstance().getConfig()).isEncrypted()) {
			channel.pipeline().addLast(new RtDecryptionHandler(this));
		}

		// Initialize the correct pipeline handler for this server.
		switch (server.getEmulationMode()) {
			case MEDIUS_AUTHENTICATION_SERVER:
				channel.pipeline().addLast(new MasHandler(this));
				break;
			case MEDIUS_LOBBY_SERVER:
				channel.pipeline().addLast(new TestHandlerMLS(this));
				break;
			default:
				break;
		}

		// Re-encrypt the packet
		if (((MediusConfig) Clank.getInstance().getConfig()).isEncrypted()) {
			channel.pipeline().addLast(new RtEncryptionHandler(this));
		}

		// Re-frame the packets
		channel.pipeline().addLast(new RtFrameEncoderHandler());

		ChannelFuture closeFuture = channel.closeFuture();
		closeFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> future) throws Exception {
				onDisconnect();
			}
		});

	}

	/**
	 * Get the Medius Server associated with this client.
	 * 
	 * @return
	 */
	public MediusServer getServer() {
		return server;
	}

	/**
	 * Get the Socket Channel associated with this client.
	 * 
	 * @return
	 */
	protected SocketChannel getSocketChannel() {
		return socketChannel;
	}

	/**
	 * Get the client's socket IP Address.
	 */
	public String getIPAddress() {
		return socketChannel.remoteAddress().getAddress().getHostAddress();
	}

	/**
	 * Get the client's socket port.
	 */
	public int getPort() {
		return socketChannel.remoteAddress().getPort();
	}

	/**
	 * Get the client's state bitmask.
	 */
	public ClientState getClientState() {
		return state;
	}

	/**
	 * Set the client's state bitmask.
	 * 
	 * @param state
	 */
	protected void setClientState(final ClientState state) {
		this.state = state;
	}

	/**
	 * Returns true if the connection to this client is encrypted.
	 * 
	 * @return
	 */
	public boolean isEncrypted() {
		return encrypted;
	}

	/**
	 * Set if this client is using encryption or not.
	 * 
	 * @param encrypted
	 */
	protected void setEncrypted(final boolean encrypted) {
		this.encrypted = encrypted;
	}

	/**
	 * Get the player object associated with this client.
	 * 
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Set the player object to be associated with this client.
	 * 
	 * @param player
	 */
	protected void setPlayer(final Player player) {
		this.player = player;
	}

	/**
	 * Returns the UNIX timestamp of when the connection started.
	 * 
	 * @return
	 */
	public long getUnixConnectTime() {
		return unixConnectTime;
	}

	/**
	 * Get the total number of received packets.
	 * 
	 * @return
	 */
	public long getRxPacketCount() {
		return rxPacketCount;
	}

	/**
	 * Get the total number of transmitted packets.
	 * 
	 * @return
	 */
	public long getTxPacketCount() {
		return txPacketCount;
	}

	/**
	 * Send a raw payload to the client.
	 * 
	 * @param data
	 */
	public void sendRaw(byte[] data) {
		ByteBuf msg = Unpooled.copiedBuffer(data);
		socketChannel.pipeline().writeAndFlush(msg);
		txPacketCount++;
	}

	/**
	 * Send a data packet to the client.
	 * 
	 * @param msg
	 */
	public void sendMessage(RTMessage msg) {
		sendRaw(msg.toBytes());
	}

	public void sendMediusMessage(MediusMessage msg) {
		RTMessage packet = new RTMessage(RtMessageId.SERVER_APP, msg.toBytes());

		byte[] finalPayload = packet.toBytes();

		logger.finer(String.format("Sending %s:%d: ", getIPAddress(), getPort(), msg.toString()));
		logger.finest(String.format("Sending %s:%d byte payload: ", getIPAddress(), getPort(), Utils.bytesToHex(finalPayload)));
		this.sendMessage(packet);
	}

	/**
	 * Disconnect this client.
	 */
	public void disconnect() {
		socketChannel.flush();
		socketChannel.disconnect();
		socketChannel.close();
	}

	/**
	 * This method is called when the client socket closes.
	 */
	private void onDisconnect() {

		// Tell medius logic handler that this player disconnected
		if (server.getEmulationMode() == EmulationMode.MEDIUS_LOBBY_SERVER) {
			((MediusLobbyServer) server).updatePlayerStatus(player, MediusPlayerStatus.MEDIUS_PLAYER_DISCONNECTED);
		}

		logger.fine(String.format("Client disconnected: %s:%d", getIPAddress(), getPort()));
		server.removeClient(this);
	}

	private void generateRc4Key(CipherContext context) {
		Random rng = new Random();
		byte[] randomBytes = new byte[64];
		rng.nextBytes(randomBytes);
		rc4Key = new PS2_RC4(randomBytes, context);
	}

}
