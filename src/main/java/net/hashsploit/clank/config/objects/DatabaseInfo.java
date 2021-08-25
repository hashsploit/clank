package net.hashsploit.clank.config.objects;

import com.google.gson.annotations.SerializedName;

public class DatabaseInfo {

	@SerializedName("mode")
	private String mode = "MariaDb";

	@SerializedName("host")
	private String host = "127.0.0.1";

	@SerializedName("database")
	private String database = "clank";

	@SerializedName("username")
	private String username = "root";

	@SerializedName("password")
	private String password = null;

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

	public String getMode() {
		return mode;
	}
}
