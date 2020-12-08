package net.hashsploit.clank.server.dme;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.PriorityBlockingQueue;

import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.common.objects.DmePlayerStatus;

public class DmePlayer {

	private int playerId;

	// TCP
	private SocketChannel tcpChannel;
	// UDP
	private DatagramChannel udpChannel;
	private InetSocketAddress playerUdpAddr;
	private int aggTime = 30; // in ms
	private float lastSendTime;
	private PriorityBlockingQueue<byte[]> packetQueue;

	public DmePlayer() {
		packetQueue = new PriorityBlockingQueue<byte[]>(512);
	}

	private DmePlayerStatus status = DmePlayerStatus.DISCONNECTED;

	public DmePlayer(int playerId, SocketChannel tcpChannel) {
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
		this.playerUdpAddr = playerUdpAddr;
	}

	public void sendUdpData(byte[] payload) {
		// playerUdpChannel.writeAndFlush(new
		// DatagramPacket(Unpooled.copiedBuffer(payload), playerUdpAddr));
		packetQueue.add(payload);
	}

	public void flushUdpData() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// Gets an instance of the queue size, removes that # of packets (so even if
		// more are added,
		// they won't be pop'd until next call)
		try {
			for (int i = 0; i < packetQueue.size(); i++) {
				out.write(packetQueue.poll());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		udpChannel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(out.toByteArray()), playerUdpAddr));
	}

}
