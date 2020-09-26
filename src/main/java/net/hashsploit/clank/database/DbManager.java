package net.hashsploit.clank.database;

public class DbManager {

	private IDatabase db;
	
	public DbManager() {
		this.db = new SimDb();
	}
	
	public Boolean verifyAccount(String username, String password) {
		return db.verifyAccount(username, password);
	}
}
