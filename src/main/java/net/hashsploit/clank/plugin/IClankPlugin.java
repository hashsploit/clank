package net.hashsploit.clank.plugin;

import java.util.HashSet;

import net.hashsploit.clank.EventType;
import net.hashsploit.clank.cli.ICLICommand;

public interface IClankPlugin {
	
	/**
	 * Get the plugin name.
	 * @return
	 */
	public String getName();
	
	/**
	 * Get the plugin version as a string.
	 * @return
	 */
	public String getVersion();
	
	/**
	 * Get the plugin version revision 0.0.x.
	 * @return
	 */
	public int getVersionRevision();
	
	/**
	 * Get the plugin version minor 0.x.0.
	 * @return
	 */
	public int getVersionMinor();
	
	/**
	 * Get the plugin version major x.0.0.
	 * @return
	 */
	public int getVersionMajor();
	
	/**
	 * Get the plugin description.
	 * @return
	 */
	public String getDescription();
	
	/**
	 * Plugin initialization function
	 */
	public void init();
	
	/**
	 * Get all the CLI commands this plugin registers.
	 * @return
	 */
	public HashSet<ICLICommand> getRegisteredCommands();
	
	/**
	 * Get all the clank event types this plugin subscribes to.
	 * @return
	 */
	public HashSet<EventType> getRegisteredEventTypes();
	
	/**
	 * Plugin event call.
	 */
	public void onEvent(ClankEvent event);
	
}
