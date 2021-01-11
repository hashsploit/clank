package net.hashsploit.clank.config.configs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.hashsploit.clank.config.ConfigNames;
import net.hashsploit.clank.server.medius.objects.Channel;
import net.hashsploit.clank.server.medius.objects.Location;

public class MasConfig extends MediusConfig {

	public MasConfig(JSONObject json) {
		super(json);
	}

	/**
	 * Get the MLS address.
	 * 
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
		} catch (JSONException e) {
		}

		return null;
	}

	/**
	 * Get the MLS port.
	 * 
	 * @return
	 */
	public int getMlsPort() {
		final String key = ConfigNames.MLS.toString();

		return getJson().getJSONObject(key).getInt(ConfigNames.MLS_PORT.toString());
	}

	/**
	 * Check if the whitelist feature is enabled.
	 * 
	 * @return
	 */
	public boolean isWhitelistEnabled() {
		final String key = ConfigNames.WHITELIST.toString();

		return getJson().getJSONObject(key).getBoolean(ConfigNames.WHITELIST_ENABLED.toString());
	}

	/**
	 * Get a list of whitelisted usernames.
	 * 
	 * @return
	 */
	public HashMap<String, String> getWhitelist() {
		HashMap<String, String> whitelist = new HashMap<String, String>();

		final String key = ConfigNames.WHITELIST_PLAYERS.toString();
		
		if (getJson().isNull(ConfigNames.WHITELIST.toString())) {
			return null;
		}

		if (getJson().getJSONObject(ConfigNames.WHITELIST.toString()).isNull(key) || getJson().getJSONObject(ConfigNames.WHITELIST.toString()).isEmpty()) {
			return null;
		}

		JSONObject names = getJson().getJSONObject(ConfigNames.WHITELIST.toString()).getJSONObject(ConfigNames.WHITELIST_PLAYERS.toString());
		for (Iterator<String> iterator = names.keySet().iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			String password = names.get(name).toString();
			whitelist.put(name.toLowerCase(), password);
		}
		return whitelist;
	}

	/**
	 * Get a list of available channels.
	 * 
	 * @return
	 */
	public List<Channel> getChannels() {
		final String key = ConfigNames.CHANNELS.toString();
		final List<Channel> channels = new ArrayList<Channel>();

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
				if (jsonObj.has(ConfigNames.CHANNELS_ID.toString()) && jsonObj.has(ConfigNames.CHANNELS_NAME.toString()) && jsonObj.has(ConfigNames.CHANNELS_CAPACITY.toString())) {

					final int id = jsonObj.getInt(ConfigNames.CHANNELS_ID.toString());
					final String name = jsonObj.getString(ConfigNames.CHANNELS_NAME.toString());
					final int capacity = jsonObj.getInt(ConfigNames.CHANNELS_CAPACITY.toString());

					channels.add(new Channel(id, name, capacity));
				}
			}
		}

		return channels;
	}

	/**
	 * Get a list of available locations.
	 * 
	 * @return
	 */
	public List<Location> getLocations() {
		final String key = ConfigNames.LOCATIONS.toString();
		final List<Location> locations = new ArrayList<Location>();

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

					locations.add(new Location(id, name));
				}
			}
		}

		return locations;
	}

}
