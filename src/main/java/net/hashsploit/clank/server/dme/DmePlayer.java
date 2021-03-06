package net.hashsploit.clank.server.dme;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.DatagramPacket;
import net.hashsploit.clank.server.medius.objects.DmePlayerStatus;
import net.hashsploit.medius.crypto.Utils;

public class DmePlayer {
	private static final Logger logger = Logger.getLogger(DmeWorldManager.class.getName());

	private String mlsToken;
	private int playerId;
	private int worldId;
	
	private DmeTcpClient client;
	private DatagramChannel udpChannel;
	private InetSocketAddress udpAddress;
	private LinkedBlockingQueue<byte[]> udpPacketQueue;
	private LinkedBlockingQueue<byte[]> tcpPacketQueue;
	private Thread flushThread;
	private DmePlayerStatus status = DmePlayerStatus.DISCONNECTED;

	public String toString() {
		return "DmePlayer (" + "SessionKey: " + mlsToken + ", " + "UdpAddr: " + udpAddress + ", " + "DmePlayerId: " + Integer.toString(playerId) + ", " + "Status: " + status.toString() + ")";
	}

	public DmePlayer(DmeTcpClient client) {
		udpPacketQueue = new LinkedBlockingQueue<byte[]>();
		tcpPacketQueue = new LinkedBlockingQueue<byte[]>();
		status = DmePlayerStatus.CONNECTING;
		this.client = client;
		
		Runnable flushThreadRunner = new Runnable() {
			public void run() {
				try {
					while(true) {
						Thread.sleep(30);
						flushTcpData();
						flushUdpData();
					}
				}
				catch (InterruptedException e) {
					logger.info("Closing flush thread for client " + toString());
					return;
				}
				
			}
		};
		
		flushThread = new Thread(flushThreadRunner);
		flushThread.setDaemon(true);
		flushThread.start();
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
		tcpPacketQueue.add(arr);
	}
	
	public void sendDataNow(byte[] arr) {
		logger.fine("PUSHING TCP DATA TO CLIENT NOW: " + Integer.toString(playerId) + " | " + Utils.bytesToHex(arr));
		this.client.getSocket().writeAndFlush(Unpooled.copiedBuffer(arr));
	}

	public DmePlayerStatus getStatus() {
		return status;
	}
	
	public void flushTcpData() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		int qSize = tcpPacketQueue.size();
				
		if (qSize == 0) {
			return;
		}

		logger.finest("Flushing TCP data size: " + qSize + " player: " + playerId);
		
		// Gets an instance of the queue size, removes that # of packets (so even if
		// more are added,
		// they won't be pop'd until next call)
		try {
			for (int i = 0; i < qSize; i++) {
				out.write(tcpPacketQueue.poll());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		logger.fine("PUSHING TCP DATA TO CLIENT: " + Integer.toString(playerId));
		this.client.getSocket().writeAndFlush(Unpooled.copiedBuffer(out.toByteArray()));
	}

	public void setUdpConnection(DatagramChannel udpChannel, InetSocketAddress playerUdpAddr) {
		this.udpChannel = udpChannel;
		this.udpAddress = playerUdpAddr;
	}

	public void sendUdpData(byte[] payload) {
		// Uncommenting this WORKS
		//udpChannel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(payload), udpAddress));
		udpPacketQueue.add(payload);
	}

	public void flushUdpData() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int qSize = udpPacketQueue.size();

		if (qSize == 0) {
			return;
		}

		logger.finest("Flushing UDP data size: " + qSize + " player: " + playerId);

		// Gets an instance of the queue size, removes that # of packets (so even if
		// more are added,
		// they won't be pop'd until next call)
		try {
			for (int i = 0; i < qSize; i++) {
				out.write(udpPacketQueue.poll());
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


	public int getWorldId() {
		return worldId;
	}


	public void setWorldId(int worldId) {
		this.worldId = worldId;
	}
	
	public void disconnect() {
		flushThread.interrupt();
	}

}
