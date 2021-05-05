package net.hashsploit.clank.database;

import java.sql.ResultSet;
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

	private HashSet<String> pendingSessionKeys;
	
	private class SimDbPlayer {
		private String sessionKey;
		private int accountId;
		private String username;
		private String password;
		private String stats;

		public SimDbPlayer(String sessionKey) {
			this.sessionKey = sessionKey;
		}
		
		public String getStats() {
			return stats;
		}
		
		public void setStats(String stats) {
			this.stats = stats;
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

		public void setSessionKey(String sessionKey) {
			this.sessionKey = sessionKey;
		}

	}

	public SimDb() {
		players = new ArrayList<SimDbPlayer>();
		mlsAccessTokens = new HashSet<String>();
		pendingSessionKeys = new HashSet<String>();
		logger.info("Simulated DB initialized ...");
	}

	@Override
	public String generateSessionKey() {
		String uuid = UUID.randomUUID().toString().substring(0, MediusConstants.SESSIONKEY_MAXLEN.value - 1) + '\0';
		uuid = uuid.replace('-', '1');
		pendingSessionKeys.add(uuid);
		return uuid;
	}

	@Override
	public String generateMlsToken(int accountId) {
		String uuid = UUID.randomUUID().toString().substring(0, MediusConstants.ACCESSKEY_MAXLEN.value - 1) + '\0';
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
		for (SimDbPlayer playerToCheck : players) {
			if (playerToCheck.getAccountId() == accountId) {
				player = playerToCheck;
			}
		}
		return player;
	}

	public int getAccountIdFromSessionKey(String sessionKey) {
		SimDbPlayer player = getSimDbPlayerBySessionKey(sessionKey);
		if (player == null) {
			return -1;
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

		// sessionKey not valid
		if (!pendingSessionKeys.contains(sessionKey)) {
			throw new IllegalStateException("UNKNOWN SESSION KEY DETECTED WHEN TRYING TO LOGIN USER: '" + username + "' Session key: " + sessionKey);
		}
		pendingSessionKeys.remove(sessionKey);

		// if the player already exists in the db
		SimDbPlayer player = null;
		if (getSimDbPlayerByUsername(username) == null) {
			logger.info(username + " is a new player!");
			player = new SimDbPlayer(sessionKey);
			players.add(player);
			player.setAccountId(this.getNewAccountId());
			player.setUsername(username);
			player.setPassword(password);
		} else {
			logger.info(username + " is an existing player!");
			player = getSimDbPlayerByUsername(username);
			player.setSessionKey(sessionKey);
		}

		logger.info(player.getAccountId() + " account id for the new player!");
		return player.getAccountId();
	}

	private int getNewAccountId() {
		int accountId = 1;
		for (SimDbPlayer playerToCheck : players) {
			accountId = Integer.max(accountId, playerToCheck.getAccountId() + 1);
		}
		return accountId;
	}

	private SimDbPlayer getSimDbPlayerBySessionKey(String sessionKey) {
		SimDbPlayer player = null;
		for (SimDbPlayer playerToCheck : players) {
			if (playerToCheck.getSessionKey().toLowerCase().equals(sessionKey.toLowerCase())) {
				player = playerToCheck;
			}
		}
		return player;
	}

	private SimDbPlayer getSimDbPlayerByUsername(String username) {
		SimDbPlayer player = null;
		for (SimDbPlayer playerToCheck : players) {
			if (playerToCheck.getUsername().toLowerCase().equals(username.toLowerCase())) {
				player = playerToCheck;
			}
		}
		return player;
	}

	@Override
	public String getStats(int accountId) {
		SimDbPlayer p = getSimDbPlayerByAccountId(accountId);
		if (p == null) {
			return "00c0a84400c0a84400c0a84400c0a8440000af430000af430000af430000af4300000000ffffffffef42000037fa0000ef000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
		}
		return p.getStats();
	}

	@Override
	public void setStats(int accountId, String stats) {
		SimDbPlayer p = getSimDbPlayerByAccountId(accountId);
		if (p == null) {
			return;
		}
		p.setStats(stats);
	}
}
