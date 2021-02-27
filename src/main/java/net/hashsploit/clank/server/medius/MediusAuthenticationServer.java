package net.hashsploit.clank.server.medius;

import java.util.Arrays;
import java.util.List;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.server.medius.objects.Channel;
import net.hashsploit.clank.server.medius.objects.Location;
import net.hashsploit.clank.server.rpc.ClankMasRpcClient;
import net.hashsploit.clank.server.rpc.PlayerLoginResponse;
import net.hashsploit.clank.server.rpc.RpcConfig;
import net.hashsploit.clank.utils.Utils;

public class MediusAuthenticationServer extends MediusServer {

	private final ClankMasRpcClient rpcClient;
	private int natPort;
	private int mlsPort;

	public MediusAuthenticationServer(String address, int port, int parentThreads, int childThreads) {
		super(EmulationMode.MEDIUS_AUTHENTICATION_SERVER, address, port, parentThreads, childThreads);
		
		// Start RPC client
		final RpcConfig config = ((MediusConfig) Clank.getInstance().getConfig()).getRpcConfig();
		String rpcAddress = config.getAddress();
		final int rpcPort = config.getPort();

		if (rpcAddress == null) {
			rpcAddress = Utils.getPublicIpAddress();
		}

		rpcClient = new ClankMasRpcClient(rpcAddress, rpcPort);

		if (Clank.getInstance().getConfig() instanceof MasConfig) {
			final MasConfig masConfig = (MasConfig) Clank.getInstance().getConfig();
			if (masConfig.isWhitelistEnabled()) {
				logger.info(String.format("Whitelisted users: %s", Arrays.toString(masConfig.getWhitelist().keySet().toArray())));
			}
			natPort = masConfig.getNatPort();
			mlsPort = masConfig.getMlsPort();
		}
	
	}
	
	
	public PlayerLoginResponse playerLogin(String username, String password, String sessionKey) {
		return rpcClient.loginPlayer(username, password, sessionKey);
	}


	public String generateSessionKey() {
		return rpcClient.generateSessionKey();
	}

	public List<Channel> getChannels() {
		return ((MasConfig) Clank.getInstance().getConfig()).getChannels();
	}
	
	public List<Location> getLocations() {
		return ((MasConfig) Clank.getInstance().getConfig()).getLocations();
	}

	public int getNatPort() {
		return natPort;
	}


	public int getMlsPort() {
		return mlsPort;
	}

}
