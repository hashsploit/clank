package net.hashsploit.clank.server;

import java.util.logging.Logger;

import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.hashsploit.clank.server.pipeline.OldInboundPacketParser;
import net.hashsploit.clank.server.pipeline.TestHandler;
import net.hashsploit.clank.utils.Utils;

public class Client {
	
	private static final Logger logger = Logger.getLogger("");
	private final TestHandler mediusTestHandler;
	//private final OldInboundPacketParser mediusPacketParser;
	//private final InboundMessageHandler mediusClientHandler;
	
	
	private Server server;
	private SocketChannel socketChannel;
	private ClientState state;
	private boolean encrypted;
	
	private long unixConnectTime;
	private long txPacketCount;
	private long rxPacketCount;
	
	
	public Client(Server server, SocketChannel channel) {
		this.server = server;
		this.socketChannel = channel;
		this.state = ClientState.UNAUTHENTICATED_STAGE_1;
		this.encrypted = true;
		this.unixConnectTime = System.currentTimeMillis();
		this.txPacketCount = 0L;
		this.rxPacketCount = 0L;
		
		logger.info("Client connected: " + getIPAddress());
		
		this.mediusTestHandler = new TestHandler(this);
		//this.mediusPacketParser = new OldInboundPacketParser(this);
		//this.mediusClientHandler = new InboundMessageHandler(this);
		
		
		channel.pipeline().addLast("MediusTestHandler", mediusTestHandler);
		//channel.pipeline().addLast("MediusClientHandler", mediusClientHandler);
		
		
		
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
		return socketChannel.remoteAddress().getAddress().toString();
	}
	
	public byte[] getIPAddressAsBytes() {
		return socketChannel.remoteAddress().getAddress().getAddress();
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
	 * @return
	 */
	public boolean isEncrypted() {
		return encrypted;
	}
	
	/**
	 * Returns the UNIX timestamp of when the connection started.
	 * @return
	 */
	public long getUnixConnectTime() {
		return unixConnectTime;
	}
	
	/**
	 * Get the total number of received packets.
	 * @return
	 */
	public long getRxPacketCount() {
		return rxPacketCount;
	}
	
	/**
	 * Get the total number of transmitted packets.
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
			final byte[] data = pdp.toData().array();
			logger.fine("Sending: " + Utils.bytesToString(data));
			sendRaw(data);
		}
	}
	
	
	
	
	
	
}
