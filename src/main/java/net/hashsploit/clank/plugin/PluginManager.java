package net.hashsploit.clank.plugin;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.EventType;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.cli.Terminal;
import net.hashsploit.clank.utils.FileHelper;
import net.hashsploit.clank.utils.Utils;

public class PluginManager {
	
	private static final Logger logger = Logger.getLogger(PluginManager.class.getName());
	public static final String PLUGINS_FOLDER = "plugins";
	public static final String LUA_SRC_INIT_FILE = "init.lua";
	public static final String LUA_BIN_INIT_FILE = "init.lub";
	
	private HashSet<IClankPlugin> plugins;
	private Clank clank;
	private final String tmpFolderName;
	
	public PluginManager(Clank clank) {
		this.clank = clank;
		this.plugins = new HashSet<>();
		this.tmpFolderName = "clank_plugins_" + new Random().nextInt(100000);
	}
	
	/**
	 * Load a plugin by folder/archive name.
	 * @param name
	 * @return
	 */
	public synchronized boolean loadPlugin(final File pluginPath) {
		
		if (!pluginPath.isDirectory()) {
			logger.warning(String.format("Attempted to load an invalid plugin: %s", pluginPath.getAbsolutePath()));
			return false;
		}

		final File initFile = new File(pluginPath, LUA_SRC_INIT_FILE);
		
		// Grab the plugin name from the folder name
		final String name = pluginPath.getName();
		
		if (!initFile.isFile()) {
			logger.warning(String.format("Invalid plugin '%s'!", name));
			return false;
		}
		
		for (IClankPlugin plugin : plugins) {
			if (plugin.getName().equalsIgnoreCase(name)) {
				logger.warning(String.format("Plugin '%s' is already loaded!", name));
				return false;
			}
		}
		
		PluginStatus pluginStatus = null;
		
		// Load Lua source file (.lua)
		if (initFile.getName().endsWith(".lua")) {
			pluginStatus = loadLuaSource(name, initFile.getPath());
		} else if (initFile.getName().endsWith(".lub")) {
			// FIXME: load lua binary
			
		}
		
		if (pluginStatus == null) {
			logger.warning(String.format("Failed to load plugin '%s', no such handler for %s ...", name, initFile.getName()));
			return false;
		}
		
		// Process plugin result
		
		if (!pluginStatus.getStatus()) {
			logger.warning(String.format("Failed to load plugin '%s': %s", name, pluginStatus.getStatusMessage()));
			return false;
		}
		
		if (pluginStatus.getEmulationModes() == 0) {
			logger.warning(String.format("The plugin '%s' may not run on any emulation mode.", name));
			return false;
		}
		
		if (!Utils.isInBitmask(clank.getConfig().getEmulationMode().getValue(), pluginStatus.getEmulationModes())) {
			int modes = pluginStatus.getEmulationModes();
			final List<EmulationMode> allowedModes = new ArrayList<>();
			
			for (EmulationMode mode : EmulationMode.values()) {
				if (Utils.isInBitmask(mode.getValue(), modes)) {
					allowedModes.add(mode);
				}
			}
			
			logger.warning(String.format("The plugin '%s' may only be run on %s.", name, Arrays.toString(allowedModes.toArray())));
			return false;
		}
		
		for (final ICLICommand cmd : pluginStatus.getCommands()) {
			for (final ICLICommand registeredCmd : clank.getTerminal().getRegisteredCommands()) {
				if (registeredCmd.commandName().equalsIgnoreCase(cmd.commandName())) {
					logger.warning(String.format("The plugin %s attempted registering the CLI command name '%s', which is already registered.", name, registeredCmd.commandName()));
					continue;
				} else {
					clank.getTerminal().registerCommand(cmd);
					break;
				}
			}
		}
		
		for (final ClankEvent event : pluginStatus.getEvents()) {
			// TODO: Subscribe to clank callback events
			
		}
		
		
		plugins.add(pluginStatus.getPlugin());
		logger.fine(String.format("Plugin '%s' loaded", pluginStatus.getPlugin().getName()));
		
		// Initialize the plugin
		pluginStatus.getPlugin().init();
		
		return true;
	}
	
	public void loadAllAvailablePlugins() {
		
		final File pluginsFolder = new File(PLUGINS_FOLDER);

		// If plugins/ does not exist, create it.
		if (!pluginsFolder.exists()) {
			pluginsFolder.mkdirs();
		}
		
		// If plugins/ is not a directory
		if (!pluginsFolder.isDirectory()) {
			pluginsFolder.delete();
			pluginsFolder.mkdirs();
		}
		
		for (final File pluginFile : pluginsFolder.listFiles()) {
			
			if (pluginFile.isFile()) {
				// This plugin is an archive
				if (pluginFile.getName().endsWith(".zip")) {
					final String target = System.getProperty("java.io.tmpdir") + File.separator + tmpFolderName + File.separator + pluginFile.getName().split(Pattern.quote("."))[0];
					final File tmpPluginFolders = new File(System.getProperty("java.io.tmpdir") + File.separator + tmpFolderName);
					tmpPluginFolders.mkdirs();
					tmpPluginFolders.deleteOnExit();
					
					final Path sourcePath = Paths.get(pluginFile.getAbsolutePath());
					final Path targetPath = Paths.get(target);
					
					try {
						FileHelper.unzipFolder(sourcePath, targetPath);
						loadPlugin(targetPath.toFile());
					} catch (IOException e) {
						logger.warning(String.format("Failed to unzip and load plugin: %s", pluginFile.getName()));
						e.printStackTrace();
					}
				}
				
			} else if (pluginFile.isDirectory()) {
				// This plugin is a folder
				loadPlugin(pluginFile);
			}
			
		}
		
	}
	
	public void unloadAllPlugins() {
		Iterator<IClankPlugin> pluginsIterator = plugins.iterator();
		
		while (pluginsIterator.hasNext()) {
			final IClankPlugin plugin = pluginsIterator.next();
			synchronized (pluginsIterator) {
				plugin.shutdown();
				pluginsIterator.remove();
			}
		}
	}
	
	/**
	 * Load a Lua plugin from a source file path (.lua)
	 * @param name
	 * @param path
	 * @return
	 */
	private PluginStatus loadLuaSource(final String name, final String path) {
		
		final PluginStatus status = new PluginStatus();
		status.setStatus(false, "An unknown error has occured.");
		
		try {
			final String initScript = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
			
			
			
			// Create a new Lua sandbox for this plugin
			final LuaChunk luaChunkObj = new LuaChunk(name, LUA_SRC_INIT_FILE, initScript);
			final LuaValue chunk = luaChunkObj.getChunk();
			
			
			
			// Sanity checks
			if (chunk == null) {
				status.setStatus(false, "The Lua chunk is null.");
				return status;
			}
			if (!chunk.istable()) {
				status.setStatus(false, "Missing required Lua plugin convention table.");
				return status;
			}
			
			
			
			// Plugin Name
			final LuaValue luaPluginName = chunk.get("name");
			if (!luaPluginName.isstring()) {
				status.setStatus(false, "Expected key 'name' to be a string.");
				return status;
			}
			final String pluginName = luaPluginName.checkjstring();
			if (!pluginName.equals(name)) {
				status.setStatus(false, String.format("The folder name '%s' does not match the key 'name' '%s'.", name, pluginName));
				return status;
			}
			if (pluginName.length() > 30) {
				status.setStatus(false, "Expected key 'name' to be less than 30 characters.");
				return status;
			}
			
			
			
			// Plugin Description
			final LuaValue luaPluginDescription = chunk.get("description");
			if (!luaPluginDescription.isstring()) {
				status.setStatus(false, "Expected key 'description' to be a string.");
				return status;
			}
			final String pluginDescription = luaPluginDescription.checkjstring();
			
			
			
			// Plugin Version Table
			final LuaValue luaPluginVersionTable = chunk.get("version");
			if (!luaPluginVersionTable.istable()) {
				status.setStatus(false, "Expected key 'version' to be a table.");
				return status;
			}
			final LuaValue pluginVersionMajorLua = luaPluginVersionTable.get("major");
			final LuaValue pluginVersionMinorLua = luaPluginVersionTable.get("minor");
			final LuaValue pluginVersionRevisionLua = luaPluginVersionTable.get("revision");
			final int pluginVersionMajor = pluginVersionMajorLua.checkint();
			final int pluginVersionMinor = pluginVersionMinorLua.checkint();
			final int pluginVersionRevision = pluginVersionRevisionLua.checkint();
			
			
			// Plugin Events to subscribe
			final LuaValue luaEventsTable = chunk.get("events");
			if (!luaEventsTable.istable()) {
				status.setStatus(false, "Expected key 'events' to be a table.");
				return status;
			}
			final LuaTable luaEvents = luaEventsTable.checktable();
			final HashMap<EventType, LuaFunction> subscribedEvents = new HashMap<EventType, LuaFunction>();
			for (LuaValue luaKey : luaEvents.keys()) {
				final String key = luaKey.checkjstring();
				
				for (final EventType evt : EventType.values()) {
					if (key.equals(evt.name())) {
						final LuaValue luaFn = luaEvents.get(key);
						
						if (!luaFn.isfunction()) {
							status.setStatus(false, String.format("Expected key 'events' to have a function associated with the event '%s'.", name, key));
							return status;
						}
						
						final LuaFunction fn = luaFn.checkfunction();
						subscribedEvents.put(evt, fn);
						
						// TODO: Change to interface?
						final ClankEvent clankLuaEvent = new ClankEvent(evt) {
							@Override
							public EventType getType() {
								return super.getType();
							}
						};
						
						status.addEvent(clankLuaEvent);
					}
					
					//else {
					//status.setStatus(false, String.format("Key 'events' has an invalid event type '%s'.", key));
					//return status;
					//}
				}
				
			}
			
			
			
			// Plugin run_on (emulation modes) int bit-field or table of strings
			try {
				final LuaValue luaEmulationMode = chunk.get("run_on");
				int emulationMode = 0;
				
				if (luaEmulationMode.isint()) {
					emulationMode = luaEmulationMode.checkint();
				} else if (luaEmulationMode.istable()) {
					
					for (final LuaValue luaKey : luaEmulationMode.checktable().keys()) {
						final String key = luaEmulationMode.checktable().get(luaKey).checkjstring();
						for (final EmulationMode mode : EmulationMode.values()) {
							if (mode.name().toUpperCase().equals(key)) {
								emulationMode = Utils.addToBitmask(mode.getValue(), emulationMode);
							}
						}
					}
					
					status.setEmulationModes(emulationMode);
				} else {
					status.setStatus(false, "Expected key 'run_on' to be an integer or table.");
					return status;
				}
			} catch (LuaError e) {
				status.setStatus(false, "Bad load sequence 'run_on'");
				return status;
			}
			
			
			
			// Plugin commands
			try {
				final LuaValue luaCommandsTable = chunk.get("commands");
				
				if (!luaCommandsTable.istable()) {
					status.setStatus(false, "Expected key 'events' to be a table.");
					return status;
				}
				
				for (final LuaValue luaCommandsTableKey : luaCommandsTable.checktable().keys()) {
					
					final String commandName = luaCommandsTableKey.checkjstring();
					final LuaValue value = luaCommandsTable.checktable().get(luaCommandsTableKey);
					
					if (!value.istable()) {
						status.setStatus(false, String.format("Expected key 'commands.%s' to be a table (currently %s).", commandName, value.typename()));
						return status;
					}
					
					final String commandDescription = value.get("description").checkjstring();
					final LuaFunction commandFunction = value.get("handler").checkfunction();
					
					final ICLICommand cmd = new ICLICommand() {
						@Override
						public String commandName() {
							return commandName;
						}
						@Override
						public String commandDescription() {
							return commandDescription;
						}
						@Override
						public void invoke(Terminal term, String[] params) {
							final LuaTable luaParams = new LuaTable();
							for (int i=0; i<params.length; i++) {
								luaParams.set(i+1, LuaValue.valueOf(params[i]));
							}
							commandFunction.call(luaParams);
						}
					};
					
					status.addCommand(cmd);
				}
			} catch (LuaError e) {
				status.setStatus(false, "Bad load sequence 'commands'");
				return status;
			}
			
			final LuaPlugin plugin = new LuaPlugin(
				pluginName,
				pluginDescription,
				pluginVersionMajor,
				pluginVersionMinor,
				pluginVersionRevision,
				status.getCommands(),
				subscribedEvents
			);
			
			status.setPlugin(plugin);
			status.setStatus(true, null);
			
			return status;
		} catch (IOException e) {
			status.setStatus(false, String.format("File I/O error: %s", e.getMessage()));
		} catch (LuaError e) {
			logger.warning("A Lua error has occurred: " + e.getMessage());
		}
	
		return status;
	}

	/**
	 * Unload a plugin by name.
	 * @param name
	 * @return
	 */
	public boolean unloadPlugin(final String name) {
		Iterator<IClankPlugin> pluginsIterator = plugins.iterator();
		
		while (pluginsIterator.hasNext()) {
			final IClankPlugin plugin = pluginsIterator.next();
			if (plugin.getName().equalsIgnoreCase(name)) {
				plugin.shutdown();
				pluginsIterator.remove();
				for (final ICLICommand cmd : plugin.getRegisteredCommands()) {
					Clank.getInstance().getTerminal().unregisterCommand(cmd);
				}
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Get all currently loaded plugins.
	 * @return
	 */
	public HashSet<IClankPlugin> getPlugins() {
		return plugins;
	}
	
	/**
	 * Return the server instance.
	 * @return
	 */
	public Clank getClank() {
		return clank;
	}
	
	private class PluginStatus {
		
		private boolean status;
		private String statusMessage;
		private HashSet<ClankEvent> events;
		private HashSet<ICLICommand> commands;
		private int emulationModes;
		private IClankPlugin plugin;
		
		private PluginStatus() {
			this.status = false;
			this.statusMessage = null;
			this.events = new HashSet<>();
			this.commands = new HashSet<>();
			this.emulationModes = 0;
			this.plugin = null;
		}
		
		protected void setStatus(boolean success, String statusMessage) {
			this.status = success;
			this.statusMessage = statusMessage;
		}
		
		protected void addEvent(ClankEvent evt) {
			events.add(evt);
		}
		
		protected void addCommand(ICLICommand cmd) {
			commands.add(cmd);
		}
		
		protected void setEmulationModes(int emulationModes) {
			this.emulationModes = emulationModes;
		}
		
		protected void setPlugin(IClankPlugin plugin) {
			this.plugin = plugin;
		}
		
		public boolean getStatus() {
			return status;
		}
		
		public String getStatusMessage() {
			return statusMessage;
		}
		
		public HashSet<ClankEvent> getEvents() {
			return events;
		}
		
		public HashSet<ICLICommand> getCommands() {
			return commands;
		}
		
		public int getEmulationModes() {
			return emulationModes;
		}
		
		public IClankPlugin getPlugin() {
			return plugin;
		}
	}
	
}
