package net.hashsploit.clank.database;

import java.util.logging.Logger;

public class SimDb implements IDatabase {
	
	public static final Logger logger = Logger.getLogger(SimDb.class.getName());

	public SimDb() {
		logger.info("Simulated DB initialized ...");
	}

	@Override
	public boolean accountExists(String username) {
		logger.info(String.format("Checking if account with the username '%s' exists ...", username));
		return true;
	}

	@Override
	public boolean validateAccount(String username, String password) {
		logger.info(String.format("Validating account '%s' ...", username));
		return true;
	}
	
}
