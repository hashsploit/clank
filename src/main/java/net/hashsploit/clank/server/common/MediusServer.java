package net.hashsploit.clank.server.common;

import java.util.HashMap;

import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.MediusClientChannelInitializer;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.TcpServer;
import net.hashsploit.clank.utils.MediusMessageMapInitializer;

/**
 * A generic Medius Server (TCP)
 * 
 * @author hashsploit
 *
 */
public class MediusServer extends TcpServer {
	
	private final EmulationMode emulationMode;
	private final HashMap<MediusMessageType, MediusPacketHandler> mediusMessageMap;
	
	public MediusServer(final EmulationMode emulationMode, final String address, final int port, final int parentThreads, final int childThreads) {
		super(address, port, parentThreads, childThreads);
		this.emulationMode = emulationMode;
		this.mediusMessageMap = MediusMessageMapInitializer.getMap();
		
		setChannelInitializer(new MediusClientChannelInitializer(this));
	}
	
	@Override
	public void start() {
		super.start();
	}
	
	
	@Override
	public void stop() {
		for (IClient c : getClients()) {
			if (c instanceof MediusClient) {
				final MediusClient client = (MediusClient) c;
				client.sendMessage(new DataPacket(RTPacketId.CLIENT_DISCONNECT, null));
				client.disconnect();
			}
		}
		super.stop();
	}
	
	/**
	 * Get the server's emulation mode type.
	 * @return
	 */
	public EmulationMode getEmulationMode() {
		return emulationMode;
	}
	
	public HashMap<MediusMessageType, MediusPacketHandler> getMediusMessageMap() {
		return mediusMessageMap;
	}
	
}
