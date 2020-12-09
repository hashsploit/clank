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
	
	@Override
	public String getMlsToken(Integer accountId) {
		switch(accountId) {
		case 50:
			return "11111111111111111111111111111111";
		case 51:
			return "22222222222222222222222222222222";
		case 52:
			return "33333333333333333333333333333333";
		case 53:
			return "44444444444444444444444444444444";
		case 54:
			return "55555555555555555555555555555555";
		case 55:
			return "66666666666666666666666666666666";
		case 56:
			return "77777777777777777777777777777777";
		}
		
		return null;
	}
	
	@Override
	public int getAccountIdFromMlsToken(String mlsToken) {
		if (mlsToken.equals("11111111111111111111111111111111")) {
			return 50;
		}
		if (mlsToken.equals("22222222222222222222222222222222")) {
			return 51;
		}
		if (mlsToken.equals("33333333333333333333333333333333")) {
			return 52;
		}
		if (mlsToken.equals("44444444444444444444444444444444")) {
			return 53;
		}
		if (mlsToken.equals("55555555555555555555555555555555")) {
			return 54;
		}
		if (mlsToken.equals("66666666666666666666666666666666")) {
			return 55;
		}
		if (mlsToken.equals("77777777777777777777777777777777")) {
			return 56;
		}
		return -1;
	}

	@Override
	public String getUsername(int accountId) {
		switch(accountId) {
		case 50:
			return "smily";
		case 51:
			return "hashsploit";
		case 52:
			return "fourbolt";
		case 53:
			return "aequs";
		case 54:
			return "trop";
		case 55:
			return "badger41";
		case 56:
			return "dan";
		}
		
		return null;
	}

	
}
