package net.hashsploit.clank.config.objects;

import com.google.gson.annotations.SerializedName;

import net.hashsploit.clank.database.DatabaseMode;

public class DatabaseInfo {

	@SerializedName("mode")
	private DatabaseMode mode;

	@SerializedName("host")
	private String host = "127.0.0.1";

	@SerializedName("database")
	private String database = "clank";

	@SerializedName("username")
	private String username = "clank";

	@SerializedName("password")
	private String password = "clank";

	public DatabaseMode getMode() {
		return mode;
	}
	
	public String getHost() {
		return host;
	}

	public String getDatabase() {
		return database;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
