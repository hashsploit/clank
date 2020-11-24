package net.hashsploit.clank;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.hashsploit.clank.cli.AnsiColor;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.cli.ICLIEvent;
import net.hashsploit.clank.cli.commands.CLIClientsCommand;
import net.hashsploit.clank.cli.commands.CLIExitCommand;
import net.hashsploit.clank.cli.commands.CLIHelpCommand;
import net.hashsploit.clank.config.AbstractConfig;
import net.hashsploit.clank.config.configs.DmeConfig;
import net.hashsploit.clank.config.configs.MediusConfig;
import net.hashsploit.clank.config.configs.NatConfig;
import net.hashsploit.clank.database.DbManager;
import net.hashsploit.clank.database.SimDb;
import net.hashsploit.clank.server.IServer;
import net.hashsploit.clank.server.common.MediusServer;
import net.hashsploit.clank.server.dme.DmeServer;
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
	private HashMap<String, DiscordWebhook> discordWebhooks;
	
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
		terminal.registerCommand(new CLIClientsCommand());
		
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
		logger.info(String.format("Initializing %s v%s ...", NAME, VERSION));
		
		String prompt;
		
		switch (config.getEmulationMode()) {
		case MEDIUS_AUTHENTICATION_SERVER:
			prompt = "MAS>";
			break;
		case MEDIUS_LOBBY_SERVER:
			prompt = "MLS>";
			break;
		case MEDIUS_PROXY_SERVER:
			prompt = "MPS>";
			break;
		case MEDIUS_UNIVERSE_INFORMATION_SERVER:
			prompt = "MUIS>";
			break;
		case NAT_SERVER:
			prompt = "NAT>";
		case DME_SERVER:
			prompt = AnsiColor.GREEN + "DME>";
			break;
		default:
			prompt = ">";
		}
		
		terminal.setPrompt(Terminal.colorize(prompt + AnsiColor.RESET) + " ");
		
		// Initialize database
		// TODO: Make SimDb a config option in each component (minus DME of course)
		db = new DbManager(this, new SimDb());
		
		// Set up Event Bus
		eventBus = new EventBus(this);
		
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
		
		
		
		// Create the server
		switch (config.getEmulationMode()) {
			case MEDIUS_AUTHENTICATION_SERVER:
			case MEDIUS_LOBBY_SERVER:
			case MEDIUS_PROXY_SERVER:
			case MEDIUS_UNIVERSE_INFORMATION_SERVER:
				MediusConfig mediusConfig = (MediusConfig) config;
				server = new MediusServer(
					mediusConfig.getEmulationMode(),
					mediusConfig.getAddress(),
					mediusConfig.getPort(),
					mediusConfig.getParentThreads(),
					mediusConfig.getChildThreads()
				);
				break;
			case NAT_SERVER:
				NatConfig natConfig = (NatConfig) config;
				server = new NatServer(
					natConfig.getAddress(),
					natConfig.getPort(),
					natConfig.getUdpThreads()
				);
				break;
			case DME_SERVER:
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
				logger.warning("No valid server component provided.");
				shutdown();
				return;
		}
		
		// Start server
		//server = new Server(config.getMediusComponent(), config.getAddress(), config.getPort(), config.getParentThreads(), config.getChildThreads());
		server.start();
		
		// Tick
		while (running) {
			
			update();
			
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// Discard
			}
		}
	}
	
	
	/**
	 * Tick the server for event updates.
	 */
	public void update() {
		
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
