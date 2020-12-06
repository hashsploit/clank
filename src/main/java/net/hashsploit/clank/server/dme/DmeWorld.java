package net.hashsploit.clank.server.dme;

import java.util.HashMap;

import io.netty.buffer.Unpooled;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.utils.Utils;

public class DmeWorld {
	
	int curId = 0;
	HashMap<Integer, DmeTcpClient> clients = new HashMap<Integer, DmeTcpClient>();
	HashMap<DmeTcpClient, Integer> revClients = new HashMap<DmeTcpClient, Integer>();

	public int add(DmeTcpClient client) {
		// Send server notify to existing clients
		// then add our client
		this.sendServerNotify(curId);
		int thisId = curId;
		clients.put(thisId, client);
		revClients.put(client, thisId);
		curId += 1;
		return thisId;
	}
	
	public void clientAppSingle(DmeTcpClient cli, int id, byte[] payload) {
		int sourceId = revClients.get(cli);
		payload[3] = (byte) sourceId;
		clients.get(id).getSocket().writeAndFlush(Unpooled.copiedBuffer(payload));
	}
	
	public void broadcast(DmeTcpClient cli, byte[] payload) {
		for (DmeTcpClient c: clients.values()) {
			if (c != cli) {
				c.getSocket().writeAndFlush(Unpooled.copiedBuffer(payload));			
			}
		}
	}
	
	private void sendServerNotify(int id) {
		// build server notify packet
		String p1 = "085200";
		String p2 = Utils.bytesToHex(Utils.shortToBytesLittle((short) id));
		String p3 = "3139322e3136382e312e39390000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		String res = p1 + p2 + p3;
		
		for (DmeTcpClient c: clients.values()) {
			c.getSocket().writeAndFlush(Unpooled.copiedBuffer(Utils.hexStringToByteArray(res)));			
		}
	}

	public int getNewId() {
		return curId;
	}

}
