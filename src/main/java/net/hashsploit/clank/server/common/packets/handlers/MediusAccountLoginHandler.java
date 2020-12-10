package net.hashsploit.clank.server.common.packets.handlers;

import java.util.Random;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.common.MediusCallbackStatus;
import net.hashsploit.clank.server.common.MediusMessageType;
import net.hashsploit.clank.server.common.MediusPacketHandler;
import net.hashsploit.clank.server.common.objects.MediusMessage;
import net.hashsploit.clank.server.common.objects.MediusPlayerStatus;
import net.hashsploit.clank.server.common.packets.serializers.AccountLoginRequest;
import net.hashsploit.clank.server.common.packets.serializers.AccountLoginResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountLoginHandler extends MediusPacketHandler {

	private AccountLoginRequest reqPacket;
	private AccountLoginResponse respPacket;

	public MediusAccountLoginHandler() {
		super(MediusMessageType.AccountLogin, MediusMessageType.AccountLoginResponse);
	}

	@Override
	public void read(MediusMessage mm) {
		reqPacket = new AccountLoginRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public void write(MediusClient client) {
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.LOGIN_FAILED.getValue()));
		byte[] mlsToken = Utils.hexStringToByteArray("00000000000000000000000000000000000000");
		//byte[] mlsToken = Utils.hexStringToByteArray("12345678901234567890123456789012345678");
		                                              
		// TODO: Clean the username!!! add a utility to check if the username is valid, length, and characters in it.
		final String username = Utils.parseMediusString(reqPacket.getUsernameBytes());
		final String password = Utils.parseMediusString(reqPacket.getPasswordBytes());

		int playerAccountId = Clank.getInstance().getDatabase().getAccountId(username);
		
		if (Clank.getInstance().getDatabase().accountExists(username)) {
			if (Clank.getInstance().getDatabase().validateAccount(username, password)) {
				callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));

				// Add player to MLS Logic Handler (with accountId), update status to connected
				client.getPlayer().setUsername(username);
				client.getServer().getLogicHandler().updatePlayerStatus(client.getPlayer(), MediusPlayerStatus.MEDIUS_PLAYER_IN_AUTH_WORLD);
				
				// TODO: Generate random auth token each connection, save in db for MLS to use.
				// db should have a expiration UNIX time-stamp as well that needs to be updated
				// by MLS.
				//new Random().nextBytes(mlsToken);
				
				// Last 3 bytes are padding
				mlsToken = Utils.hexStringToByteArray(Clank.getInstance().getDatabase().getMlsToken(client.getPlayer().getAccountId()) + "000000");
				
				mlsToken[mlsToken.length - 1] = (byte) 0x00;
				mlsToken[mlsToken.length - 2] = (byte) 0x00;
				mlsToken[mlsToken.length - 3] = (byte) 0x00;

			} else {
				callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.INVALID_PASSWORD.getValue()));
			}
		}
		
		byte[] accountID = Utils.intToBytesLittle(playerAccountId);
		byte[] accountType = Utils.intToBytesLittle(2);
		byte[] worldID = Utils.intToBytesLittle(client.getServer().getLogicHandler().getLocationId());
		String mlsIpAddress = (Clank.getInstance().getConfig() instanceof MasConfig) ? ((MasConfig) Clank.getInstance().getConfig()).getMlsAddress() : null;
		String natIpAddress = (Clank.getInstance().getConfig() instanceof MasConfig) ? ((MasConfig) Clank.getInstance().getConfig()).getNatAddress() : null;

		if (mlsIpAddress == null) {
			mlsIpAddress = Utils.getPublicIpAddress();
		}

		if (natIpAddress == null) {
			mlsIpAddress = Utils.getPublicIpAddress();
		}

		byte[] mlsAddress = mlsIpAddress.getBytes();
		int mlsNumZeros = 16 - mlsIpAddress.length();
		String mlsZeroString = new String(new char[mlsNumZeros]).replace("\0", "00");
		byte[] mlsZeroTrail = Utils.hexStringToByteArray(mlsZeroString);

		byte[] natAddress = natIpAddress.getBytes();
		int natNumZeros = 16 - natIpAddress.length();
		String natZeroString = new String(new char[natNumZeros]).replace("\0", "00");
		byte[] natZeroTrail = Utils.hexStringToByteArray(natZeroString);

		respPacket = new AccountLoginResponse(reqPacket.getMessageID(), callbackStatus, accountID, accountType, worldID, mlsAddress, mlsZeroTrail, natAddress, natZeroTrail, mlsToken);
		client.sendMediusMessage(respPacket);
	}

}
