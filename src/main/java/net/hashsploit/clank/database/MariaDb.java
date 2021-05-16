package net.hashsploit.clank.database;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Logger;
import org.bouncycastle.util.encoders.Hex;

import net.hashsploit.clank.config.objects.DatabaseInfo;
import net.hashsploit.clank.server.medius.MediusConstants;

public class MariaDb implements IDatabase {	
	public static final Logger logger = Logger.getLogger(MariaDb.class.getName());
	
	private static Connection conn;
	
	private DatabaseInfo dbInfo;
	
	private HashSet<String> mlsAccessTokens;
	private HashSet<String> pendingSessionKeys;

	public MariaDb(DatabaseInfo dbInfo) {
		logger.info("Maria DB initializing ...");
		this.dbInfo = dbInfo;
		mlsAccessTokens = new HashSet<String>();
		pendingSessionKeys = new HashSet<String>();
		try {
			conn = DriverManager.getConnection("jdbc:mariadb://" + dbInfo.getHost() + "/" + dbInfo.getDatabase(), dbInfo.getUsername(), dbInfo.getPassword());
			conn.setAutoCommit(true);
			
			PreparedStatement createTables = conn.prepareStatement("create table if not exists users "
					+ "("
					+ "account_id int primary key auto_increment, "
					+ "username char(32) not null unique, "
					+ "password char(64) not null, "
					+ "session_key char(17), "
					+ "session_key_expir date,"
					+ "stats text, "
					+ "ladderstatswide text"
					+ ")"
					+ ";");
			query(createTables);
			
		}
		catch (Exception e) {
			logger.severe("Failed to establish DB connection: " + e.toString());
		}
		logger.info("Maria DB initialized.");
	}
	
	public int getResultSetSize(ResultSet rs) {
		int size =0;
		if (rs != null) 
		{
		  try {
			rs.last();
			size = rs.getRow(); // get row id 
			rs.first();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    // moves cursor to the last row
		}
		return size;
	}
	
	private ResultSet query(PreparedStatement stmt) {
        logger.finest("Executing " + stmt.toString());
		try {
        	ResultSet rs = stmt.executeQuery();
            logger.finest("Execution successful.");
            if (rs != null) {
            	logger.finest("SQL execution results size: " + getResultSetSize(rs));
            }
            return rs;
        }
        catch (Exception e) {
        	logger.severe("Error running query: " + e.toString());
        	e.printStackTrace();
        }
		return null;
	}

	@Override
	public String generateSessionKey() {
		logger.info("Generating session key!");
		String uuid = UUID.randomUUID().toString().substring(0, MediusConstants.SESSIONKEY_MAXLEN.value - 1) + '\0';
		uuid = uuid.replace('-', '1');
		pendingSessionKeys.add(uuid);
		logger.info("Generated session key: " + uuid);
		return uuid;
	}

	@Override
	public int validateAccount(String sessionKey, String username, String password) {
		logger.info("Validating account: " + sessionKey + " " + username + " " + password);
		// sessionKey not valid
		if (!pendingSessionKeys.contains(sessionKey)) {
			throw new IllegalStateException("UNKNOWN SESSION KEY DETECTED WHEN TRYING TO LOGIN USER: '" + username + "' Session key: " + sessionKey);
		}
		pendingSessionKeys.remove(sessionKey);

		// if the player already exists in the db
		int accountId = getAccountIdByUsername(username);
		if (accountId == -1) {
			logger.info(username + " is a new player!");
			accountId = createAccount(sessionKey, username, password);
		} else {
			logger.info(username + " is an existing player!");
			String passwordHashed = hashPassword(password);
			if (passwordHashed.equals(getPasswordByUsername(username))) {
				// Login success
				setSessionKeyByUsername(sessionKey, username);
			}
			else {
				// Login Failure
				return 0;
			}
		}

		logger.info("Validated!");
		logger.info("Account id for validated player: " + accountId);
		return accountId;
	}

	private void setSessionKeyByUsername(String sessionKey, String username) {
		// UPDATE t1 SET c1=c1+1 WHERE c2=(SELECT MAX(c2) FROM t1);
		try {
			PreparedStatement newAcc = conn.prepareStatement("update users set session_key = ? where username = ?;");
			newAcc.setString(1, sessionKey);
			newAcc.setString(2, username.toLowerCase());
			query(newAcc);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getPasswordByUsername(String username) {
		try {
			PreparedStatement newAcc = conn.prepareStatement("select password from users where username = ?;");
			newAcc.setString(1, username.toLowerCase());
			ResultSet results = query(newAcc);
			if (results != null && getResultSetSize(results) != 0) {
				results.first();
				return results.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private int createAccount(String sessionKey, String username, String password) {
		try {
			PreparedStatement newAcc = conn.prepareStatement("insert into users (username, password, session_key) values (?,?,?);");
			newAcc.setString(1, username.toLowerCase());
			newAcc.setString(2, hashPassword(password));
			newAcc.setString(3, sessionKey);
			query(newAcc);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return getAccountIdByUsername(username);
	}

	@Override
	public String generateMlsToken(int accountId) {
		String uuid = UUID.randomUUID().toString().substring(0, MediusConstants.ACCESSKEY_MAXLEN.value - 1) + '\0';
		uuid = uuid.replace('-', '1');
		
		String username = getUsernameByAccountId(accountId);
		if (username == null) {
			throw new IllegalStateException("No player with account id: '" + accountId + "'");
		}
		mlsAccessTokens.add(uuid);
		return uuid;
	}

	
	private int getAccountIdByUsername(String username) {
		try {
			PreparedStatement stmt = conn.prepareStatement("select account_id from users where username = ?;");
			stmt.setString(1, username.toLowerCase());
			ResultSet s = query(stmt);
			if (s == null || getResultSetSize(s) == 0) {
				return -1;
			}
			s.first();
			return s.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private String getUsernameByAccountId(int accountId) {
		try {
			PreparedStatement stmt = conn.prepareStatement("select username from users where account_id = ?;");
			stmt.setInt(1, accountId);
			ResultSet s = query(stmt);
			if (s == null ||  getResultSetSize(s) == 0) {
				return null;
			}
			s.first();
			return s.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean validateMlsAccessToken(String mlsToken) {
		if (mlsAccessTokens.contains(mlsToken)) {
			mlsAccessTokens.remove(mlsToken);
			return true;
		}
		return false;
	}

	@Override
	public int getAccountIdFromSessionKey(String sessionKey) {
		try {
			PreparedStatement stmt = conn.prepareStatement("select account_id from users where session_key = ?;");
			stmt.setString(1, sessionKey);
			ResultSet s = query(stmt);
			if (s == null || getResultSetSize(s) == 0) {
				return -1;
			}
			s.first();
			return s.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public String getUsername(int accountId) {
		return getUsernameByAccountId(accountId);
	}
	
	private String hashPassword(String rawPassword) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] encodedhash = digest.digest(
				rawPassword.getBytes(StandardCharsets.UTF_8));
		String sha256hex = new String(Hex.encode(encodedhash));
		return sha256hex;
	}

	@Override
	public String getStats(int accountId) {
		try {
			PreparedStatement stmt = conn.prepareStatement("select stats from users where account_id = ?;");
			stmt.setInt(1, accountId);
			ResultSet s = query(stmt);
			if (s == null ||  getResultSetSize(s) == 0) {
				return "00c0a84400c0a84400c0a84400c0a8440000af430000af430000af430000af4300000000ffffffffef42000037fa0000ef000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
			}
			s.first();
			String res = s.getString(1);
			if (res == null) {
				return "00c0a84400c0a84400c0a84400c0a8440000af430000af430000af430000af4300000000ffffffffef42000037fa0000ef000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
			}
			return s.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "00c0a84400c0a84400c0a84400c0a8440000af430000af430000af430000af4300000000ffffffffef42000037fa0000ef000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	}

	@Override
	public void setStats(int accountId, String stats) {
		// UPDATE t1 SET c1=c1+1 WHERE c2=(SELECT MAX(c2) FROM t1);
		try {
			PreparedStatement newAcc = conn.prepareStatement("update users set stats = ? where account_id = ?;");
			newAcc.setString(1, stats);
			newAcc.setInt(2, accountId);
			query(newAcc);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getLadderStatsWide(int accountId) {
		try {
			PreparedStatement stmt = conn.prepareStatement("select ladderstatswide from users where account_id = ?;");
			stmt.setInt(1, accountId);
			ResultSet s = query(stmt);
			if (s == null ||  getResultSetSize(s) == 0) {
				return "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
			}
			s.first();
			String res = s.getString(1);
			if (res == null) {
				return "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
			}
			return s.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	}
	
	@Override
	public void setLadderStatsWide(Integer accountId, String stats) {
		try {
			PreparedStatement newAcc = conn.prepareStatement("update users set ladderstatswide = ? where account_id = ?;");
			newAcc.setString(1, stats);
			newAcc.setInt(2, accountId);
			query(newAcc);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}


}
