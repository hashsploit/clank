package net.hashsploit.clank.config.configs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.ConfigNames;
import net.hashsploit.clank.server.muis.UniverseInfo;

public class MuisConfig extends MediusConfig {

	public MuisConfig(JSONObject json) {
		super(json);
	}

	public List<UniverseInfo> getUniverseInformation() {

		if (getJson().isNull(ConfigNames.UNIVERSES.toString())) {
			return null;
		}

		try {
			JSONArray jsonArray = getJson().getJSONArray(ConfigNames.UNIVERSES.toString());
			List<UniverseInfo> universes = new ArrayList<UniverseInfo>();
			
			jsonArray.forEach(element -> {
				final JSONObject universe = (JSONObject) element;
				final int applicationId = universe.getInt("application_id");
				final String name = universe.getString("name");
				final String description = universe.getString("description");
				final int universeId = universe.getInt("universe_id");
				final String hostname = universe.getString("hostname");
				final int port = universe.getInt("port");
				universes.add(new UniverseInfo(name, description, hostname, port, applicationId, universeId));
			});
			
			return universes;
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public EmulationMode getEmulationMode() {
		return EmulationMode.MEDIUS_UNIVERSE_INFORMATION_SERVER;
	}

}
