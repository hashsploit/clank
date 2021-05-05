package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.database.DbManager;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusConstants;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.medius.serializers.PlayerInfoRequest;
import net.hashsploit.clank.server.medius.serializers.PlayerInfoResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusPlayerInfoHandler extends MediusPacketHandler {

	private PlayerInfoRequest reqPacket;
	private PlayerInfoResponse respPacket;

	public MediusPlayerInfoHandler() {
		super(MediusMessageType.PlayerInfo, MediusMessageType.PlayerInfoResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new PlayerInfoRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {

		int accountId;
		if (Utils.bytesToHex(reqPacket.getAccountID()).equals("ffffffff")) {
			accountId = 0;
		} else {
			accountId = Utils.bytesToIntLittle(reqPacket.getAccountID());
		}

		DbManager db = Clank.getInstance().getDatabase();

		String accountName = db.getUsername(accountId);
		byte[] callbackStatus;
		byte[] accountNameArr;
		if (accountName == null) {
			callbackStatus = Utils.intToBytes(MediusCallbackStatus.SUCCESS.getValue()); // TODO: Fix this
			accountNameArr = Utils.buildByteArrayFromString("", MediusConstants.ACCOUNTNAME_MAXLEN.value);
		} else {
			callbackStatus = Utils.intToBytes(MediusCallbackStatus.SUCCESS.getValue());
			accountNameArr = Utils.buildByteArrayFromString(accountName, MediusConstants.ACCOUNTNAME_MAXLEN.value);
		}

		MediusLobbyServer server = (MediusLobbyServer) client.getServer();

		MediusPlayerStatus playerStatus = server.getPlayerStatus(accountId);
		byte[] stats = Utils.hexStringToByteArray(Clank.getInstance().getDatabase().getPlayerStats(accountId));
		
		byte[] appID = Utils.hexStringToByteArray("bc290000");
		byte[] playerStatusArr = Utils.intToBytesLittle(playerStatus.getValue());
		byte[] connectionClass = Utils.intToBytesLittle(1);
		
		respPacket = new PlayerInfoResponse(reqPacket.getMessageID(), callbackStatus, appID, accountNameArr, playerStatusArr, connectionClass, stats);

		// client.sendMessage(new RTMessage(RTMessageId.SERVER_APP,
		// Utils.hexStringToByteArray("013231000000000000000000000000000000000000000000000000000000426F6E6563727573686572000000000000000000000000000000000000000000BC2900000300000001000000401AD944CAF51C4450727C44C4448E440000C842000000000000C8425555854200000000AABB1200FA66F48A4815A2F100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")));

		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}

}
