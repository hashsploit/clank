package net.hashsploit.clank.cli;

import net.hashsploit.clank.Terminal;

public interface ICLICommand {
	
	/**
	 * The command name that identifies this command that will be invoked
	 */
	public String commandName();

	/**
	 * The description of this command, use NULL for no description
	 */
	public String commandDescription();

	/**
	 * This method is a callback from when the command name gets invoked
	 * 
	 * @param command parameters
	 */
	public void invoke(Terminal term, String[] params);
}
