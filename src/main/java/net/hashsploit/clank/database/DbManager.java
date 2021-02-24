package net.hashsploit.clank.database;

import net.hashsploit.clank.Clank;

public class DbManager {

	private final Clank clank;
	private IDatabase db;
	
	public DbManager(Clank clank, IDatabase databaseType) {
		this.clank = clank;
		this.db = databaseType;
	}

	public int validateAccount(String sessionKey, String username, String password) {
		return db.validateAccount(sessionKey, username, password);
	}

	public String generateSessionKey() {
		return db.generateSessionKey();
	}

	public String generateMlsToken(int accountId) {
		return db.generateMlsToken(accountId);
	}

	public boolean validateMlsAccessToken(String mlsAccessToken) {
		return db.validateMlsAccessToken(mlsAccessToken);
	}

	public int getAccountIdFromSessionKey(String sessionKey) {
		return db.getAccountIdFromSessionKey(sessionKey);
	}

	public String getUsername(int accountId) {
		return db.getUsername(accountId);
	}

	// ------------------------------- DELETE THESE
	public int getAccountId(String a) {
		return 0;
	}

	public int getAccountIdFromMlsToken(String a) {
		return 0;
	}

	public String getMlsToken(int a) {
		return null;
	}

	public Clank getClank() {
		return clank;
	}

}
