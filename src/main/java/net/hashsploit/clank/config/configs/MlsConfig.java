package net.hashsploit.clank.config.configs;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import net.hashsploit.clank.config.ConfigNames;
import net.hashsploit.clank.server.common.objects.Channel;
import net.hashsploit.clank.server.common.objects.Location;

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
	 * Get player operators.
	 * @return
	 */
	public List<String> getOperators() {
		final String key = ConfigNames.OPERATORS.toString();

		if (getJson().isNull(key)) {
			return null; 
		}
		
		if (getJson().getJSONArray(key).isEmpty()) {
			return null;
		}
		
		return getArrayOfStrings(getJson(), key);
	}
	
	/**
	 * Get a list of available channels.
	 * 
	 * @return
	 */
	public Set<Channel> getChannels() {
		final String key = ConfigNames.CHANNELS.toString();
		final Set<Channel> channels = new HashSet<Channel>();

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
	public HashSet<Location> getLocations() {
		final String key = ConfigNames.LOCATIONS.toString();
		final HashSet<Location> locations = new HashSet<Location>();

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
