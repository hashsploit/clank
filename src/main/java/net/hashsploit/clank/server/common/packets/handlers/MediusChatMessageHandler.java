package net.hashsploit.clank.server.common.packets.handlers;

import java.nio.ByteBuffer;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.objects.MediusChatMessageType;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.ChatMessageRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroRequest;
import net.hashsploit.clank.server.common.packets.serializers.GameInfoZeroResponse;
import net.hashsploit.clank.server.common.packets.serializers.GenericChatFwdMessageResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusChatMessageHandler extends MediusPacketHandler {

	private ChatMessageRequest reqPacket;
	private GenericChatFwdMessageResponse respPacket;

	public MediusChatMessageHandler() {
		super(MediusMessageType.ChatMessage, MediusMessageType.GenericChatFwdMessage);
	}

	@Override
	public void read(MediusMessage mm) {
		reqPacket = new ChatMessageRequest(mm.getPayload());

		logger.finest(reqPacket.getDebugString());

	}

	@Override
	public MediusMessage write(MediusClient client) {

		int timestamp = 0;
		int senderAccountId = 0;
		MediusChatMessageType mediusChatMessageType = MediusChatMessageType.BROADCAST;
		byte[] accountName = "DanBot".getBytes();
		byte[] message = "this is a test string".getBytes();

		respPacket = new GenericChatFwdMessageResponse(timestamp, senderAccountId, mediusChatMessageType, accountName, message);

		return null;
	}

}
