package net.hashsploit.clank.database;

public interface IDatabase {
	
	/**
	 * Check if the username for an account exists, this can be used to validate if the player account must be created first.
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean accountExists(String username);
	
	/**
	 * Check if the account credentials passed in are correct.
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean validateAccount(String username, String password);
	
}
