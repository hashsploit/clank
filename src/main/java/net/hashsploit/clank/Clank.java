package net.hashsploit.clank;

import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.hashsploit.clank.cli.AnsiColor;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.cli.ICLIEvent;
import net.hashsploit.clank.cli.commands.CLIBroadcastCommand;
import net.hashsploit.clank.cli.commands.CLIExitCommand;
import net.hashsploit.clank.cli.commands.CLIHelpCommand;
import net.hashsploit.clank.cli.commands.CLIPlayersCommand;
import net.hashsploit.clank.cli.commands.CLIVersionCommand;
import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.config.configs.NatConfig;
import net.hashsploit.clank.database.DbManager;
import net.hashsploit.clank.database.SimDb;
import net.hashsploit.clank.server.IServer;
import net.hashsploit.clank.server.common.MediusServer;
import net.hashsploit.clank.server.dme.DmePlayer;
import net.hashsploit.clank.server.dme.DmeServer;
import net.hashsploit.clank.server.dme.DmeWorld;
import net.hashsploit.clank.server.dme.DmeWorldManager;
import net.hashsploit.clank.server.nat.NatServer;

public class Clank {
	
	public static final String NAME = "Clank";
	public static final String VERSION = "0.1.5";
	public static Clank instance;
	private static final Logger logger = Logger.getLogger("");
	
	private boolean running;
	private AbstractConfig config;
	private Terminal terminal;
	private IServer server;
	private DbManager db;
	private EventBus eventBus;
	//private HashMap<String, DiscordWebhook> discordWebhooks;
	
	public Clank(AbstractConfig config) {
		
		if (instance != null) {
			return;
		}
		
		instance = this;
		this.config = config;
		this.running = true;
		
		// Configure terminal
		System.out.println("Initializing ...");
		terminal = new Terminal();
		terminal.setLevel(config.getLogLevel());
		terminal.registerCommand(new CLIExitCommand());
		terminal.registerCommand(new CLIHelpCommand());
		terminal.registerCommand(new CLIVersionCommand());
		
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
		
		terminal.init();
		logger.info(String.format("%s v%s (starting %s)", NAME, VERSION, config.getEmulationMode().name()));
		
		
		// FIXME: Configure Discord Webhook callbacks
		/*
		for (final Object objectKey : config.getProperties().keySet()) {
			final String key = objectKey.toString();
			if (key.startsWith("DiscordWebhook_")) {
				if (config.getProperties().getProperty(key) != null && !config.getProperties().getProperty(key).isEmpty()) {
					final String urlString = config.getProperties().getProperty(key);
					
					try {
						final URL url = new URL(urlString);
						discordWebhooks.put(key.replace("DiscordWebhook_", ""), new DiscordWebhook(url));
					} catch (MalformedURLException e) {
						logger.log(Level.SEVERE, "Configuration value '" + key + "' requires a valid Discord Webhook URL.");
						return;
					}
					
				}
			}
		}
		*/
		
		final int mediusBitmask = EmulationMode.MEDIUS_AUTHENTICATION_SERVER.getValue() | EmulationMode.MEDIUS_LOBBY_SERVER.getValue() | EmulationMode.MEDIUS_PROXY_SERVER.getValue() | EmulationMode.MEDIUS_UNIVERSE_INFORMATION_SERVER.getValue();
		
		// This is a *Medius server, set up the generic Medius components
		if ((config.getEmulationMode().getValue() & mediusBitmask) != 0) {
			
			// Register generic *Medius CLI commands
			terminal.registerCommand(new CLIPlayersCommand());
			
			// Set up the basic configuration for *Medius servers
			MediusConfig mediusConfig = (MediusConfig) config;
			server = new MediusServer(
				mediusConfig.getEmulationMode(),
				mediusConfig.getAddress(),
				mediusConfig.getPort(),
				mediusConfig.getParentThreads(),
				mediusConfig.getChildThreads()
			);
			
			// Initialize the database for generic Medius servers
			// TODO: check if the database config value is null, if so
			// this shouild use the SimDb object instead of the regular MySQL object.
			db = new DbManager(this, new SimDb());
		}
		
		String terminalPrompt = ">";
		
		// Configure the server specifics
		switch (config.getEmulationMode()) {
			case MEDIUS_AUTHENTICATION_SERVER:
				terminalPrompt = "MAS>";
				break;
			case MEDIUS_LOBBY_SERVER:
				terminalPrompt = "MLS>";
				terminal.registerCommand(new CLIBroadcastCommand());
				break;
			case MEDIUS_PROXY_SERVER:
				terminalPrompt = "MPS>";
				break;
			case MEDIUS_UNIVERSE_INFORMATION_SERVER:
				terminalPrompt = "MUIS>";
				break;
			case NAT_SERVER:
				terminalPrompt = "NAT>";
				NatConfig natConfig = (NatConfig) config;
				server = new NatServer(
					natConfig.getAddress(),
					natConfig.getPort(),
					natConfig.getUdpThreads()
				);
				break;
			case DME_SERVER:
				terminalPrompt = AnsiColor.GREEN + "DME>";
				terminal.registerCommand(new CLIBroadcastCommand());
				DmeConfig dmeConfig = (DmeConfig) config;
				server = new DmeServer(
					dmeConfig.getTcpAddress(),
					dmeConfig.getTcpPort(),
					dmeConfig.getParentThreads(),
					dmeConfig.getChildThreads(),
					dmeConfig.getUdpAddress(),
					dmeConfig.getUdpStartingPort(),
					dmeConfig.getUdpThreads()
				);
				break;
			default:
				logger.severe("No valid server component provided.");
				shutdown();
				return;
		}
		
		// Set up the event bus
		eventBus = new EventBus(this);
		
		
		// Tick
		Executors.newSingleThreadExecutor().submit(() -> {
			while (running) {
				update();
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// Discard
				}
			}
		});
		
		
		// Start server
		terminal.setPrompt(Terminal.colorize(terminalPrompt + AnsiColor.RESET) + " ");
		server.start();
	}
	
	
	/**
	 * Tick the server for internal event updates.
	 */
	public void update() {
		// Configure the server specifics
		switch (config.getEmulationMode()) {
			case MEDIUS_AUTHENTICATION_SERVER:
				break;
			case MEDIUS_LOBBY_SERVER:
				break;
			case MEDIUS_PROXY_SERVER:
				break;
			case MEDIUS_UNIVERSE_INFORMATION_SERVER:
				break;
			case NAT_SERVER:
				break;
			case DME_SERVER:
				dmeUpdate();
				break;
			default:
				return;
		}
	}
		
	public void dmeUpdate() {
		DmeWorldManager dmeWorldManager = ((DmeServer) server).getDmeWorldManager();
		for (DmeWorld dmeWorld : dmeWorldManager.getWorlds()) {
			for (DmePlayer dmePlayer: dmeWorld.getPlayers()) {
				dmePlayer.flushUdpData();
			}
		}
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
	 * @return
	 */
	public EventBus getEventBus() {
		return eventBus;
	}
	
	/**
	 * Get the Clank configuration class.
	 * @return
	 */
	public AbstractConfig getConfig() {
		return config;
	}
	
	/**
	 * Get the current terminal instance.
	 * @return
	 */
	public Terminal getTerminal() {
		return terminal;
	}
	
	/**
	 * Get the current database instance.
	 * @return
	 */
	public DbManager getDatabase() {
		return db;
	}
	
	/**
	 * Get the current server instance.
	 * @return
	 */
	public IServer getServer() {
		return server;
	}
	
	/**
	 * Return the current running instance of Clank.
	 * @return
	 */
	public static Clank getInstance() {
		return instance;
	}
	
}
