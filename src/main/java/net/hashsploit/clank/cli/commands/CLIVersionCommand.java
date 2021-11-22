package net.hashsploit.clank.cli.commands;

import java.util.logging.Logger;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.cli.Terminal;

public class CLIVersionCommand implements ICLICommand {

	private static final Logger logger = Logger.getLogger(CLIVersionCommand.class.getName());
	
	@Override
	public void invoke(Terminal term, String[] params) {
		logger.info(String.format("This server is running %s v%s (implementing %s)", Clank.NAME, Clank.VERSION, Clank.getInstance().getConfig().getEmulationMode().name()));
	}

	@Override
	public String commandName() {
		return "version";
	}

	@Override
	public String commandDescription() {
		return "Show the current server version.";
	}

}
