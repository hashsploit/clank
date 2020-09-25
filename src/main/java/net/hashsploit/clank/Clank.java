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
import net.hashsploit.clank.server.IServer;
import net.hashsploit.clank.server.MediusClientChannelInitializer;
import net.hashsploit.clank.server.dme.DmeServer;
import net.hashsploit.clank.server.medius.MediusServer;
import net.hashsploit.clank.server.nat.NatServer;

public class Clank {
	
	public static final String NAME = "Clank";
	public static final String VERSION = "0.1.3";
	public static Clank instance;
	private static final Logger logger = Logger.getLogger("");
	
	private boolean running;
	private ClankConfig config;
	private Terminal terminal;
	private IServer server;
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
		logger.info(String.format("Initializing %s v%s ...", NAME, VERSION));
		
		String prompt;
		
		switch (config.getServerComponent()) {
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
		
		// Create the server
		switch (config.getServerComponent()) {
			case MEDIUS_AUTHENTICATION_SERVER:
			case MEDIUS_LOBBY_SERVER:
			case MEDIUS_PROXY_SERVER:
			case MEDIUS_UNIVERSE_INFORMATION_SERVER:
				server = new MediusServer(
					config.getServerComponent(),
					config.getAddress(),
					config.getPort(),
					config.getParentThreads(),
					config.getChildThreads()
				);
				break;
			case NAT_SERVER:
				server = new NatServer(
					config.getAddress(),
					config.getPort(),
					Integer.parseInt(config.getProperties().getProperty("Threads"))
				);
				break;
			case DME_SERVER:
				server = new DmeServer(
					config.getAddress(),
					config.getPort(),
					config.getParentThreads(),
					config.getChildThreads(),
					config.getProperties().getProperty("UdpAddress"),
					Integer.parseInt(config.getProperties().getProperty("UdpStartingPort")),
					Integer.parseInt(config.getProperties().getProperty("UdpThreads"))
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
