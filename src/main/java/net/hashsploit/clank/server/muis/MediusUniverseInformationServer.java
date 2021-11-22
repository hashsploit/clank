package net.hashsploit.clank.server.muis;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.config.configs.MuisConfig2;
import net.hashsploit.clank.server.medius.MediusServer;

public class MediusUniverseInformationServer extends MediusServer {

	public MediusUniverseInformationServer(String address, int port, int parentThreads, int childThreads) {
		super(EmulationMode.MEDIUS_UNIVERSE_INFORMATION_SERVER, address, port, parentThreads, childThreads);

		if (Clank.getInstance().getConfig() instanceof MuisConfig2) {
			final MuisConfig2 muisConfig = (MuisConfig2) Clank.getInstance().getConfig();
			for (UniverseInfo universe : muisConfig.getUniverseInformation()) {
				logger.finest("Universes: " + universe.toString());
			}
		}
	}

}
