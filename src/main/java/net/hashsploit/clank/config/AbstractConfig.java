package net.hashsploit.clank.config;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.hashsploit.clank.EmulationMode;

public class AbstractConfig {
	
	private final JSONObject jsonConfig;
	private final EmulationMode emulationMode;
	private final Level logLevel;
	
	public AbstractConfig(final JSONObject json) {
		this.jsonConfig = json;
		this.emulationMode = EmulationMode.valueOf(json.getString(ConfigNames.EMULATION_MODE.toString()));
		this.logLevel = Level.parse(json.getString(ConfigNames.LOG_LEVEL.toString()));
	}
	
	/**
	 * Get the underlying JSON Object configuration.
	 * @return
	 */
	public JSONObject getJson() {
		return jsonConfig;
	}
	
	/**
	 * Get a configuration value directly.
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		return jsonConfig.isNull(key) ? null : jsonConfig.getString(key);
	}
	
	/**
	 * Get an array of objects.
	 * @param key
	 * @return
	 */
	private static List<Object> getArrayOfObjects(JSONObject json, String key) {
		if (json.isNull(key)) {
			return null;
		}
		
		try {
			JSONArray jsonArray = json.getJSONArray(key);
			return jsonArray.toList();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Get an array of strings.
	 * @param json
	 * @param key
	 * @return
	 */
	public static List<String> getArrayOfStrings(JSONObject json, String key) {
		return getArrayOfObjects(json, key).stream().map(element -> element.toString()).collect(Collectors.toList());
	}
	
	/**
	 * Get an array of integers.
	 * @param json
	 * @param key
	 * @return
	 */
	public static List<Integer> getArrayOfIntegers(JSONObject json, String key) {
		return getArrayOfObjects(json, key).stream().map(element -> Integer.parseInt(element.toString())).collect(Collectors.toList());
	}
	
	/**
	 * Get the config's emulation mode.
	 */
	public EmulationMode getEmulationMode() {
		return emulationMode;
	}

	/**
	 * Get the config's logger level.
	 */
	public Level getLogLevel() {
		return logLevel;
	}
	
}
