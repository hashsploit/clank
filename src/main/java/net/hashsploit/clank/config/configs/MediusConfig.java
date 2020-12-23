package net.hashsploit.clank.config.configs;

import java.util.List;

import org.json.JSONObject;

import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.ConfigNames;
import net.hashsploit.clank.config.objects.DatabaseInfo;
import net.hashsploit.clank.server.rpc.RpcConfig;
import net.hashsploit.clank.server.rpc.RpcServerConfig;

public class MediusConfig extends AbstractConfig {

	public MediusConfig(JSONObject json) {
		super(json);
	}

	/**
	 * Get the address the server should be bound to.
	 * 
	 * @return
	 */
	public String getAddress() {
		final String key = ConfigNames.ADDRESS.toString();

		if (getJson().isNull(key)) {
			return "";
		}

		return getJson().getString(key);
	}

	/**
	 * Get the port the server should be bound to.
	 * 
	 * @return
	 */
	public int getPort() {
		final String key = ConfigNames.PORT.toString();

		return getJson().getInt(key);
	}

	/**
	 * Returns true if encryption should be enforced.
	 * 
	 * @return
	 */
	public boolean isEncrypted() {
		final String key = ConfigNames.ENCRYPTION.toString();

		return getJson().getBoolean(key);
	}
	
	/**
	 * Get the parent thread count that this server should use.
	 * 
	 * @return
	 */
	public int getParentThreads() {
		final String key = ConfigNames.PARENT_THREADS.toString();

		return getJson().getInt(key);
	}

	/**
	 * Get the child thread count that this server should use.
	 * 
	 * @return
	 */
	public int getChildThreads() {
		final String key = ConfigNames.CHILD_THREADS.toString();
		
		return getJson().getInt(key);
	}

	/**
	 * Get the max connections capacity.
	 * 
	 * @return
	 */
	public int getCapacity() {
		final String key = ConfigNames.CHILD_THREADS.toString();
		
		return getJson().getInt(key);
	}
	
	/**
	 * Get the NAT address.
	 * @return
	 */
	public String getNatAddress() {
		final String key = ConfigNames.NAT.toString();
		
		if (getJson().isNull(key)) {
			return null;
		}
		
		if (getJson().getJSONObject(key).isNull(ConfigNames.NAT_ADDRESS.toString())) {
			return null;
		}
		
		return getJson().getJSONObject(key).getString(ConfigNames.NAT_ADDRESS.toString());
	}
	
	/**
	 * Get the NAT port.
	 * @return
	 */
	public int getNatPort() {
		final String key = ConfigNames.NAT.toString();
		
		return getJson().getJSONObject(key).getInt(ConfigNames.NAT_PORT.toString());
	}
	
	/**
	 * Get the policy text.
	 * @return
	 */
	public String getPolicy() {
		final String key = ConfigNames.POLICY.toString();
		
		if (getJson().isNull(key)) {
			return null;
		}
		
		return getJson().getString(key);
	}
	
	/**
	 * Get the announcements message.
	 * @return
	 */
	public List<String> getAnnouncements() {
		final String key = ConfigNames.ANNOUNCEMENTS.toString();

		if (getJson().isNull(ConfigNames.ANNOUNCEMENTS.toString())) {
			return null; 
		}
		
		if (getJson().getJSONArray(ConfigNames.ANNOUNCEMENTS.toString()).isEmpty()) {
			return null;
		}
		
		return getArrayOfStrings(getJson(), key);
	}
	
	/**
	 * Get database info.
	 * @return
	 */
	public DatabaseInfo getDatabaseInfo() {
		final String key = ConfigNames.DATABASE.toString();
		final String host = getJson().getJSONObject(key).getString(ConfigNames.DATABASE_HOST.toString());
		final String database = getJson().getJSONObject(key).getString(ConfigNames.DATABASE_DATABASE.toString());
		final String username = getJson().getJSONObject(key).getString(ConfigNames.DATABASE_USERNAME.toString());
		final String password = getJson().getJSONObject(key).getString(ConfigNames.DATABASE_PASSWORD.toString());
		
		return new DatabaseInfo(host, database, username, password);
	}
	
	/**
	 * Get the RPC client configuration.
	 * @return
	 */
	public RpcConfig getRpcConfig() {
		final String key = ConfigNames.RPC.toString();
		
		if (getJson().isNull(key)) {
			return null; 
		}
		
		if (getJson().getJSONObject(key).isEmpty()) {
			return null;
		}
		
		String rpcAddress = null;
		final int rpcPort = getJson().getJSONObject(key).getInt(ConfigNames.RPC_PORT.toString());
		
		if (!getJson().getJSONObject(key).isNull(ConfigNames.RPC_ADDRESS.toString())) {
			rpcAddress = getJson().getJSONObject(key).getString(ConfigNames.RPC_ADDRESS.toString());
		}
		
		return new RpcConfig(rpcAddress, rpcPort);
	}
	
	/**
	 * Get the RPC server configuration.
	 */
	public RpcServerConfig getRpcServerConfig() {
		final String key = ConfigNames.RPC_SERVER.toString();
		
		if (getJson().isNull(key)) {
			return null; 
		}
		
		if (getJson().getJSONObject(key).isEmpty()) {
			return null;
		}
		
		String rpcAddress = null;
		final int rpcPort = getJson().getJSONObject(key).getInt(ConfigNames.RPC_PORT.toString());
		final JSONObject encryption = getJson().getJSONObject(key).getJSONObject(ConfigNames.RPC_SERVER_ENCRYPTION.toString());
		final boolean encryptionEnabled = encryption.getBoolean(ConfigNames.RPC_SERVER_ENCRYPTION_ENABLED.toString());
		String certChainFile = null;
		String privateKeyFile = null;
		
		if (!getJson().getJSONObject(key).isNull(ConfigNames.RPC_SERVER_ADDRESS.toString())) {
			rpcAddress = getJson().getJSONObject(key).getString(ConfigNames.RPC_SERVER_ADDRESS.toString());
		}
		
		if (!encryption.isNull(ConfigNames.RPC_SERVER_ENCRYPTION_CERT_CHAIN.toString())) {
			certChainFile = encryption.getString(ConfigNames.RPC_SERVER_ENCRYPTION_CERT_CHAIN.toString());
		}
		
		if (!encryption.isNull(ConfigNames.RPC_SERVER_ENCRYPTION_PRIVATE_KEY.toString())) {
			certChainFile = encryption.getString(ConfigNames.RPC_SERVER_ENCRYPTION_PRIVATE_KEY.toString());
		}
		
		return new RpcServerConfig(rpcAddress, rpcPort, encryptionEnabled, certChainFile, privateKeyFile);
	}
	
}
