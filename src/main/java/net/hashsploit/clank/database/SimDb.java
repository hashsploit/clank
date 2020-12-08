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
	
	@Override
	public int getAccountId(String username) {
		if (username.toLowerCase().equals("smily")) {
			return 50;
		}
		else if (username.toLowerCase().equals("hashsploit")) {
			return 51;
		}
		else if (username.toLowerCase().equals("fourbolt")) { // Used in MediusGetMyClansHandler
			return 52;
		}
		else if (username.toLowerCase().equals("aequs")) {
			return 53;
		}
		else if (username.toLowerCase().equals("trop")) {
			return 54;
		}
		else if (username.toLowerCase().equals("badger41")) {
			return 55;
		}
		else if (username.toLowerCase().equals("dan")) {
			return 56;
		}
		
		return 0;
	}
	
}
