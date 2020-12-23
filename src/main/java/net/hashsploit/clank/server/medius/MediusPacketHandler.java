package net.hashsploit.clank.server.medius;

import java.util.List;
import java.util.logging.Logger;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.objects.MediusMessage;

public abstract class MediusPacketHandler {

	protected static final Logger logger = Logger.getLogger(MediusPacketHandler.class.getName());
	
	public final MediusMessageType type;
	public final MediusMessageType responseType;

	public MediusPacketHandler(final MediusMessageType type, final MediusMessageType responseType) {
		this.type = type;
		this.responseType = responseType;
	}

	public MediusMessageType getType() {
		return type;
	}

	/**
	 * This method is called on when a message should be handled to be read by the server.
	 * @param message
	 */
	public abstract void read(final MediusClient client, final MediusMessage message);

	/**
	 * This method is called on when a message should be sent back to the client.
	 * @param client
	 */
	public abstract List<MediusMessage> write(final MediusClient client);
	
}
