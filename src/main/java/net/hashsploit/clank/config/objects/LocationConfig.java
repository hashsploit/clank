package net.hashsploit.clank.config.objects;

public class LocationConfig {
	
	private final int id;
	private final String name;
	
	public LocationConfig(final int id, final String name) {
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
