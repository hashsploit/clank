package net.hashsploit.clank.server.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.server.ChatColor;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.MediusClientChannelInitializer;
import net.hashsploit.clank.server.MediusLogicHandler;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RTMessageId;
import net.hashsploit.clank.server.TcpServer;
import net.hashsploit.clank.server.scert.objects.RTMsgEncodingType;
import net.hashsploit.clank.server.scert.objects.RTMsgLanguageType;
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
	private final MediusLogicHandler logicHandler = new MediusLogicHandler();
	
	public MediusServer(final EmulationMode emulationMode, final String address, final int port, final int parentThreads, final int childThreads) {
		super(address, port, parentThreads, childThreads);
		this.emulationMode = emulationMode;
		
		if (emulationMode == EmulationMode.MEDIUS_AUTHENTICATION_SERVER) {
			this.mediusMessageMap = MediusMessageMapInitializer.getMasMap();	
		}
		else if (emulationMode == EmulationMode.MEDIUS_LOBBY_SERVER) {
			this.mediusMessageMap = MediusMessageMapInitializer.getMlsMap();	
		}
		else {
			this.mediusMessageMap = null;
		}
		setChannelInitializer(new MediusClientChannelInitializer(this));
	}
	
	
	@Override
	public void start() {
		super.start();
	}
	
	
	@Override
	public void stop() {
		
		sendSystemBroadcastMessage(255, "The server was closed. You have been disconnected.");
		
		for (IClient c : getClients()) {
			if (c instanceof MediusClient) {
				final MediusClient client = (MediusClient) c;
				client.sendMessage(new RTMessage(RTMessageId.CLIENT_DISCONNECT, null));
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
	
	/**
	 * Broadcast a Medius SYSTEM_MESSAGE to all clients connected.
	 * 
	 * @param severity An integer anywhere between 0-255 inclusive.
	 * @param message A string of text to send to the client.
	 */
	public void sendSystemBroadcastMessage(int severity, String message) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write((byte) severity); // Severity 255 (1 byte)
			baos.write(RTMsgEncodingType.RT_MSG_ENCODING_UTF8.getValue()); // Encoding type (1 byte)
			baos.write(RTMsgLanguageType.RT_MSG_LANGUAGE_US_ENGLISH.getValue()); // Language type (1 byte)
			baos.write((byte) 1); // EndOfList boolean (1 byte)
			baos.write(ChatColor.parse(message));
			baos.write((byte) 0x00);
		} catch (IOException e) {}
		
		for (IClient c : getClients()) {
			if (c instanceof MediusClient) {
				final MediusClient client = (MediusClient) c;
				client.sendMessage(new RTMessage(RTMessageId.SERVER_SYSTEM_MESSAGE, baos.toByteArray()));
			}
		}
	}
	
	public HashMap<MediusMessageType, MediusPacketHandler> getMediusMessageMap() {
		return mediusMessageMap;
	}

	public synchronized MediusLogicHandler getLogicHandler() {
		return logicHandler;
	}
	
}
