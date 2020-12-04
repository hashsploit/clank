package net.hashsploit.clank.server.common;

import java.util.ArrayList;

import net.hashsploit.clank.server.common.packets.serializers.CreateGameOneRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusGame {
	
	private int worldId;
	private CreateGameOneRequest req;
	private int playerCount = 1;
	
	private int worldStatus = 1;
	
	private ArrayList<String> players = new ArrayList<String>();

	public MediusGame(int worldId, CreateGameOneRequest req) {
		this.worldId = worldId;
		this.req = req;
	}
	
	public CreateGameOneRequest getReqPacket() {
		return req;
	}

	public byte[] getPlayerCount() {
		return Utils.intToBytesLittle(playerCount);
	}

	public byte[] getStats() {
		// TODO Auto-generated method stub
		return Utils.hexStringToByteArray("00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
	}

	public byte[] getWorldStatus() {
		// TODO Auto-generated method stub
		return Utils.intToBytesLittle(worldStatus);
	}

	public byte[] getWorldId() {
		// TODO Auto-generated method stub
		return Utils.intToBytesLittle(worldId);
	}
	
	
}
