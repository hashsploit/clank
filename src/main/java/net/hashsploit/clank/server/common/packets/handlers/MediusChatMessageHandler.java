package net.hashsploit.clank.server.common.packets.handlers;

import java.util.HashSet;

import net.hashsploit.clank.server.ChatColor;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.common.MediusConstants;
import net.hashsploit.clank.server.common.MediusLobbyServer;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.packets.serializers.ChatFwdMessageResponse;
import net.hashsploit.clank.server.common.packets.serializers.ChatMessageRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusChatMessageHandler extends MediusPacketHandler {

	private ChatMessageRequest requestPacket;

	public MediusChatMessageHandler() {
		super(MediusMessageType.ChatMessage, MediusMessageType.ChatFwdMessage);
	}

	@Override
	public void read(MediusMessage mm) {
		requestPacket = new ChatMessageRequest(mm.getPayload());
		logger.finest(requestPacket.getDebugString());
	}

	@Override
	public void write(MediusClient client) {

		String username = client.getPlayer().getUsername();
		String chatMsg = Utils.bytesToStringClean(requestPacket.getText());
		
		// FIXME: sanitize input, check for color parsing, check for muted, etc.
		
		logger.finest(username + ": " + chatMsg);
		
		// This should be ChatColor.strip() unless the player is an operator.
		byte[] byteMsg = Utils.padByteArray(ChatColor.parse(chatMsg), MediusConstants.CHATMESSAGE_MAXLEN.getValue());

		ChatFwdMessageResponse responsePacket = new ChatFwdMessageResponse(requestPacket.getMessageId(), Utils.buildByteArrayFromString(username, MediusConstants.USERNAME_MAXLEN.getValue()), byteMsg);

		int playerWorldId = client.getPlayer().getChatWorldId();
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		HashSet<Player> playersInWorld = server.getLobbyWorldPlayers(playerWorldId);

		for (Player player : playersInWorld) {
			// Send the message to everyone but yourself.
			if (player != client.getPlayer()) {
				player.getClient().sendMediusMessage(responsePacket);
			}
		}
	}

}
