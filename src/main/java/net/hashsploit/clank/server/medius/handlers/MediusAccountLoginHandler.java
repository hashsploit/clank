package net.hashsploit.clank.server.medius.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.medius.MediusAuthenticationServer;
import net.hashsploit.clank.server.medius.MediusCallbackStatus;
import net.hashsploit.clank.server.medius.MediusMessageType;
import net.hashsploit.clank.server.medius.MediusPacketHandler;
import net.hashsploit.clank.server.medius.objects.MediusMessage;
import net.hashsploit.clank.server.medius.serializers.AccountLoginRequest;
import net.hashsploit.clank.server.medius.serializers.AccountLoginResponse;
import net.hashsploit.clank.server.rpc.PlayerLoginResponse;
import net.hashsploit.clank.utils.Utils;

public class MediusAccountLoginHandler extends MediusPacketHandler {
	
	private static final Logger logger = Logger.getLogger(MediusAccountLoginHandler.class.getName());

	private AccountLoginRequest reqPacket;
	private AccountLoginResponse respPacket;

	public MediusAccountLoginHandler() {
		super(MediusMessageType.AccountLogin, MediusMessageType.AccountLoginResponse);
	}

	@Override
	public void read(MediusClient client, MediusMessage mm) {
		reqPacket = new AccountLoginRequest(mm.getPayload());
		logger.finest(reqPacket.toString());
	}

	@Override
	public List<MediusMessage> write(MediusClient client) {
		
		byte[] callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.LOGIN_FAILED.getValue()));
		byte[] mlsToken = null;
		//byte[] mlsToken = Utils.hexStringToByteArray("12345678901234567890123456789012345678");
		                                              
		// TODO: Clean the username!!! add a utility to check if the username is valid, length, and characters in it.
		final String username = Utils.parseMediusString(reqPacket.getUsernameBytes());
		final String password = Utils.parseMediusString(reqPacket.getPasswordBytes());

		PlayerLoginResponse rpcResponse = ((MediusAuthenticationServer) client.getServer()).playerLogin(username, password);
		int playerAccountId = rpcResponse.getAccountId();
		boolean rpcSuccess = rpcResponse.getSuccess();
		String mlsTokenString = rpcResponse.getMlsToken();
		
		logger.info("PlayerLoginRPC AccountId: " + Integer.toString(playerAccountId));
		logger.info("PlayerLoginRPC success: " + rpcSuccess);
		logger.info("PlayerLoginRPC mlsTokenString: " + mlsTokenString);
		
		// TODO: Generate random auth token each connection, save in db for MLS to use.
		// db should have a expiration UNIX time-stamp as well that needs to be updated
		// by MLS.
		//new Random().nextBytes(mlsToken);
		
		// MLS Token is all 0s when failure happens
		
		// Last 3 bytes are padding
		mlsToken = Utils.hexStringToByteArray(mlsTokenString + "000000");
		
		mlsToken[mlsToken.length - 1] = (byte) 0x00;
		mlsToken[mlsToken.length - 2] = (byte) 0x00;
		mlsToken[mlsToken.length - 3] = (byte) 0x00;
	
		// If player login is successful, check if the username/pass is in whitelist also
		boolean whitelistSuccess = false;
		
		MasConfig masConfig;
		
		if (Clank.getInstance().getConfig() instanceof MasConfig) {
			masConfig = (MasConfig) Clank.getInstance().getConfig();	
			if (!masConfig.isWhitelistEnabled()) { // Whitelist is disabled
				whitelistSuccess = true;
			}
			else { // Whitelist is enabled. Check username/pass with whitelist
				HashMap<String, String> whitelist = masConfig.getWhitelist();
				if (whitelist.keySet().contains(username.toLowerCase())) {
					if (whitelist.get(username.toLowerCase()).equals(password)) {
						whitelistSuccess = true;
					}
				}
			}
		}
		
		logger.info("Whitelist success: " + whitelistSuccess);
		// gRPC Login success
		if (rpcSuccess && whitelistSuccess) {
			callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.SUCCESS.getValue()));
		} else {
			callbackStatus = Utils.intToBytesLittle((MediusCallbackStatus.INVALID_PASSWORD.getValue()));
		}
		
		byte[] accountID = Utils.intToBytesLittle(playerAccountId);
		byte[] accountType = Utils.intToBytesLittle(2);
		
		// FIXME: bad location
		//final LocationConfig location = client.getServer().getLogicHandler().getLocation();
		
		// Default world id = 0
		byte[] worldID = Utils.intToBytesLittle(0);
		String mlsIpAddress = (Clank.getInstance().getConfig() instanceof MasConfig) ? ((MasConfig) Clank.getInstance().getConfig()).getMlsAddress() : null;
		String natIpAddress = (Clank.getInstance().getConfig() instanceof MasConfig) ? ((MasConfig) Clank.getInstance().getConfig()).getNatAddress() : null;

		if (mlsIpAddress == null) {
			mlsIpAddress = Utils.getPublicIpAddress();
		}

		if (natIpAddress == null) {
			natIpAddress = Utils.getPublicIpAddress();
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
		
		
		List<MediusMessage> response = new ArrayList<MediusMessage>();
		response.add(respPacket);
		return response;
	}

}
