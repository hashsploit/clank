package net.hashsploit.clank.server.medius;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.server.rpc.ClankDmeRpcClient;
import net.hashsploit.clank.server.rpc.ClankMasRpcClient;
import net.hashsploit.clank.server.rpc.PlayerLoginResponse;
import net.hashsploit.clank.server.rpc.RpcConfig;
import net.hashsploit.clank.utils.MediusMessageMapInitializer;
import net.hashsploit.clank.utils.Utils;

public class MediusAuthenticationServer extends MediusServer{

	private final ClankMasRpcClient rpcClient;
	
	public MediusAuthenticationServer(EmulationMode emulationMode, String address, int port, int parentThreads,
			int childThreads) {
		super(emulationMode, address, port, parentThreads, childThreads);
		
		this.mediusMessageMap = MediusMessageMapInitializer.getMasMap();
		
		
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
			for (String key: masConfig.getWhitelist().keySet()) {
				logger.info("Whitelist username/pass: " + key + " / " + masConfig.getWhitelist().get(key));
			}
		}
	}
	
	
	public PlayerLoginResponse playerLogin(String username, String password) {
		return rpcClient.loginPlayer(username, password);
	}

	
}
