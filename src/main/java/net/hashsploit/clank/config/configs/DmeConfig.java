package net.hashsploit.clank.config.configs;

import org.json.JSONObject;

import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.ConfigNames;

public class DmeConfig extends AbstractConfig {

	public DmeConfig(JSONObject json) {
		super(json);
	}

	/**
	 * Get the TCP address the server should be bound to.
	 * 
	 * @return
	 */
	public String getTcpAddress() {
		final String key = ConfigNames.ADDRESS.toString();

		if (getJson().isNull(key)) {
			return "";
		}

		return getJson().getString(key);
	}

	/**
	 * Get the TCP port the server should be bound to.
	 * 
	 * @return
	 */
	public int getTcpPort() {
		final String key = ConfigNames.PORT.toString();

		if (getJson().isNull(key)) {
			return 0;
		}

		return getJson().getInt(key);
	}

	/**
	 * Get the TCP parent thread count that this server should use.
	 * 
	 * @return
	 */
	public int getParentThreads() {
		final String key = ConfigNames.PARENT_THREADS.toString();

		if (getJson().isNull(key)) {
			return 0;
		}

		return getJson().getInt(key);
	}

	/**
	 * Get the TCP child thread count that this server should use.
	 * 
	 * @return
	 */
	public int getChildThreads() {
		final String key = ConfigNames.CHILD_THREADS.toString();

		if (getJson().isNull(key)) {
			return 0;
		}

		return getJson().getInt(key);
	}
	
	/**
	 * Get the UDP thread count that this server should use.
	 * 
	 * @return
	 */
	public int getUdpThreads() {
		final String key = ConfigNames.UDP_THREADS.toString();

		if (getJson().isNull(key)) {
			return 0;
		}

		return getJson().getInt(key);
	}

	/**
	 * Get the max connections capacity.
	 * 
	 * @return
	 */
	public int getCapacity() {
		final String key = ConfigNames.CHILD_THREADS.toString();

		if (getJson().isNull(key)) {
			return 0;
		}

		return getJson().getInt(key);
	}
	
	/**
	 * Get the UDP address the server should be bound to.
	 * 
	 * @return
	 */
	public String getUdpAddress() {
		final String key = ConfigNames.UDP_ADDRESS.toString();

		if (getJson().isNull(key)) {
			return null;
		}

		return getJson().getString(key);
	}

	/**
	 * Get the TCP port the server should be bound to.
	 * 
	 * @return
	 */
	public int getUdpStartingPort() {
		final String key = ConfigNames.UDP_PORT.toString();

		if (getJson().isNull(key)) {
			return 0;
		}

		return getJson().getInt(key);
	}
	
	@Override
	public EmulationMode getEmulationMode() {
		return EmulationMode.DME_SERVER;
	}

}
