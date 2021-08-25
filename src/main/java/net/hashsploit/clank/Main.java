package net.hashsploit.clank;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.configs.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Required argument: 1 (config file)");
			return;
		}

		try {
			final File file = new File(args[0]);

			if (!file.isFile()) {
				System.err.printf("File not found: %s%n", args[0]);
				return;
			}

			Gson gson = new Gson();
			JsonObject jsonConfig = gson.fromJson(new FileReader(args[0]), JsonObject.class);

			AbstractConfig config = gson.fromJson(jsonConfig, AbstractConfig.class);

			switch (config.getEmulationMode()) {
				case MEDIUS_UNIVERSE_INFORMATION_SERVER:
					config = gson.fromJson(jsonConfig, MuisConfig.class);
					break;
				case MEDIUS_AUTHENTICATION_SERVER:
					config = gson.fromJson(jsonConfig, MasConfig.class);
					break;
				case MEDIUS_LOBBY_SERVER:
					config = gson.fromJson(jsonConfig, MlsConfig.class);
					break;
				case DME_SERVER:
					config = gson.fromJson(jsonConfig, DmeConfig.class);
					break;
				case NAT_SERVER:
					config = gson.fromJson(jsonConfig, NatConfig.class);
					break;
				default:
					System.err.printf("Invalid 'emulation_mode' provided in the config file %s%n", args[0]);
					return;
			}

			config.setJsonObject(jsonConfig);
			new Clank(config);

		} catch (JsonParseException e) {
			System.err.printf("Failed to start server, failed to parse JSON config error: %s%n", e.getMessage());
		} catch (FileNotFoundException e) {
			System.err.printf("File not found: %s%n", args[0]);
		} catch (Throwable t) {
			// Invalid config values or something else when loading the config
			t.printStackTrace();
		}

	}
}
