package net.hashsploit.clank.rt;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusPacketHandler;

public abstract class RtMessageHandler {
	
	protected static final Logger logger = Logger.getLogger(MediusPacketHandler.class.getName());
	protected final MediusClient client;
	private final RtMessageId id;
	
	public RtMessageHandler(final MediusClient client, final RtMessageId id) {
		this.client = client;
		this.id = id;
	}
	
	public MediusClient getClient() {
		return client;
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
	 * @param buffer
	 */
	public abstract void write(final ByteBuf buffer);
	
}
