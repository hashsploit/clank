package net.hashsploit.clank.server.common;

import java.util.ArrayList;
import java.util.HashMap;

import io.netty.channel.ChannelHandlerContext;
import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;

public class MediusDMEWorldHandler {
	
	private final ArrayList<PlayerConnection> players = new ArrayList<PlayerConnection>();
	
	public ArrayList<PlayerConnection> getPlayersConnected() {		
		return players;
	}

	public void addPlayer(ChannelHandlerContext ctx) {
		players.add(new PlayerConnection(ctx));
	}
	
	public void send(byte[] data) {
		for (PlayerConnection c: players) {
			c.write(data);
		}
	}
	
}
