package net.hashsploit.clank.config.objects;

public class ChannelConfig {
	
	private final int id;
	private final String name;
	private final int capacity;
	
	public ChannelConfig(final int id, final String name, final int capacity) {
		this.id = id;
		this.name = name;
		this.capacity = capacity;
	}
	
	/**
	 * Get the channel id.
	 * @return
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Get the channel name.
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get the maximum amount of players this channel can support. (255 at most)
	 * @return
	 */
	public int getCapacity() {
		return capacity;
	}
	
}
