package net.hashsploit.clank.config.configs;

import org.json.JSONObject;

import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.ConfigNames;
import net.hashsploit.clank.database.DatabaseInfo;

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
		
		System.err.println("################################# " + getJson().getString(key));

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
		
		return getJson().getJSONObject(key).getString(ConfigNames.NAT_ADDRESS.toString());
	}
	
	/**
	 * Get the NAT port.
	 * @return
	 */
	public String getNatPort() {
		final String key = ConfigNames.NAT.toString();
		
		return getJson().getJSONObject(key).getString(ConfigNames.NAT_PORT.toString());
	}
	
	/**
	 * 
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

}
