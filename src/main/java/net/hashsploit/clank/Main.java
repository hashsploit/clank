package net.hashsploit.clank;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.ConfigNames;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.config.configs.MlsConfig;
import net.hashsploit.clank.config.configs.MuisConfig;
import net.hashsploit.clank.config.configs.NatConfig;

public class Main {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Required argument: 1 (config file)");
			return;
		}

		AbstractConfig config = null;

		try {
			final File file = new File(args[0]);

			if (!file.isFile()) {
				System.err.println(String.format("File not found: %s", args[0]));
				return;
			}

			if (!file.getName().endsWith(".json")) {
				System.err.println(String.format("The configuration file '%s' must be a .json file. ", args[0]));
				return;
			}

			JSONTokener jsonTokener = new JSONTokener(new FileReader(new File(args[0])));
			JSONObject jsonConfig = new JSONObject(jsonTokener);
			EmulationMode mode = EmulationMode.valueOf(jsonConfig.getString(ConfigNames.EMULATION_MODE.toString()));

			switch (mode) {
				case MEDIUS_UNIVERSE_INFORMATION_SERVER:
					config = new MuisConfig(jsonConfig);
					break;
				case MEDIUS_AUTHENTICATION_SERVER:
					config = new MasConfig(jsonConfig);
					break;
				case MEDIUS_LOBBY_SERVER:
					config = new MlsConfig(jsonConfig);
					break;
				case DME_SERVER:
					config = new DmeConfig(jsonConfig);
					break;
				case NAT_SERVER:
					config = new NatConfig(jsonConfig);
					break;
				default:
					System.err.println(String.format("Invalid 'emulation_mode' provided in the config file %s", args[0]));
					return;
			}

		} catch (JSONException e) {
			System.err.println(String.format("Failed to start server, failed to parse JSON config error: %s", e.getMessage()));
			return;
		} catch (FileNotFoundException e) {
			System.err.println(String.format("File not found: %s", args[0]));
			return;
		} catch (Throwable t) {
			// Invalid config values or something else when loading the config
			t.printStackTrace();
			return;
		}

		new Clank(config);
	}
}
