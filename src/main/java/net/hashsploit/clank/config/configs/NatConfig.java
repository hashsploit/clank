package net.hashsploit.clank.config.configs;

import org.json.JSONObject;

import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.ConfigNames;

public class NatConfig extends AbstractConfig {

	public NatConfig(JSONObject json) {
		super(json);
	}
	
	/**
	 * Get the UDP address the server should be bound to.
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
	 * Get the UDP port the server should be bound to.
	 * @return
	 */
	public int getPort() {
		final String key = ConfigNames.PORT.toString();
		
		if (getJson().isNull(key)) {
			return 0;
		}
		
		return getJson().getInt(key);
	}
	
	/**
	 * Get the thread count that this server should use.
	 * @return
	 */
	public int getUdpThreads() {
		final String key = ConfigNames.UDP_THREADS.toString();
		
		if (getJson().isNull(key)) {
			return 0;
		}
		
		return getJson().getInt(key);
	}
	
	@Override
	public EmulationMode getEmulationMode() {
		return EmulationMode.NAT_SERVER;
	}

}
