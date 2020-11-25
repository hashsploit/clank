package net.hashsploit.clank.cli.commands;

import net.hashsploit.clank.Terminal;
import net.hashsploit.clank.cli.ICLICommand;

public class CLIPlayersCommand implements ICLICommand {
	
	@Override
	public void invoke(Terminal term, String[] params) {
		
		// TODO: if this server is the MAS, then show a table of connections/players currently authenticating and players that have successfully authenticated.
		// if this server is the MLS, show a list of currently connected/online players.
		// if this server is the DME, show a list of currently active games along with the players.
		
	}
	
	@Override
	public String commandName() {
		return "players";
	}
	
	@Override
	public String commandDescription() {
		return "Print a list of all the connected players.";
	}
	
}
