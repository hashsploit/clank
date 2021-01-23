package net.hashsploit.clank.server.medius.handlers;

import java.nio.ByteBuffer;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.database.DbManager;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.utils.Utils;

public class MediusUpdateUserStateHandler extends MediusPacketHandler {

	private byte[] sessionKey = new byte[MediusConstants.MESSAGEID_MAXLEN.value];

	public MediusUpdateUserStateHandler() {
		super(MediusMessageType.UpdateUserState, null);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		// 0b2e00019631007572426f4c74000000000000000000000000006664643462383630316537373731343400000000000000
		ByteBuffer buf = ByteBuffer.wrap(mm.getPayload());
		buf.get(sessionKey);

		logger.finest("UpdateUserState data read:");
		logger.finest("sessionKey: " + Utils.bytesToHex(sessionKey));
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		String generatedSessionKey = Utils.bytesToStringClean(sessionKey) + "\0";

		DbManager db = Clank.getInstance().getDatabase();
		int accountId = db.getAccountIdFromSessionKey(generatedSessionKey);
		if (accountId == 0) {
			client.disconnect();
			throw new IllegalStateException("User session key is not valid (" + generatedSessionKey + ")" + ". Disconnecting client.");
		}

		client.getPlayer().setAccountId(accountId);
		MediusLobbyServer mlsServer = (MediusLobbyServer) client.getServer();
		mlsServer.updatePlayerStatus(client.getPlayer(), MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD);

		logger.info("Player '" + client.getPlayer().getUsername() + "' has authenticated their session key: " + generatedSessionKey);
		client.getPlayer().setSessionKey(generatedSessionKey);

		return null;
	}

}
