package net.hashsploit.clank.server.medius.handlers;

import java.util.HashSet;
import java.util.List;

import net.hashsploit.clank.server.ChatColor;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusChatMessageType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.ChatFwdMessageResponse;
import net.hashsploit.clank.server.medius.serializers.ChatMessageRequest;
import net.hashsploit.clank.utils.Utils;

public class MediusChatMessageHandler extends MediusPacketHandler {

	private ChatMessageRequest requestPacket;

	public MediusChatMessageHandler() {
		super(MediusMessageType.ChatMessage, MediusMessageType.ChatFwdMessage);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		requestPacket = new ChatMessageRequest(mm.getPayload());

		logger.finest(requestPacket.getDebugString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		String username = client.getPlayer().getUsername();
		String chatMsg = Utils.bytesToStringClean(requestPacket.getText());

		// FIXME: sanitize input, check for color parsing, check for muted, etc.

		logger.finest("[CHAT] " + username + ": " + chatMsg);

		// This should be ChatColor.strip() unless the player is an operator.
		byte[] byteMsg = requestPacket.getText(); //Utils.padByteArray(ChatColor.parse(chatMsg), MediusConstants.CHATMESSAGE_MAXLEN.value);

		ChatFwdMessageResponse responsePacket = new ChatFwdMessageResponse(requestPacket.getMessageId(), client.getPlayer().getAccountId(), Utils.buildByteArrayFromString(username, MediusConstants.USERNAME_MAXLEN.value), byteMsg, requestPacket.getMessageType());
		int playerWorldId = client.getPlayer().getChatWorldId();
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();
		HashSet<Player> playersInWorld = server.getLobbyWorldPlayers(playerWorldId);
		
		if (requestPacket.getMessageType() == MediusChatMessageType.BROADCAST) {
			for (Player player : playersInWorld) {
				if (player != client.getPlayer()) {
					player.getClient().sendMediusMessage(responsePacket);
				}
			}
		}
		else if (requestPacket.getMessageType() == MediusChatMessageType.WHISPER) {
			Player p = server.getPlayer(requestPacket.getTargetId());
			if (p != null) {
				p.getClient().sendMediusMessage(responsePacket);
			}
		}
		else {
			logger.warning("Unimplemented MediusChatMessageType: " + requestPacket.getDebugString());
		}

		return null;
	}

}
