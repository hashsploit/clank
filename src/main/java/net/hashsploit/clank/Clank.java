package net.hashsploit.clank;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.hashsploit.clank.cli.AnsiColor;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.cli.ICLIEvent;
import net.hashsploit.clank.cli.commands.CLIClientsCommand;
import net.hashsploit.clank.cli.commands.CLIExitCommand;
import net.hashsploit.clank.cli.commands.CLIHelpCommand;
import net.hashsploit.clank.server.Server;

public class Clank {
	
	public static final String NAME = "Clank";
	public static final String VERSION = "0.1.3";
	public static Clank instance;
	private static final Logger logger = Logger.getLogger("");
	
	private boolean running;
	private ClankConfig config;
	private Terminal terminal;
	private Server server;
	private EventBus eventBus;
	private HashMap<String, DiscordWebhook> discordWebhooks;
	
	public Clank(ClankConfig config) {
		
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
		
		terminal.init();
		logger.info("Initializing " + NAME + " v" + VERSION + " ...");
		
		String prompt;
		
		switch (config.getMediusComponent()) {
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
		case DME_PROXY:
			prompt = AnsiColor.GREEN + "DME_PROXY>";
			break;
		default:
			prompt = ">";
		}
		
		terminal.setPrompt(Terminal.colorize(prompt + AnsiColor.RESET) + " ");
		
		// Set up Event Bus
		eventBus = new EventBus(this);
		
		// Configure Discord Webhook callbacks
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
		
		// Start server
		server = new Server(config.getMediusComponent(), config.getAddress(), config.getPort(), config.getParentThreads(), config.getChildThreads());
		server.start();
		
		// Tick
		while (running) {
			
			update();
			
			try {
				Thread.sleep(250);
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
		server.stop();
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
	public ClankConfig getConfig() {
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
	 * Get the current server instance.
	 * @return
	 */
	public Server getServer() {
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
