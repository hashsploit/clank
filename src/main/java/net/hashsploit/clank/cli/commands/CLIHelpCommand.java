package net.hashsploit.clank.cli.commands;

import java.util.logging.Logger;

import net.hashsploit.clank.Terminal;
import net.hashsploit.clank.cli.AnsiColor;
import net.hashsploit.clank.cli.ICLICommand;

public class CLIHelpCommand implements ICLICommand {

	private static final Logger logger = Logger.getLogger("");
	
	@Override
	public void invoke(Terminal term, String[] params) {
		if (params.length == 1) {
			for (ICLICommand c : term.getRegisteredCommands()) {
				if (c.commandName().equalsIgnoreCase(params[1])) {
					logger.info("---- Help for '" + c.commandName() + "' ----");
					logger.info(String.format(Terminal.colorize(AnsiColor.GREEN + "%s" + AnsiColor.RESET + "  -  " + AnsiColor.GOLD + "%s" + AnsiColor.RESET), c.commandName(), c.commandDescription()));
				}
			}
			return;
		}
		logger.info("---- Help Commands ----");
		for (ICLICommand c : term.getRegisteredCommands()) {
			logger.info(String.format(Terminal.colorize(AnsiColor.GREEN + "%s" + AnsiColor.RESET + "  -  " + AnsiColor.GOLD + "%s" + AnsiColor.RESET), c.commandName(), c.commandDescription()));
		}
	}

	@Override
	public String commandName() {
		return "help";
	}

	@Override
	public String commandDescription() {
		return "Show a list of commands.";
	}

}
