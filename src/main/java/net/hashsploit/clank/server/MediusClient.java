package net.hashsploit.clank.server;

import java.util.HashMap;
import java.util.logging.Logger;

import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.hashsploit.clank.database.DbManager;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusPacketType;
import net.hashsploit.clank.server.common.MediusServer;
import net.hashsploit.clank.server.pipeline.TestHandlerMAS;
import net.hashsploit.clank.server.pipeline.TestHandlerMLS;
import net.hashsploit.clank.utils.MediusPacketMapInitializer;
import net.hashsploit.clank.utils.Utils;

public class MediusClient implements IClient {

	private static final Logger logger = Logger.getLogger(MediusClient.class.getName());

	private HashMap<MediusPacketType, MediusPacketHandler> mediusPacketMap;

	private AbstractServer server;
	private SocketChannel socketChannel;
	private ClientState state;
	private boolean encrypted;

	private long unixConnectTime;
	private long txPacketCount;
	private long rxPacketCount;

	public MediusClient(MediusServer server, SocketChannel channel) {
		this.server = server;
		this.socketChannel = channel;
		this.state = ClientState.UNAUTHENTICATED_STAGE_1;
		this.encrypted = true;
		this.unixConnectTime = System.currentTimeMillis();
		this.txPacketCount = 0L;
		this.rxPacketCount = 0L;

		this.mediusPacketMap = MediusPacketMapInitializer.getMap();

		logger.info("Client connected: " + getIPAddress());

		// Check if the server is a Medius Server component.
		if (server instanceof MediusServer) {
			final MediusServer ms = (MediusServer) server;

			// If so, initialize the correct pipeline for it.
			switch (ms.getComponent()) {
			case MEDIUS_AUTHENTICATION_SERVER:
				channel.pipeline().addLast("MediusTestHandlerMAS", new TestHandlerMAS(this));
				break;
			case MEDIUS_LOBBY_SERVER:
				channel.pipeline().addLast("MediusTestHandlerMLS", new TestHandlerMLS(this));
				break;
			default:
				break;

			}

		}

		ChannelFuture closeFuture = channel.closeFuture();

		closeFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> future) throws Exception {
				onDisconnect();
			}
		});

	}

	protected SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public String getIPAddress() {
		return socketChannel.remoteAddress().getAddress().getHostAddress();
	}

	public byte[] getIPAddressAsBytes() {
		return getIPAddress().getBytes();
	}

	public int getPort() {
		return socketChannel.remoteAddress().getPort();
	}

	public ClientState getClientState() {
		return state;
	}

	protected void setClientState(ClientState state) {
		this.state = state;
	}

	protected void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
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

	public void sendRaw(byte[] data) {
		socketChannel.writeAndFlush(data).awaitUninterruptibly();
		txPacketCount++;
	}

	/**
	 * Gracefully disconnect this client.
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
		logger.info("Client disconnect: " + socketChannel.remoteAddress());
		server.removeClient(this);
	}

	public void sendMessage(DataPacket msg) {
		if (msg instanceof EncryptedDataPacket) {

			// TODO: handle encryption
			final EncryptedDataPacket edp = (EncryptedDataPacket) msg;

		} else {
			final DataPacket pdp = (DataPacket) msg;
			final byte[] data = pdp.toBytes();
			logger.fine("Sending: " + Utils.bytesToString(data));
			sendRaw(data);
		}
	}

	public final HashMap<MediusPacketType, MediusPacketHandler> getMediusMap() {
		return mediusPacketMap;
	}

}
