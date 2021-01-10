package net.hashsploit.clank.rt.serializers;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusMessageType;

public class RT_ServerApp extends RTMessage {

	private final MediusMessageType type;
	private final byte[] mediusPayload;
	
	public RT_ServerApp(ByteBuf payload) {
		super(RtMessageId.SERVER_APP, payload.readableBytes(), payload);
		type = MediusMessageType.getTypeByShort(payload.readShortLE());
		mediusPayload = new byte[MediusConstants.MEDIUS_MESSAGE_MAXLEN.getValue()];
		payload.readBytes(mediusPayload, 0, payload.readableBytes());
	}
	
	/**
	 * Returns the Medius message type.
	 * @return
	 */
	public MediusMessageType getMediusMessageType() {
		return type;
	}
	
	/**
	 * Returns the Medius payload without the Medius message type.
	 * @return
	 */
	public byte[] getMediusPayload() {
		return mediusPayload;
	}

}
