package net.hashsploit.clank.database;

public interface IDatabase {

	String generateSessionKey();

	int validateAccount(String sessionKey, String username, String password);

	String generateMlsToken(int accountId);
	
	boolean validateMlsAccessToken(String mlsToken);

	int getAccountIdFromSessionKey(String sessionKey);

	String getUsername(int accountId);

}
