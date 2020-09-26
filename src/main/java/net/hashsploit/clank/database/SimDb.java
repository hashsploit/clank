package net.hashsploit.clank.database;

import java.util.logging.Logger;

public class SimDb implements IDatabase{
	public static final Logger logger = Logger.getLogger("SimDB");

	
	public Boolean verifyAccount(String username, String password) {
		logger.info("Verifying account username: " + username);
		logger.info("Verifying account password: " + password);
		if (username.equals("Smily") && password.equals("Smily123"))
			return true;
		
		return false;
	}
}
