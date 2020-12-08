package net.hashsploit.clank.server.dme;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.common.objects.DmePlayerStatus;

public class DmePlayer {
	private static final Logger logger = Logger.getLogger(DmeWorldManager.class.getName());

	private int playerId;
	private SocketChannel tcpChannel;
	private DatagramChannel udpChannel;
	private InetSocketAddress udpAddress;
	private int aggTime = 30; // in ms
	private float lastSendTime;
	private BlockingQueue<byte[]> packetQueue;
	private DmePlayerStatus status = DmePlayerStatus.DISCONNECTED;

	public DmePlayer(int playerId, SocketChannel tcpChannel) {
		packetQueue = new LinkedBlockingQueue<byte[]>(512);
		status = DmePlayerStatus.CONNECTING;
		this.playerId = playerId;
		this.tcpChannel = tcpChannel;
	}

	public void fullyConnected() {
		status = DmePlayerStatus.CONNECTED;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void sendData(byte[] arr) {
		// TODO Auto-generated method stub
		tcpChannel.writeAndFlush(Unpooled.copiedBuffer(arr));
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
				out.write(packetQueue.take());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		logger.fine("PUSHING UDP DATA ON CLIENT: " + Integer.toString(playerId));
		udpChannel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(out.toByteArray()), udpAddress));
	}

}
