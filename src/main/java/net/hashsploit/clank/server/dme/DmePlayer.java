package net.hashsploit.clank.server.dme;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.medius.objects.DmePlayerStatus;

public class DmePlayer {
	private static final Logger logger = Logger.getLogger(DmeWorldManager.class.getName());

	private String mlsToken;
	private int playerId;
	
	private DmeTcpClient client;
	private DatagramChannel udpChannel;
	private InetSocketAddress udpAddress;
	private int aggTime = 30; // in ms
	private float lastSendTime;
	private ConcurrentLinkedQueue<byte[]> packetQueue;
	private DmePlayerStatus status = DmePlayerStatus.DISCONNECTED;

	public String toString() {
		return "DmePlayer: \n" + 
				"MlsToken: " + mlsToken + "\n" +
				"PlayerId: " + Integer.toString(playerId) + "\n" +
				"Status: " + status.toString() + "\n" ;
	}
	
	
	public DmePlayer(DmeTcpClient client) {
		packetQueue = new ConcurrentLinkedQueue<byte[]>();
		status = DmePlayerStatus.CONNECTING;
		this.client = client;
	}
	
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void fullyConnected() {
		status = DmePlayerStatus.STAGING;
	}


	public void sendData(byte[] arr) {
		this.client.getSocket().writeAndFlush(Unpooled.copiedBuffer(arr));
	}

	public DmePlayerStatus getStatus() {
		return status;
	}

	public void setUdpConnection(DatagramChannel udpChannel, InetSocketAddress playerUdpAddr) {
		this.udpChannel = udpChannel;
		this.udpAddress = playerUdpAddr;
	}

	public void sendUdpData(byte[] payload) {
		// Uncommenting this WORKS
		//udpChannel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(payload), udpAddress));
		packetQueue.add(payload);
	}

	public void flushUdpData() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int qSize = packetQueue.size();
		
		if (qSize == 0) {
			return;
		}

		// Gets an instance of the queue size, removes that # of packets (so even if
		// more are added,
		// they won't be pop'd until next call)
		try {
			for (int i = 0; i < qSize; i++) {
				out.write(packetQueue.poll());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.fine("PUSHING UDP DATA ON CLIENT: " + Integer.toString(playerId));
		udpChannel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(out.toByteArray()), udpAddress));
	}

	public DmeTcpClient getClient() {
		return client;
	}

	public InetSocketAddress getUdpAddr() {
		return udpAddress;
	}


	public void setMlsToken(String mlsToken) {
		this.mlsToken = mlsToken;
	}


	public String getMlsToken() {
		return mlsToken;
	}

}
