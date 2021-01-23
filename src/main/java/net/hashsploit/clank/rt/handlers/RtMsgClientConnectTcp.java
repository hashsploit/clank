package net.hashsploit.clank.rt.handlers;

import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.util.Arrays;

import io.netty.buffer.ByteBuf;
import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.rt.RtMessageHandler;
import net.hashsploit.clank.rt.serializers.RT_ClientConnectTcp;
import net.hashsploit.clank.rt.serializers.RT_ServerConnectAcceptTcp;
import net.hashsploit.clank.rt.serializers.RT_ServerConnectComplete;
import net.hashsploit.clank.rt.serializers.RT_ServerCryptKeyGame;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.Player;
import net.hashsploit.clank.server.RTMessage;
import net.hashsploit.clank.server.RtMessageId;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.medius.objects.MediusPlayerStatus;
import net.hashsploit.clank.utils.Utils;
import net.hashsploit.medius.crypto.CipherContext;
import net.hashsploit.medius.crypto.rc.PS2_RC4;

public class RtMsgClientConnectTcp extends RtMessageHandler {

	private RT_ClientConnectTcp reqPacket;
	
	public RtMsgClientConnectTcp() {
		super(RtMessageId.CLIENT_CONNECT_TCP);
	}

	@Override
	public void read(ByteBuf buffer) {
		reqPacket = new RT_ClientConnectTcp(buffer);		
	}
	
	@Override
	public List<RTMessage> write(MediusClient client) {
		
		// if there is an access token, set that player to be initialized from simdb
		// TODO: fix later with session key taken from playerupdatestatus
		if (reqPacket.getAccessToken() != null) {
			String mlsAccessToken = Utils.bytesToStringClean(reqPacket.getAccessToken()).toLowerCase() + "\0";
			boolean mlsTokenValidated = Clank.getInstance().getDatabase().validateMlsAccessToken(mlsAccessToken);
			
			if (!mlsTokenValidated) {
				logger.severe("Attempted connection from " + client.getIPAddress() + " with invalid MLS access token: " + mlsAccessToken);
				client.disconnect();
				return new ArrayList<RTMessage>();
			}
			
			logger.info("Client MLS access token validated. " + client.getIPAddress() + " client connected.");
//			byte[] accessTokenTemp = reqPacket.getAccessToken();
//			byte[] accessToken = Arrays.copyOf(accessTokenTemp, 16); // BUG, fix this later should be 17, but hard coded in simdb
//			String accessTokenStr = Utils.bytesToHex(accessToken);
			
			
			
			
			client.setPlayer(new Player(client, MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD));
			client.getPlayer().setChatWorld(reqPacket.getTargetWorldId());
//			int accountId = Clank.getInstance().getDatabase().getAccountIdFromMlsToken(accessTokenStr);
//			client.getPlayer().setAccountId(accountId);
//			client.getPlayer().setUsername(Clank.getInstance().getDatabase().getUsername(accountId));
//			
//			MediusLobbyServer mlsServer = (MediusLobbyServer) client.getServer();
//			mlsServer.updatePlayerStatus(client.getPlayer(), MediusPlayerStatus.MEDIUS_PLAYER_IN_CHAT_WORLD);
		}

		
		List<RTMessage> responses = new ArrayList<RTMessage>();
		
		
		if (client.getServer().getEmulationMode() == EmulationMode.MEDIUS_AUTHENTICATION_SERVER ||
			client.getServer().getEmulationMode() == EmulationMode.MEDIUS_LOBBY_SERVER ||
			client.getServer().getEmulationMode() == EmulationMode.MEDIUS_UNIVERSE_INFORMATION_SERVER) {
			MediusConfig cfg = (MediusConfig) Clank.getInstance().getConfig();
			
			if (cfg.isEncrypted()) { // Encryption is enabled				
				// CRYPT KEY GAME
				byte[] generatedServerCryptKey = Utils.hexStringToByteArray("59F5AECD5CCD444855688FAE31C271C402BDB6C090843B693644840675FBCD4DD82C6011FE3CDEDEF949B0A32DFBAE6E086538055C7B7E5C9A891357E41C8C07");
				PS2_RC4 rc4serverCryptKey = new PS2_RC4(generatedServerCryptKey, CipherContext.RC_CLIENT_SESSION);
				client.setRC4ServerSessionKey(rc4serverCryptKey);
				
				// Build the serializer for ServerCryptKeyGame
				RT_ServerCryptKeyGame serverCryptKeyResponse = RT_ServerCryptKeyGame.build(generatedServerCryptKey);
				responses.add(serverCryptKeyResponse);
			}
			
			// TCP CONNECT ACCEPT
			RT_ServerConnectAcceptTcp serverConnectAcceptTcp = RT_ServerConnectAcceptTcp.build((short) 0x0801, 0x01000010, (byte) 0x00, client.getIPAddress());
			responses.add(serverConnectAcceptTcp);
			
			// TCP CONNECT COMPLETE
			RT_ServerConnectComplete serverConnectComplete = RT_ServerConnectComplete.build((short) 0x0001);
			responses.add(serverConnectComplete);
			
		} else if (client.getServer().getEmulationMode() == EmulationMode.DME_SERVER) {
			DmeConfig cfg = (DmeConfig) Clank.getInstance().getConfig();
		}

		return responses;
	}
	
}
