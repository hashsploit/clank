package net.hashsploit.clank.config.configs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.ConfigNames;
import net.hashsploit.clank.config.objects.LocationConfig;

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
	public int getMlsPort() {
		final String key = ConfigNames.MLS.toString();
		
		return getJson().getJSONObject(key).getInt(ConfigNames.MLS_PORT.toString());
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

	/**
	 * Get a list of available locations.
	 * @return
	 */
	public List<LocationConfig> getLocations() {
		final String key = ConfigNames.LOCATIONS.toString();
		final List<LocationConfig> locations = new ArrayList<LocationConfig>();
		
		if (getJson().isNull(key)) {
			return null;
		}
		
		if (getJson().getJSONArray(key).isEmpty()) {
			return null;
		}
		
		final JSONArray jsonArray = getJson().getJSONArray(key);
		final Iterator<Object> iterator = jsonArray.iterator();
		
		while (iterator.hasNext()) {
			final Object obj = iterator.next();
			if (obj instanceof JSONObject) {
				final JSONObject jsonObj = (JSONObject) obj;
				if (jsonObj.has(ConfigNames.LOCATIONS_ID.toString()) && jsonObj.has(ConfigNames.LOCATIONS_NAME.toString())) {
					
					final int id = jsonObj.getInt(ConfigNames.LOCATIONS_ID.toString());
					final String name = jsonObj.getString(ConfigNames.LOCATIONS_NAME.toString());
					
					locations.add(new LocationConfig(id, name));
				}
			}
		}
		
		return locations;
	}

}
