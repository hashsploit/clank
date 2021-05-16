package net.hashsploit.clank.cli.commands;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EventType;
import net.hashsploit.clank.Terminal;
import net.hashsploit.clank.cli.AnsiColor;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.plugin.IClankPlugin;
import net.hashsploit.clank.plugin.PluginManager;

public class CLIPluginsCommand implements ICLICommand {

	private static final Logger logger = Logger.getLogger(CLIPluginsCommand.class.getName());
	
	@Override
	public void invoke(Terminal term, String[] params) {
		
		
		if (params.length >= 1) {
			
			if (params[0].equalsIgnoreCase("list")) {
				logger.info(String.format("---- %d Loaded Plugins ----", Clank.getInstance().getPluginManager().getPlugins().size()));
				
				for (final IClankPlugin plugin : Clank.getInstance().getPluginManager().getPlugins()) {
					logger.info(String.format("%s [%s]: %s", plugin.getName(), plugin.getVersion(), plugin.getDescription()));
				}
				return;
			} else if (params[0].equalsIgnoreCase("load")) {
				// Plugin names may have spaces (allow it up to here for now, regex will prevent this though)
				String pluginName = String.join(" ", Arrays.copyOfRange(params, 1, params.length));
				
				// Verify the plugin name is valid
				if (!pluginName.matches("[A-Za-z0-9_-]+")) {
					logger.warning(String.format("Invalid plugin name '%s'!", pluginName));
					logger.warning("A plugin name may only be alpha-numeric with hyphens and underscores.");
					return;
				}
				
				final File pluginsFolder = new File(PluginManager.PLUGINS_FOLDER);

				// If plugins/ does not exist, create it.
				if (!pluginsFolder.exists()) {
					pluginsFolder.mkdirs();
				}
				
				// If plugins/ is not a directory
				if (!pluginsFolder.isDirectory()) {
					pluginsFolder.delete();
					pluginsFolder.mkdirs();
				}
				
				final File pluginFile = new File(pluginsFolder, pluginName);
				
				if (!pluginFile.exists()) {
					logger.warning(String.format("Plugin not found: %s", pluginFile.getName()));
					return;
				}
				
				Clank.getInstance().getPluginManager().loadPlugin(pluginFile);
				return;
			} else if (params[0].equalsIgnoreCase("unload")) {
				// Plugin names may have spaces
				String pluginName = String.join(" ", Arrays.copyOfRange(params, 1, params.length));
				
				for (final IClankPlugin plugin : Clank.getInstance().getPluginManager().getPlugins()) {
					if (plugin.getName().equals(pluginName)) {
						Clank.getInstance().getPluginManager().unloadPlugin(pluginName);
						return;
					}
				}
				
				logger.warning(String.format("Plugin not loaded: %s", pluginName));
				return;
			} else if (params[0].equalsIgnoreCase("info")) {
				
				// Plugin names may have spaces
				String pluginName = String.join(" ", Arrays.copyOfRange(params, 1, params.length));
				
				for (final IClankPlugin plugin : Clank.getInstance().getPluginManager().getPlugins()) {
					if (plugin.getName().equals(pluginName)) {
						
						logger.info("---- Plugin Info ----");
						logger.info(String.format("- Name: %s", plugin.getName()));
						logger.info(String.format("- Version: %s", plugin.getVersion()));
						logger.info(String.format("- Description: %s", plugin.getDescription()));
						
						logger.info("- Registered Commands:");
						for (final ICLICommand cmd : plugin.getRegisteredCommands()) {
							logger.info(String.format("  - %s%s%s: %s%s%s", AnsiColor.GREEN, cmd.commandName(), AnsiColor.RESET, AnsiColor.GOLD, cmd.commandDescription(), AnsiColor.RESET));
						}
						
						logger.info("- Registered Event Types:");
						for (final EventType evt : plugin.getRegisteredEventTypes()) {
							logger.info(String.format("  - %s%s%s", AnsiColor.DARK_AQUA, evt.name(), AnsiColor.RESET));
						}
						
						return;
					}
				}
				
				logger.warning(String.format("Plugin not found: %s", pluginName));
				return;
			}
			
		} 
		
		printUsage();
		
	}
	
	private void printUsage() {
		logger.info("Usage: " + commandName() + " list - List all loaded plugins.");
		logger.info("Usage: " + commandName() + " load <plugin-name> - Load a new plugin in manually.");
		logger.info("Usage: " + commandName() + " unload <plugin-name> - Unload a currently loaded plugin.");
		logger.info("Usage: " + commandName() + " info <plugin-name> - Inspect a plugin and provide information about it.");
	}

	@Override
	public String commandName() {
		return "plugin";
	}

	@Override
	public String commandDescription() {
		return "Plugin management.";
	}

}

