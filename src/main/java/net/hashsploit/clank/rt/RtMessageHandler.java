package net.hashsploit.clank.rt;

import java.util.List;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusPacketHandler;

public abstract class RtMessageHandler {
	
	protected static final Logger logger = Logger.getLogger(MediusPacketHandler.class.getName());
	private final RtMessageId id;
	
	public RtMessageHandler(final RtMessageId id) {
		this.id = id;
	}

	/**
	 * Get the RT message type.
	 * @return
	 */
	public RtMessageId getId() {
		return id;
	}
	
	/**
	 * This method is called on when a message should be handled to be read by the server.
	 * @param buffer
	 */
	public abstract void read(final ByteBuf buffer);
	
	/**
	 * This method is called on when a message should be sent back to the client.
	 * @return 
	 */
	public abstract List<RTMessage> write(MediusClient client);
	
}
