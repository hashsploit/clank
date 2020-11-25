package net.hashsploit.clank.config.configs;

import org.json.JSONObject;

import net.hashsploit.clank.config.ConfigNames;

public class MlsConfig extends MediusConfig {

	public MlsConfig(JSONObject json) {
		super(json);
	}
	
	/**
	 * Get the DME TCP address.
	 * @return
	 */
	public String getDmeAddress() {
		final String key = ConfigNames.DME.toString();
		
		return getJson().getJSONObject(key).getString(ConfigNames.DME_ADDRESS.toString());
	}
	
	/**
	 * Get the DME TCP port.
	 * @return
	 */
	public int getDmePort() {
		final String key = ConfigNames.DME.toString();
		
		return getJson().getJSONObject(key).getInt(ConfigNames.DME_PORT.toString());
	}
	

}
