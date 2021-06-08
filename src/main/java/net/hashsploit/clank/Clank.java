package net.hashsploit.clank;

import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import net.hashsploit.clank.cli.AnsiColor;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.cli.ICLIEvent;
import net.hashsploit.clank.cli.commands.CLIBroadcastCommand;
import net.hashsploit.clank.cli.commands.CLIExitCommand;
import net.hashsploit.clank.cli.commands.CLIHelpCommand;
import net.hashsploit.clank.cli.commands.CLIPlayersCommand;
import net.hashsploit.clank.cli.commands.CLIPluginsCommand;
import net.hashsploit.clank.cli.commands.CLIVersionCommand;
import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.config.configs.MlsConfig;
import net.hashsploit.clank.config.configs.NatConfig;
import net.hashsploit.clank.config.objects.DatabaseInfo;
import net.hashsploit.clank.database.DbManager;
import net.hashsploit.clank.database.MariaDb;
import net.hashsploit.clank.database.SimDb;
import net.hashsploit.clank.plugin.PluginManager;
import net.hashsploit.clank.server.IServer;
import net.hashsploit.clank.server.dme.DmeServer;
import net.hashsploit.clank.server.medius.MediusAuthenticationServer;
import net.hashsploit.clank.server.medius.MediusLobbyServer;
import net.hashsploit.clank.server.muis.MediusUniverseInformationServer;
import net.hashsploit.clank.server.nat.NatServer;
import net.hashsploit.clank.utils.Utils;

public class Clank {

	public static final String NAME = "Clank";
	public static final String VERSION = "0.1.8";
	public static Clank instance;
	private static final Logger logger = Logger.getLogger(Clank.class.getName());

	private boolean running;
	private AbstractConfig config;
	private Terminal terminal;
	private IServer server;
	private DbManager db;
	private EventBus eventBus;
	private PluginManager pluginManager;

	public Clank(AbstractConfig config) {

		if (instance != null) {
			return;
		}

		instance = this;
		this.config = config;
		this.running = true;

		// Initialize server
		System.out.println("Initializing ...");

		Security.addProvider(new BouncyCastleProvider());

		terminal = new Terminal();
		terminal.setLevel(config.getLogLevel());
		terminal.registerCommand(new CLIExitCommand());
		terminal.registerCommand(new CLIHelpCommand());
		terminal.registerCommand(new CLIVersionCommand());
		terminal.registerCommand(new CLIPluginsCommand());

		terminal.registerEvent(new ICLIEvent() {

			@Override
			public void userInterruptEvent() {
				shutdown();
			}

			@Override
			public void onReturnEvent(String line) {

			}

			@Override
			public void onCommandEvent(String command, String[] params, ICLICommand handler) {

			}

			@Override
			public void eofInterruptEvent() {
				// TODO: replace with issuing the stats command.
				logger.warning("Caught ^D EOL, use 'exit' to shutdown the server.");
			}
		});

		// Disable other framework logging from clogging up the console logs.
		Logger.getLogger("io.netty").setLevel(Level.OFF);
		Logger.getLogger("javax.net").setLevel(Level.OFF);
		Logger.getLogger("io.grpc").setLevel(Level.OFF);
		Logger.getLogger("io.perfmark.impl").setLevel(Level.OFF);

		terminal.init();

		// Check if file logging is enabled
		if (config.getFileLogLevel() != null) {
			terminal.startFile("logs/" + config.getEmulationMode().name().toLowerCase() + "/latest.log", config.getFileLogLevel());
		}

		logger.info(String.format("%s v%s (starting %s)", NAME, VERSION, config.getEmulationMode().name()));
		
		final int mediusBitmask = EmulationMode.MEDIUS_AUTHENTICATION_SERVER.value | EmulationMode.MEDIUS_LOBBY_SERVER.value | EmulationMode.MEDIUS_UNIVERSE_INFORMATION_SERVER.value;
		
		// This is a *Medius server, set up the generic Medius components
		MediusConfig mediusConfig = null;
		if ((config.getEmulationMode().value & mediusBitmask) != 0) {
			
			// Register generic *Medius CLI commands
			terminal.registerCommand(new CLIPlayersCommand());

			// Set up the basic configuration for *Medius servers
			mediusConfig = (MediusConfig) config;
		}

		String terminalPrompt = ">";

		// Configure the server specifics
		switch (config.getEmulationMode()) {
			case MEDIUS_UNIVERSE_INFORMATION_SERVER:
				terminalPrompt = "MUIS>";
				server = new MediusUniverseInformationServer(
					mediusConfig.getAddress(),
					mediusConfig.getPort(),
					mediusConfig.getParentThreads(),
					mediusConfig.getChildThreads()
				);
				break;
			case MEDIUS_AUTHENTICATION_SERVER:
				terminalPrompt = "MAS>";
				server = new MediusAuthenticationServer(
					mediusConfig.getAddress(),
					mediusConfig.getPort(),
					mediusConfig.getParentThreads(),
					mediusConfig.getChildThreads()
				);
				break;
			case MEDIUS_LOBBY_SERVER:
				terminalPrompt = "MLS>";
				terminal.registerCommand(new CLIBroadcastCommand());
				server = new MediusLobbyServer(
					mediusConfig.getAddress(),
					mediusConfig.getPort(),
					mediusConfig.getParentThreads(),
					mediusConfig.getChildThreads()
				);
				MlsConfig mlsConfig = (MlsConfig) config;
				//db = new DbManager(this, new SimDb());
				DatabaseInfo dbInfo = mlsConfig.getDatabaseInfo();
				if (dbInfo.getMode().equalsIgnoreCase("MariaDb")) {
					db = new DbManager(this, new MariaDb(dbInfo));
				}
				else {
					db = new DbManager(this, new SimDb());
				}
				break;
			case NAT_SERVER:
				terminalPrompt = "NAT>";
				NatConfig natConfig = (NatConfig) config;
				server = new NatServer(natConfig.getAddress(), natConfig.getPort(), natConfig.getUdpThreads());
				break;
			case DME_SERVER:
				terminalPrompt = AnsiColor.GREEN + "DME>";
				terminal.registerCommand(new CLIBroadcastCommand());
				DmeConfig dmeConfig = (DmeConfig) config;
				server = new DmeServer(dmeConfig.getTcpAddress(), dmeConfig.getTcpPort(), dmeConfig.getParentThreads(), dmeConfig.getChildThreads(), dmeConfig.getUdpAddress(), dmeConfig.getUdpStartingPort(), dmeConfig.getUdpThreads(), dmeConfig.getTimeout());
				break;
			default:
				logger.severe("No valid server component provided.");
				shutdown();
				return;
		}

		// Set up the event bus
		eventBus = new EventBus(this);
		
		// Start Plugin Manager
		logger.info("Loading plugin manager ...");
		pluginManager = new PluginManager(this);
		
		// Loading available plugins
		logger.info("Loading plugins ...");
		pluginManager.loadAllAvailablePlugins();
		
		// Start server
		logger.info("Starting server ...");
		terminal.setPrompt(Terminal.colorize(terminalPrompt + AnsiColor.RESET) + " ");
		server.start();
	}

	/**
	 * Gracefully shutdown Clank.
	 */
	public void shutdown() {
		running = false;
		terminal.print(Level.INFO, "Shutting down ...");
		if (server != null) {
			server.stop();
		}
		terminal.shutdown();
		System.exit(0);
	}

	/**
	 * Get the event bus.
	 * 
	 * @return
	 */
	public EventBus getEventBus() {
		return eventBus;
	}

	/**
	 * Get the Clank configuration class.
	 * 
	 * @return
	 */
	public AbstractConfig getConfig() {
		return config;
	}

	/**
	 * Get the current terminal instance.
	 * 
	 * @return
	 */
	public Terminal getTerminal() {
		return terminal;
	}

	/**
	 * Get the current database instance.
	 * 
	 * @return
	 */
	public DbManager getDatabase() {
		return db;
	}
	
	/**
	 * Get the plugin manager instance.
	 * 
	 * @return
	 */
	public PluginManager getPluginManager() {
		return pluginManager;
	}

	/**
	 * Get the current server instance.
	 * 
	 * @return
	 */
	public IServer getServer() {
		return server;
	}

	/**
	 * Return the current running instance of Clank.
	 * 
	 * @return
	 */
	public static Clank getInstance() {
		return instance;
	}

}
