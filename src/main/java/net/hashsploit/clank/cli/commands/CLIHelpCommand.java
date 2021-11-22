package net.hashsploit.clank.cli.commands;

import java.util.logging.Logger;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.cli.AnsiColor;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.cli.Terminal;
import net.hashsploit.clank.plugin.IClankPlugin;

public class CLIHelpCommand implements ICLICommand {

	private static final Logger logger = Logger.getLogger(CLIHelpCommand.class.getName());
	
	@Override
	public void invoke(Terminal term, String[] params) {
		if (params.length == 1) {
			for (ICLICommand c : term.getRegisteredCommands()) {
				if (c.commandName().equalsIgnoreCase(params[0])) {
					boolean isPlugin = false;
					
					for (final IClankPlugin plugin : Clank.getInstance().getPluginManager().getPlugins()) {
						for (final ICLICommand c2 : plugin.getRegisteredCommands()) {
							if (c == c2) {
								isPlugin = true;
								break;
							}
						}
						if (isPlugin) {
							break;
						}
					}
					
					logger.info("---- Help for '" + c.commandName() + "' ----");
					logger.info(String.format(Terminal.colorize((isPlugin ? AnsiColor.BLUE : AnsiColor.GREEN) + "%s" + AnsiColor.RESET + "  -  " + AnsiColor.GOLD + "%s" + AnsiColor.RESET), c.commandName(), c.commandDescription()));
				}
			}
			return;
		}
		logger.info("---- Help Commands ----");
		for (final ICLICommand c : term.getRegisteredCommands()) {
			boolean isPlugin = false;
			
			for (final IClankPlugin plugin : Clank.getInstance().getPluginManager().getPlugins()) {
				for (final ICLICommand c2 : plugin.getRegisteredCommands()) {
					if (c == c2) {
						isPlugin = true;
						break;
					}
				}
				if (isPlugin) {
					break;
				}
			}
			
			logger.info(String.format(Terminal.colorize((isPlugin ? AnsiColor.BLUE : AnsiColor.GREEN) + "%s" + AnsiColor.RESET + "  -  " + AnsiColor.GOLD + "%s" + AnsiColor.RESET), c.commandName(), c.commandDescription()));
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
