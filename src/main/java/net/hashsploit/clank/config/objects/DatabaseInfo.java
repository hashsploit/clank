package net.hashsploit.clank.config.objects;

public class DatabaseInfo {

	private String host;
	private String database;
	private String username;
	private String password;

	public DatabaseInfo(final String host, final String database, final String username, final String password) {
		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
