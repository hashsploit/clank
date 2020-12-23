package net.hashsploit.clank.server.medius;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.server.ChatColor;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.MediusClientChannelInitializer;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.TcpServer;
import net.hashsploit.clank.server.dme.DmeServer;
import net.hashsploit.clank.server.rpc.AbstractRpcServer;
import net.hashsploit.clank.server.rpc.ClankMlsRpcServer;
import net.hashsploit.clank.server.rpc.RpcServerConfig;
import net.hashsploit.clank.server.scert.objects.RTMsgEncodingType;
import net.hashsploit.clank.server.scert.objects.RTMsgLanguageType;
import net.hashsploit.clank.utils.MediusMessageMapInitializer;
import net.hashsploit.clank.utils.Utils;

/**
 * A generic Medius Server (TCP)
 * 
 * @author hashsploit
 *
 */
public class MediusServer extends TcpServer {

	protected static final Logger logger = Logger.getLogger(MediusServer.class.getName());
	
	private final EmulationMode emulationMode;
	
	protected AbstractRpcServer rpcServer;
	protected HashMap<MediusMessageType, MediusPacketHandler> mediusMessageMap;

	public MediusServer(final EmulationMode emulationMode, final String address, final int port, final int parentThreads, final int childThreads) {
		super(address, port, parentThreads, childThreads);
		this.emulationMode = emulationMode;

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
				client.sendMessage(new RTMessage(RtMessageId.CLIENT_DISCONNECT, null));
				client.disconnect();
			}
		}
		
		if (rpcServer != null) {
			rpcServer.stop();
		}

		super.stop();
	}

	/**
	 * Get the server's emulation mode type.
	 * 
	 * @return
	 */
	public EmulationMode getEmulationMode() {
		return emulationMode;
	}

	/**
	 * Broadcast a Medius SYSTEM_MESSAGE to all clients connected.
	 * 
	 * @param severity An integer anywhere between 0-255 inclusive.
	 * @param message  A string of text to send to the client.
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
		} catch (IOException e) {
		}

		for (IClient c : getClients()) {
			if (c instanceof MediusClient) {
				final MediusClient client = (MediusClient) c;
				client.sendMessage(new RTMessage(RtMessageId.SERVER_SYSTEM_MESSAGE, baos.toByteArray()));
			}
		}
	}

	public HashMap<MediusMessageType, MediusPacketHandler> getMediusMessageMap() {
		return mediusMessageMap;
	}

	/**
	 * Get the RPC Server relating to this server.
	 * 
	 * @return
	 */
	public AbstractRpcServer getRpcServer() {
		return rpcServer;
	}

}
