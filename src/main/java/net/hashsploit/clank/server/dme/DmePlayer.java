package net.hashsploit.clank.server.dme;

import io.netty.buffer.Unpooled;
import io.netty.channel.socket.SocketChannel;
import net.hashsploit.clank.server.common.objects.DmePlayerStatus;

public class DmePlayer {

	private int playerId;
	
	private SocketChannel socket;
	
	private DmePlayerStatus status = DmePlayerStatus.DISCONNECTED;
	private int aggTime;

	
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
	
}
