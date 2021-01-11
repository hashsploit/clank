package net.hashsploit.clank.server.muis;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.MuisConfig;
import net.hashsploit.clank.server.medius.MediusServer;
import net.hashsploit.clank.utils.MediusMessageMapInitializer;

public class MediusUniverseInformationServer extends MediusServer {

	public MediusUniverseInformationServer(String address, int port, int parentThreads, int childThreads) {
		super(EmulationMode.MEDIUS_UNIVERSE_INFORMATION_SERVER, address, port, parentThreads, childThreads);

		this.mediusMessageMap = MediusMessageMapInitializer.getMuisMap();

		if (Clank.getInstance().getConfig() instanceof MuisConfig) {
			final MuisConfig muisConfig = (MuisConfig) Clank.getInstance().getConfig();
			for (UniverseInfo universe : muisConfig.getUniverseInformation()) {
				logger.finest("Universes: " + universe.toString());
			}
		}
	}

}
