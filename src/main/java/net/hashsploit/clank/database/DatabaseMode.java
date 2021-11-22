package net.hashsploit.clank.database;

public enum DatabaseMode {
	
	MARIA_DB("maria_db");
	
	public final String name;
	
	private DatabaseMode(String name) {
		this.name = name;
	}
	
}
