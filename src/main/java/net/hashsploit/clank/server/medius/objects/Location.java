package net.hashsploit.clank.server.medius.objects;

public class Location {
	
	private final int id;
	private final String name;
	
	public Location(final int id, final String name) {
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Get the location id.
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Get the location name.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
}
