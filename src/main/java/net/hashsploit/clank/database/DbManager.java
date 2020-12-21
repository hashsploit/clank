package net.hashsploit.clank.database;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.config.configs.MasConfig;

public class DbManager {

	private final Clank clank;
	private IDatabase db;
	
	public DbManager(Clank clank, IDatabase databaseType) {
		this.clank = clank;
		this.db = databaseType;
	}

	public boolean validateAccount(String username, String password) {
		return db.validateAccount(username, password);
	}

	public int getAccountId(String username) {
		return db.getAccountId(username);
	}

	public String getMlsToken(Integer accountId) {
		return db.getMlsToken(accountId);
	}
	
	public int getAccountIdFromMlsToken(String mlsToken) {
		return db.getAccountIdFromMlsToken(mlsToken);
	}

	public String getUsername(int accountId) {
		return db.getUsername(accountId);
	}
	
	
	
}
