package net.hashsploit.clank.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import net.hashsploit.clank.server.medius.MediusConstants;

public class SimDb implements IDatabase {
	
	public static final Logger logger = Logger.getLogger(SimDb.class.getName());
	
	private List<SimDbPlayer> players;
	
	private HashSet<String> mlsAccessTokens;
	
	
	private class SimDbPlayer {
		private String sessionKey;
		private int accountId;
		private String username;
		private String password;
		public SimDbPlayer(String sessionKey) {
			this.sessionKey = sessionKey;
		}

		public String getSessionKey() {
			return sessionKey;
		}

		public int getAccountId() {
			return accountId;
		}

		public String getUsername() {
			return username;
		}

		public void setAccountId(int accountId) {
			this.accountId = accountId;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		
	}
	

	public SimDb() {
		players = new ArrayList<SimDbPlayer>();
		mlsAccessTokens = new HashSet<String>();
		logger.info("Simulated DB initialized ...");
	}
	
	@Override
	public String generateSessionKey() {
		String uuid = UUID.randomUUID().toString().substring(0, MediusConstants.SESSIONKEY_MAXLEN.getValue()-1) + '\0';
		uuid = uuid.replace('-', '1');
		players.add(new SimDbPlayer(uuid));
	    return uuid;	
	}
	
	@Override
	public String generateMlsToken(int accountId) {
		String uuid = UUID.randomUUID().toString().substring(0, MediusConstants.ACCESSKEY_MAXLEN.getValue()-1) + '\0';
		uuid = uuid.replace('-', '1');
		SimDbPlayer player = this.getSimDbPlayerByAccountId(accountId);
		if (player == null) {
			throw new IllegalStateException("No player with account id: '" + accountId + "'");
		}
		mlsAccessTokens.add(uuid);
		return uuid;
	}
	
	@Override
	public boolean validateMlsAccessToken(String mlsToken) {
		if (mlsAccessTokens.contains(mlsToken)) {
			mlsAccessTokens.remove(mlsToken);
			return true;
		}
		return false;
	}

	private SimDbPlayer getSimDbPlayerByAccountId(int accountId) {
		SimDbPlayer player = null;
		for (SimDbPlayer playerToCheck: players) {
			if (playerToCheck.getAccountId() == accountId) {
				player = playerToCheck;
			}
		}
		return player;
	}
	
	public int getAccountIdFromSessionKey(String sessionKey) {
		SimDbPlayer player = getSimDbPlayerBySessionKey(sessionKey);
		if (player == null) {
			return 0;
		}
		return player.getAccountId();
	}
	
	public String getUsername(int accountId) {
		SimDbPlayer player = getSimDbPlayerByAccountId(accountId);
		if (player == null) {
			return null;
		}
		return player.getUsername();
	}


	@Override
	public int validateAccount(String sessionKey, String username, String password) {
		if (false) { // in real db, change this to be checking username/password
			return 0;
		}
		
		SimDbPlayer player = this.getSimDbPlayerBySessionKey(sessionKey);
		if (player == null) {
			throw new IllegalStateException("UNKNOWN SESSION KEY DETECTED WHEN TRYING TO LOGIN USER: '" + username + "' Session key: " + sessionKey);
		}
		player.setAccountId(this.getNewAccountId());
		player.setUsername(username);
		player.setPassword(password);
		
		return player.getAccountId();
	}

	private int getNewAccountId() {
		int accountId = 1;
		for (SimDbPlayer playerToCheck: players) {
			accountId = Integer.max(accountId, playerToCheck.getAccountId()+1);
		}
		return accountId;
	}

	private SimDbPlayer getSimDbPlayerBySessionKey(String sessionKey) {
		SimDbPlayer player = null;
		for (SimDbPlayer playerToCheck: players) {
			if (playerToCheck.getSessionKey().toLowerCase().equals(sessionKey.toLowerCase())) {
				player = playerToCheck;
			}
		}
		return player;
	}
}
