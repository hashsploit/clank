package net.hashsploit.clank.config.configs;

import java.util.List;

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
	 * Get player operators.
	 * @return
	 */
	public List<String> getOperators() {
		final String key = ConfigNames.OPERATORS.toString();

		if (getJson().isNull(ConfigNames.OPERATORS.toString())) {
			return null; 
		}
		
		if (getJson().getJSONObject(ConfigNames.OPERATORS.toString()).isNull(key) || getJson().getJSONObject(ConfigNames.OPERATORS.toString()).isEmpty()) {
			return null;
		}
		
		return getArrayOfStrings(getJson().getJSONObject(ConfigNames.OPERATORS.toString()), key);
	}
	
}
