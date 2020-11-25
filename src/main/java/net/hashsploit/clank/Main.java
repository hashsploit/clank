package net.hashsploit.clank;

import java.io.File;
import java.io.FileReader;

import org.json.JSONObject;
import org.json.JSONTokener;

import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.ConfigNames;
import net.hashsploit.clank.config.configs.MasConfig;
import net.hashsploit.clank.config.configs.MlsConfig;
import net.hashsploit.clank.config.configs.MpsConfig;
import net.hashsploit.clank.config.configs.MuisConfig;

public class Main {
	
	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.err.println("Required argument: 1 (config file)");
			return;
		}
		
		try {
			if (!new File(args[0]).isFile()) {
				System.err.println("File not found: " + args[0]);
				return;
			}
			
			AbstractConfig config = null;
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
				case MEDIUS_PROXY_SERVER:
					config = new MpsConfig(jsonConfig);
					break;
				
				default:
					System.err.println(String.format("Invalid 'emulation_mode' provided in the config file %s", args[0]));
					break;
			}

			new Clank(config);
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
}
