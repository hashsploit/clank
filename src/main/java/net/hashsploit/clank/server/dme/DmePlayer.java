package net.hashsploit.clank.server.dme;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.PriorityBlockingQueue;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.common.objects.DmePlayerStatus;

public class DmePlayer {

	private int playerId;

	// TCP
	private SocketChannel socket;
	// UDP
	private Channel playerUdpChannel;
	private InetSocketAddress playerUdpAddr;
	private int aggTime = 30; // in ms
	private float lastSendTime;
	private PriorityBlockingQueue<byte[]> packetQueue;

	public DmePlayer() {
		packetQueue = new PriorityBlockingQueue<byte[]>();
		
	}

	private DmePlayerStatus status = DmePlayerStatus.DISCONNECTED;

	public DmePlayer(int playerId, SocketChannel socket) {
		status = DmePlayerStatus.CONNECTING;
		this.playerId = playerId;
		this.socket = socket;
	}

	public void fullyConnected() {
		status = DmePlayerStatus.CONNECTED;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void sendData(byte[] arr) {
		// TODO Auto-generated method stub
		socket.writeAndFlush(Unpooled.copiedBuffer(arr));
	}

	public DmePlayerStatus getStatus() {
		return status;
	}

	public void setUdpConnection(Channel playerUdpChannel, InetSocketAddress playerUdpAddr) {
		this.playerUdpChannel = playerUdpChannel;
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

		playerUdpChannel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(out.toByteArray()), playerUdpAddr));
	}

}
