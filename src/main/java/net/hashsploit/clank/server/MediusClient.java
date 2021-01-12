package net.hashsploit.clank.server;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.rt.RtPacketMap;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.MediusServer;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.pipeline.MasHandler;
import net.hashsploit.clank.server.pipeline.MlsHandler;
import net.hashsploit.clank.server.pipeline.RtDecryptionHandler;
import net.hashsploit.clank.server.pipeline.RtEncryptionHandler;
import net.hashsploit.clank.server.pipeline.RtFrameDecoderHandler;
import net.hashsploit.clank.server.pipeline.RtFrameEncoderHandler;
import net.hashsploit.clank.server.pipeline.TestHandlerMLS;
import net.hashsploit.clank.utils.MediusMessageMapInitializer;
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
	private PS2_RC4 rc4ClientSessionKey;
	private PS2_RC4 rc4ServerSessionKey;
	
	private Player player;
	
	protected HashMap<MediusMessageType, MediusPacketHandler> mediusMessageMap;
	protected HashMap<RtMessageId, RtMessageHandler> rtMessageMap;

	public MediusClient(MediusServer server, SocketChannel channel) {
		this.server = server;
		this.socketChannel = channel;
		this.state = ClientState.UNAUTHENTICATED;
		this.encrypted = true;
		this.unixConnectTime = System.currentTimeMillis();
		this.txPacketCount = 0L;
		this.rxPacketCount = 0L;
		this.player = new Player(this, MediusPlayerStatus.MEDIUS_PLAYER_IN_AUTH_WORLD);

		if (server.getEmulationMode() == EmulationMode.MEDIUS_AUTHENTICATION_SERVER) {
			this.mediusMessageMap = MediusMessageMapInitializer.getMasMap();
		}
		else if (server.getEmulationMode() == EmulationMode.MEDIUS_LOBBY_SERVER) {
			this.mediusMessageMap = MediusMessageMapInitializer.getMlsMap();
		}
		this.rtMessageMap = RtPacketMap.buildRtPacketMap();
		
		logger.fine(String.format("Client connected: %s:%d", getIPAddress(), getPort()));

		// 1
		// Decode the packet into frames (1)
		channel.pipeline().addLast(new RtFrameDecoderHandler(ByteOrder.LITTLE_ENDIAN, MediusConstants.MEDIUS_MESSAGE_MAXLEN.getValue(), 1, 2, 0, 0, false));

		// 2
		// Decrypt the packet (2)
		if (((MediusConfig) Clank.getInstance().getConfig()).isEncrypted()) {
			channel.pipeline().addLast(new RtDecryptionHandler(this));
		}

		// 3
		// Initialize the correct pipeline handler for this server (3)
		switch (server.getEmulationMode()) {
			case MEDIUS_AUTHENTICATION_SERVER:
				channel.pipeline().addLast(new MasHandler(this));
				break;
			case MEDIUS_LOBBY_SERVER:
				channel.pipeline().addLast(new MlsHandler(this));
				break;
			default:
				break;
		}

		// 5
		// Re-frame the packets (5)
		channel.pipeline().addLast(new RtFrameEncoderHandler(this));
		
		// 4
		// Re-encrypt the packet (4)
		if (((MediusConfig) Clank.getInstance().getConfig()).isEncrypted()) {
			channel.pipeline().addLast(new RtEncryptionHandler(this));
		}

		
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
	public void setPlayer(final Player player) {
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
	public void sendRaw(ByteBuf data) {
		socketChannel.pipeline().writeAndFlush(data);
		txPacketCount++;
	}

	/**
	 * Send a RT message to the client.
	 * 
	 * @param msg
	 */
	public void sendMessage(RTMessage msg) {
		sendRaw(msg.getFullMessage());
	}

	/**
	 * Send a Medius (RT SERVER_APP) message.
	 * 
	 * @param msg
	 */
	public void sendMediusMessage(MediusMessage msg) {
		RTMessage packet = new RTMessage(RtMessageId.SERVER_APP, msg.toBytes());

		logger.finer(String.format("Sending %s:%d: ", getIPAddress(), getPort(), msg.toString()));
		logger.finest(String.format("Sending %s:%d byte payload: ", getIPAddress(), getPort(), Utils.bytesToHex(Utils.nettyByteBufToByteArray(packet.getFullMessage()))));
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
	
	/**
	 * Set this client's app id.
	 * 
	 * @param applicationId
	 */
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	
	/**
	 * Get this client's app id.
	 * 
	 * @return
	 */
	public int getApplicationId() {
		return applicationId;
	}

	/**
	 * Set the Client RSA key.
	 * @param rsaKey
	 */
	public void setRSAKey(PS2_RSA rsaKey) {
		this.rsaKey = rsaKey;
	}

	/**
	 * Get the Client RSA key.
	 * @return
	 */
	public PS2_RSA getRSAKey() {
		return rsaKey;
	}

	/**
	 * GEt the RC4 Client Session Key.
	 * May be null if not initialized.
	 * @return
	 */
	public PS2_RC4 getRC4ClientSessionKey() {
		return rc4ClientSessionKey;
	}

	/**
	 * Set the RC4 Client Session Key.
	 * @param rc4Key
	 */
	public synchronized void setRC4ClientSessionKey(PS2_RC4 rc4Key) {
		this.rc4ClientSessionKey = rc4Key;
	}

	/**
	 * Get the RC4 Server Session Key.
	 * May be null if not initialized.
	 * @return
	 */
	public PS2_RC4 getRC4ServerSessionKey() {
		return rc4ServerSessionKey;
	}

	/**
	 * Set the RC4 Server Session Key.
	 * @param rc4Key
	 */
	public synchronized void setRC4ServerSessionKey(PS2_RC4 rc4Key) {
		this.rc4ServerSessionKey = rc4Key;
	}
	
	public RtMessageHandler getRtHandler(byte byteIn) {
		RtMessageId rtId = RtMessageId.getIdByByte(byteIn);
		return rtMessageMap.get(rtId);
	}

	public HashMap<MediusMessageType, MediusPacketHandler> getMediusMessageMap() {
		return mediusMessageMap;
	}

}
