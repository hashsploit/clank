package net.hashsploit.clank.database;

import net.hashsploit.clank.Clank;

public class DbManager {

	private final Clank clank;
	private IDatabase db;
	
	public DbManager(Clank clank, IDatabase databaseType) {
		this.clank = clank;
		this.db = databaseType;
	}

	public boolean accountExists(String username) {
		
		// Check if the whitelist is enabled
		if (clank.getConfig().isWhitelistEnabled()) {
			boolean exists = false;
			// Check if the username matches any of the usernames in the config
			for (final String s : clank.getConfig().getWhitelistedPlayers()) {
				if (s.equalsIgnoreCase(username)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				return false;
			}
		}
		
		return db.accountExists(username);
	}

	public boolean validateAccount(String username, String password) {
		return db.validateAccount(username, password);
	}
	
	
	
	
}
