package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.MediusGame;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusApplicationType;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.medius.serializers.FindPlayerRequest;
import net.hashsploit.clank.server.medius.serializers.FindPlayerResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusFindPlayerHandler extends MediusPacketHandler {

	private FindPlayerRequest reqPacket;
	private FindPlayerResponse respPacket;

	public MediusFindPlayerHandler() {
		super(MediusMessageType.FindPlayer, MediusMessageType.FindPlayerResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage message) {
		reqPacket = new FindPlayerRequest(message.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		MediusLobbyServer server = (MediusLobbyServer) client.getServer();

		Player p = server.getPlayer(Utils.bytesToIntLittle(reqPacket.getAccountId()));
		
		byte[] applicationType;
		byte[] worldId;
		byte[] callbackStatus;
		
		
		if (p == null) {
			callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.NO_RESULT.getValue());
			applicationType = Utils.intToBytesLittle(MediusApplicationType.MEDIUS_APP_TYPE_GAME.value);
			worldId = Utils.intToBytesLittle(-1);
		}
		else {
			callbackStatus = Utils.intToBytesLittle(MediusCallbackStatus.SUCCESS.getValue());
			MediusPlayerStatus status = p.getStatus();
			if (status == MediusPlayerStatus.MEDIUS_PLAYER_IN_GAME_WORLD) {
				applicationType = Utils.intToBytesLittle(MediusApplicationType.MEDIUS_APP_TYPE_GAME.value);

				MediusGame game = server.getGameFromPlayer(p);
				if (game == null) {
					worldId = Utils.intToBytesLittle(-1);
				}
				else {
					worldId = Utils.intToBytesLittle(game.getWorldId());
				}
			}
			else if (status == MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD) {
				applicationType = Utils.intToBytesLittle(MediusApplicationType.LOBBY_CHAT_CHANNEL.value);
				worldId = Utils.intToBytesLittle(p.getChatWorldId());
			}
			else {
				applicationType = Utils.intToBytesLittle(MediusApplicationType.LOBBY_CHAT_CHANNEL.value);
				worldId = Utils.intToBytesLittle(-1);
			}
		}

		byte[] applicationId = Utils.hexStringToByteArray("00000000");
		byte[] applicationName = Utils.buildByteArrayFromString("", MediusConstants.APPNAME_MAXLEN.value);

		byte[] accountID = reqPacket.getAccountId();
		byte[] accountName = Utils.buildByteArrayFromString("Test name", MediusConstants.ACCOUNTNAME_MAXLEN.value);
		byte[] endOfList = Utils.intToBytesLittle(1);

		respPacket = new FindPlayerResponse(reqPacket.getMessageID(), callbackStatus, applicationId, applicationName, applicationType, worldId, accountID,
				reqPacket.getAccountName(), endOfList);
		
		List<MediusMessage> responses = new ArrayList<MediusMessage>();
		responses.add(respPacket);
		return responses;
	}

}
