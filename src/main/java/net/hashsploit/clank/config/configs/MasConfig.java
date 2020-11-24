package net.hashsploit.clank.config.configs;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.ConfigNames;

public class MasConfig extends MediusConfig {

	public MasConfig(JSONObject json) {
		super(json);
	}
	
	/**
	 * Get the MLS address.
	 * @return
	 */
	public String getMlsAddress() {
		final String key = ConfigNames.MLS.toString();
		
		try {
			final String value = getJson().getJSONObject(key).getString(ConfigNames.MLS_ADDRESS.toString());
			
			if (value.trim().isEmpty()) {
				return null;
			}
			
			return value;
		} catch (JSONException e) {}
		
		return null;
	}
	
	/**
	 * Get the MLS port.
	 * @return
	 */
	public String getMlsPort() {
		final String key = ConfigNames.MLS.toString();
		
		return getJson().getJSONObject(key).getString(ConfigNames.MLS_PORT.toString());
	}
	
	/**
	 * Check if the whitelist feature is enabled.
	 * @return
	 */
	public boolean isWhitelistEnabled() {
		final String key = ConfigNames.WHITELIST.toString();
		
		return getJson().getJSONObject(key).getBoolean(ConfigNames.WHITELIST_ENABLED.toString());
	}
	
	/**
	 * Get a list of whitelisted usernames.
	 * @return
	 */
	public List<String> getWhitelistedUsernames() {
		final String key = ConfigNames.WHITELIST_PLAYERS.toString();
		
		if (getJson().isNull(ConfigNames.WHITELIST.toString())) {
			return null; 
		}
		
		if (getJson().getJSONObject(ConfigNames.WHITELIST.toString()).isNull(key) || getJson().getJSONObject(ConfigNames.WHITELIST.toString()).isEmpty()) {
			return null;
		}
		
		return getArrayOfStrings(getJson().getJSONObject(ConfigNames.WHITELIST.toString()), key);
	}
	
	@Override
	public EmulationMode getEmulationMode() {
		return EmulationMode.MEDIUS_AUTHENTICATION_SERVER;
	}

}
