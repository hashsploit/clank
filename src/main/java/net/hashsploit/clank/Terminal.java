package net.hashsploit.clank;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;

import jline.console.ConsoleReader;
import jline.console.UserInterruptException;
import jline.console.completer.Completer;
import jline.console.history.History;
import net.hashsploit.clank.cli.AnsiColor;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.cli.ICLIEvent;
import net.hashsploit.clank.cli.ICLIInvalidCommand;

public final class Terminal {

	private static final String CONSOLE_DATE = "HH:mm:ss.SSS";
	private static final String FILE_DATE = "yyyy/MM/dd HH:mm:ss.SSS";
	private static Logger logger = Logger.getLogger("");
	private static Logger fileLogger = Logger.getLogger("");
	private final Terminal instance;
	private Thread thread;
	private String prompt;
	private ConsoleReader reader;
	private boolean running;

	private List<ICLICommand> registeredCommands;
	private List<ICLIEvent> registeredEvents;
	private static Map<AnsiColor, String> replacements;
	private ICLIInvalidCommand invalidCommandHandler;
	private Executor executor;

	public Terminal() {
		instance = this;
		thread = new Thread(new ConsoleCommandThread());
		thread.setName("cli-terminal");
		thread.setDaemon(true);
		prompt = "> ";
		running = false;
		replacements = new EnumMap<>(AnsiColor.class);
		registeredCommands = new ArrayList<ICLICommand>();
		registeredEvents = new ArrayList<ICLIEvent>();
		invalidCommandHandler = null;
		executor = Executors.newSingleThreadExecutor();

		// Install ANSI Console if arch is not x86-64
		if (!System.getProperty("os.arch").equals("aarch64")){
			AnsiConsole.systemInstall();
		}

		// Remove all other handlers
		for (Handler h : logger.getHandlers()) {
			logger.removeHandler(h);
		}

		// add log handler which writes to console
		logger.addHandler(new FancyConsoleHandler());

		// set up coloring replacements
		if (replacements.isEmpty()) {
			replacements.put(AnsiColor.BLACK, Ansi.ansi().a(Attribute.RESET).fg(Color.BLACK).boldOff().toString());
			replacements.put(AnsiColor.DARK_BLUE, Ansi.ansi().a(Attribute.RESET).fg(Color.BLUE).boldOff().toString());
			replacements.put(AnsiColor.DARK_GREEN, Ansi.ansi().a(Attribute.RESET).fg(Color.GREEN).boldOff().toString());
			replacements.put(AnsiColor.DARK_AQUA, Ansi.ansi().a(Attribute.RESET).fg(Color.CYAN).boldOff().toString());
			replacements.put(AnsiColor.DARK_RED, Ansi.ansi().a(Attribute.RESET).fg(Color.RED).boldOff().toString());
			replacements.put(AnsiColor.PURPLE, Ansi.ansi().a(Attribute.RESET).fg(Color.MAGENTA).boldOff().toString());
			replacements.put(AnsiColor.GOLD, Ansi.ansi().a(Attribute.RESET).fg(Color.YELLOW).boldOff().toString());
			replacements.put(AnsiColor.GRAY, Ansi.ansi().a(Attribute.RESET).fg(Color.WHITE).boldOff().toString());
			replacements.put(AnsiColor.DARK_GRAY, Ansi.ansi().a(Attribute.RESET).fg(Color.BLACK).bold().toString());
			replacements.put(AnsiColor.BLUE, Ansi.ansi().a(Attribute.RESET).fg(Color.BLUE).bold().toString());
			replacements.put(AnsiColor.GREEN, Ansi.ansi().a(Attribute.RESET).fg(Color.GREEN).bold().toString());
			replacements.put(AnsiColor.AQUA, Ansi.ansi().a(Attribute.RESET).fg(Color.CYAN).bold().toString());
			replacements.put(AnsiColor.RED, Ansi.ansi().a(Attribute.RESET).fg(Color.RED).bold().toString());
			replacements.put(AnsiColor.PINK, Ansi.ansi().a(Attribute.RESET).fg(Color.MAGENTA).bold().toString());
			replacements.put(AnsiColor.YELLOW, Ansi.ansi().a(Attribute.RESET).fg(Color.YELLOW).bold().toString());
			replacements.put(AnsiColor.WHITE, Ansi.ansi().a(Attribute.RESET).fg(Color.WHITE).bold().toString());
			replacements.put(AnsiColor.MAGIC, Ansi.ansi().a(Attribute.BLINK_SLOW).toString());
			replacements.put(AnsiColor.BOLD, Ansi.ansi().a(Attribute.UNDERLINE_DOUBLE).toString());
			replacements.put(AnsiColor.STRIKETHROUGH, Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString());
			replacements.put(AnsiColor.UNDERLINE, Ansi.ansi().a(Attribute.UNDERLINE).toString());
			replacements.put(AnsiColor.ITALIC, Ansi.ansi().a(Attribute.ITALIC).toString());
			replacements.put(AnsiColor.RESET, Ansi.ansi().a(Attribute.RESET).toString());
		}

		// Create the console handler
		try {
			reader = new ConsoleReader();
			reader.setHistoryEnabled(true);
			reader.setExpandEvents(false);
			reader.setBellEnabled(true);
			reader.setHandleUserInterrupt(true);
			reader.setPrompt(prompt);
			reader.addCompleter(new CommandCompleter());
		} catch (Exception e) {
			handleException(e);
		}

		System.setOut(new PrintStream(new LoggerOutputStream(Level.INFO), true));
		System.setErr(new PrintStream(new LoggerOutputStream(Level.WARNING), true));

		for (Handler handler : logger.getHandlers()) {
			if (handler.getClass() == FancyConsoleHandler.class) {
				handler.setFormatter(new ConsoleDateOutputFormatter(CONSOLE_DATE, true));
			}
		}

		// Add shutdown hook to reset terminal before system exit
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					if (reader != null) {
						reader.getTerminal().restore();
						reader.println();
						reader = null;
					}
				} catch (Exception e) {}
			}
		});
	}
	
	
	/**
	 * Adds a console-log handler writing to the given file.
	 *
	 * @param logfile the file path
	 */
	public void startFile(String logfile, Level level) {
		File parent = new File(logfile).getParentFile();
		if (!parent.isDirectory() && !parent.mkdirs()) {
			logger.warning("Could not create log folder: " + parent);
		}
		Handler fileHandler = new RotatingFileHandler(logfile);
		fileHandler.setFormatter(new DateOutputFormatter(FILE_DATE, false));
		fileHandler.setLevel(level);
		fileLogger.setLevel(level);
		fileLogger.addHandler(fileHandler);
	}

	/**
	 * Get the console reader object.
	 * @return
	 */
	public ConsoleReader getConsoleReader() {
		return reader;
	}

	/**
	 * Initialize the terminal.
	 */
	public void init() {
		if (running) {
			return;
		}
		running = true;
		thread.start();
	}

	/**
	 * Shutdown the terminal.
	 */
	public synchronized void shutdown() {
		AnsiConsole.out().println();
		//AnsiConsole.systemUninstall();
		if (!running) {
			return;
		}
		running = false;
		for (final Handler handler : logger.getHandlers()) {
			handler.flush();
			handler.close();
		}
		for (final Handler handler : fileLogger.getHandlers()) {
			handler.flush();
			handler.close();
		}
		if (reader != null) {
			try {
				reader.getTerminal().restore();
				reader.println();
				reader = null;
			} catch (Exception e) {}
		}
	}

	/**
	 * Invoke the print function with a specific level and a message.
	 * 
	 * @param level
	 * @param message
	 */
	public void print(Level level, String message) {
		logger.log(level, message);
	}

	/**
	 * Get a list of registered commands.
	 * @return
	 */
	public synchronized List<ICLICommand> getRegisteredCommands() {
		return new ArrayList<ICLICommand>(registeredCommands);
	}

	/**
	 * Get a list of registered events.
	 * @return
	 */
	public synchronized List<ICLIEvent> getRegisteredEvents() {
		return new ArrayList<ICLIEvent>(registeredEvents);
	}

	/**
	 * Set the invalid command handler.
	 * @param handler
	 */
	public synchronized void setInvalidCommandHandler(ICLIInvalidCommand handler) {
		this.invalidCommandHandler = handler;
	}

	/**
	 * Get the current invalid command handler.
	 * @return
	 */
	public ICLIInvalidCommand getInvalidCommandHandler() {
		return invalidCommandHandler;
	}

	/**
	 * Register a command with the terminal (add a command).
	 * 
	 * @param command
	 */
	public synchronized void registerCommand(ICLICommand command) {

		// Do not allow a command name of null to exist
		if (command.commandName() == null) {
			print(Level.WARNING, "REGISTER: Failed for " + command.toString() + " null name");
			return;
		}

		// Do not allow a command of the same name to exist
		for (ICLICommand t : registeredCommands) {
			if (t.commandName().equalsIgnoreCase(command.commandName())) {
				print(Level.WARNING, "REGISTER: Failed for " + command.toString() + " command name already taken");
				return;
			}
		}

		this.registeredCommands.add(command);
	}

	/**
	 * Register a event with the terminal.
	 * 
	 * @param command
	 */
	public synchronized void registerEvent(ICLIEvent event) {

		if (event == null) {
			print(Level.WARNING, "REGISTER: Attempted to register a null event");
			return;
		}

		this.registeredEvents.add(event);
	}

	/**
	 * Unregister a command from the terminal (remove a command).
	 * 
	 * @param command
	 */
	public synchronized void unregisterCommand(ICLICommand command) {
		this.registeredCommands.remove(command);
	}

	/**
	 * Unregister a command from the terminal (remove a command).
	 * 
	 * @param command
	 */
	public synchronized void unregisterEvent(ICLIEvent event) {
		this.registeredEvents.remove(event);
	}

	/**
	 * Set the logging level.
	 * @param level
	 */
	public synchronized void setLevel(Level level) {
		logger.setLevel(level);
		for (Handler handler : logger.getHandlers()) {
			handler.setLevel(level);
		}
	}

	/**
	 * Get the current logging level.
	 * @return
	 */
	public Level getLevel() {
		return logger.getLevel();
	}

	/**
	 * Set the terminal prompt.
	 * @param prompt
	 */
	public void setPrompt(String prompt) {
		try {
			this.prompt = prompt;
			reader.setPrompt(prompt);
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Set if terminal history should be enabled.
	 * @param enabled
	 */
	public void setHistoryEnabled(boolean enabled) {
		try {
			reader.setHistoryEnabled(enabled);
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Set the terminal history.
	 * @param history
	 */
	public void setHistory(History history) {
		try {
			reader.setHistory(history);
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * Get the terminal history.
	 * @return
	 */
	public History getHistory() {
		return reader.getHistory();
	}

	/**
	 * Pretty print an exception.
	 * @param e
	 */
	public void handleException(Throwable e) {
		print(Level.SEVERE, "#### BEGIN EXCEPTION ####");
		print(Level.SEVERE, "Exception Parameters {");
		print(Level.SEVERE, "\t* Call Thread: " + Thread.currentThread().getName());
		print(Level.SEVERE, "\t* Exception Cause: " + (e.getCause() == null ? "<UNKNOWN>" : e.getCause().toString()));
		print(Level.SEVERE, "\t* Exception Message: \"" + e.getMessage() + "\"");
		print(Level.SEVERE, "\t* Trace Length: " + e.getStackTrace().length);
		print(Level.SEVERE, "}");
		print(Level.SEVERE, "");
		print(Level.SEVERE, "Stack Trace {");
		for (int i = 0; i < e.getStackTrace().length - 1; i++) {
			String s = "| ";
			if (i == e.getStackTrace().length - 2) {
				s = "|>";
			}
			print(Level.SEVERE,
					"\t" + s + " #" + (i + 1) + " " + e.getStackTrace()[(e.getStackTrace().length - 1) - i].toString());
		}
		print(Level.SEVERE, "}");

		if (logger.getLevel() == Level.FINEST) {
			print(Level.SEVERE, "");
			print(Level.SEVERE, "Master Stack Trace Tree {");
			for (Thread t : Thread.getAllStackTraces().keySet()) {
				StackTraceElement[] v = Thread.getAllStackTraces().get(t);
				if (v.length > 0) {
					print(Level.SEVERE, "\tThread \"" + t.getName() + "\"");
					for (int i = 0; i < v.length - 1; i++) {
						String s = "| ";
						if (i == v.length - 2) {
							s = "|>";
						}
						print(Level.SEVERE, "\t\t" + s + " #" + (i + 1) + " " + v[(v.length - 1) - i].toString());
					}
					print(Level.SEVERE, "");
				}
			}
			print(Level.SEVERE, "}");
		}

		print(Level.SEVERE, "#### END EXCEPTION ####");
	}

	/**
	 * Get a list of terminal tab completers.
	 * @param buffer
	 * @return
	 */
	private List<String> getTabCompleters(String buffer) {
		List<String> list = new ArrayList<String>();

		for (ICLICommand cmd : this.registeredCommands) {
			if (cmd.commandName().startsWith(buffer)) {
				list.add(cmd.commandName());
			}
		}

		return list;
	}

	/**
	 * Return a String of text that has been converted to valid terminal colors. 
	 * @param string
	 * @return
	 */
	public static String colorize(String string) {
		if (string.indexOf(AnsiColor.COLOR_CHAR) < 0) {
			// no colors in the message
			return string;
		} else {
			// colorize or strip all colors
			for (AnsiColor color : AnsiColor.values()) {
				if (replacements.containsKey(color)) {
					string = string.replaceAll("(?i)" + color, replacements.get(color));
				} else {
					string = string.replaceAll("(?i)" + color, "");
				}
			}
			return string + Ansi.ansi().reset();
		}
	}

	private class CommandCompleter implements Completer {
		@Override
		public int complete(String buffer, int cursor, List<CharSequence> candidates) {
			try {
				List<String> completions = getTabCompleters(buffer);

				if (completions == null) {
					return cursor; // no completions
				}

				if (completions.size() == 0) {
					return cursor; // no completions
				}

				candidates.addAll(completions);

				// location to position the cursor at (before auto filling takes place)
				return buffer.lastIndexOf(' ') + 1;
			} catch (Throwable t) {
				return cursor;
			}
		}
	}

	private class FancyConsoleHandler extends ConsoleHandler {

		public FancyConsoleHandler() {
			setFormatter(new ConsoleDateOutputFormatter(CONSOLE_DATE, true));
			setOutputStream(System.out);
			setLevel(Level.ALL);
		}

		@Override
		public synchronized void flush() {
			try {
				if (!running) {
					return;
				}
				
				reader.print(ConsoleReader.RESET_LINE + "");
				reader.flush();
				super.flush();

				try {
					reader.drawLine();
				} catch (Throwable ex) {
					reader.getCursorBuffer().clear();
				}

				reader.flush();
			} catch (IOException ex) {
				System.err.println(ex.getMessage());
			}
		}
	}
	
	private static class RotatingFileHandler extends StreamHandler {

		private final SimpleDateFormat dateFormat;
		private final String template;
		private final boolean rotate;
		private String filename;

		public RotatingFileHandler(String template) {
			this.template = template;
			rotate = template.contains("%D");
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			filename = calculateFilename();
			updateOutput();
		}

		private void updateOutput() {
			try {
				setOutputStream(new FileOutputStream(filename, true));
			} catch (IOException ex) {
				logger.log(Level.SEVERE, "Unable to open " + filename + " for writing", ex);
			}
		}

		private void checkRotate() {
			if (rotate) {
				String newFilename = calculateFilename();
				if (!filename.equals(newFilename)) {
					filename = newFilename;
					// note that the console handler doesn't see this message
					super.publish(new LogRecord(Level.INFO, "Log rotating to: " + filename));
					updateOutput();
				}
			}
		}

		private String calculateFilename() {
			return template.replace("%D", dateFormat.format(new Date()));
		}

		@Override
		public synchronized void publish(LogRecord record) {
			if (!isLoggable(record)) {
				return;
			}
			checkRotate();
			super.publish(record);
			super.flush();
		}

		@Override
		public synchronized void flush() {
			checkRotate();
			super.flush();
		}
	}

	private static class LoggerOutputStream extends ByteArrayOutputStream {
		private final String separator = System.getProperty("line.separator");
		private final Level level;

		public LoggerOutputStream(Level level) {
			this.level = level;
		}

		@Override
		public synchronized void flush() throws IOException {
			super.flush();
			String record = toString();
			reset();

			if (!record.isEmpty() && !record.equals(separator)) {
				logger.logp(Level.INFO, "LoggerOutputStream", "log" + level, record);
			}
		}
	}

	private class DateOutputFormatter extends Formatter {
		private final SimpleDateFormat date;
		private final boolean color;

		public DateOutputFormatter(String pattern, boolean color) {
			date = new SimpleDateFormat(pattern);
			this.color = color;
		}

		@Override
		public String format(LogRecord record) {
			StringBuilder builder = new StringBuilder();

			builder.append(date.format(record.getMillis())).append(' ');
			
			if (color) {
				if (record.getLevel().intValue() <= Level.CONFIG.intValue()) {
					builder.append(AnsiColor.BLUE);
				} else if (record.getLevel().intValue() == Level.INFO.intValue()) {
					// do nothing
				} else if (record.getLevel().intValue() == Level.WARNING.intValue()) {
					builder.append(AnsiColor.YELLOW);
				} else if (record.getLevel().intValue() >= Level.SEVERE.intValue()) {
					builder.append(AnsiColor.RED);
				}
			}

			builder.append('[');
			builder.append(record.getLevel().getLocalizedName().toUpperCase());
			builder.append("]" + AnsiColor.RESET + " ");

			if (color) {
				builder.append(colorize(AnsiColor.RESET + formatMessage(record) + AnsiColor.RESET));
			} else {
				builder.append(formatMessage(record));
			}

			builder.append('\n');

			if (record.getThrown() != null) {
				// StringWriter's close() is trivial
				StringWriter writer = new StringWriter();
				record.getThrown().printStackTrace(new PrintWriter(writer));
				builder.append(writer);
			}

			return AnsiColor.stripColor(builder.toString());
		}
	}
	
	private class ConsoleDateOutputFormatter extends Formatter {
		private final SimpleDateFormat date;
		private final boolean color;

		public ConsoleDateOutputFormatter(String pattern, boolean color) {
			date = new SimpleDateFormat(pattern);
			this.color = color;
		}

		@Override
		public String format(LogRecord record) {
			StringBuilder builder = new StringBuilder();

			builder.append(date.format(record.getMillis())).append(' ');
			
			if (color) {
				if (record.getLevel().intValue() <= Level.CONFIG.intValue()) {
					builder.append(AnsiColor.BLUE);
				} else if (record.getLevel().intValue() == Level.INFO.intValue()) {
					// do nothing
				} else if (record.getLevel().intValue() == Level.WARNING.intValue()) {
					builder.append(AnsiColor.YELLOW);
				} else if (record.getLevel().intValue() >= Level.SEVERE.intValue()) {
					builder.append(AnsiColor.RED);
				}
			}

			builder.append('[');
			builder.append(record.getLevel().getLocalizedName().toUpperCase());
			builder.append("]" + AnsiColor.RESET + " ");

			if (color) {
				builder.append(colorize(AnsiColor.RESET + formatMessage(record) + AnsiColor.RESET));
			} else {
				builder.append(formatMessage(record));
			}

			builder.append('\n');

			if (record.getThrown() != null) {
				// StringWriter's close() is trivial
				StringWriter writer = new StringWriter();
				record.getThrown().printStackTrace(new PrintWriter(writer));
				builder.append(writer);
			}

			return colorize(builder.toString());
		}
	}

	private class ConsoleCommandThread implements Runnable {
		String consoleInput = "";

		@Override
		public void run() {
			while (running) {

				// Block for text
				try {
					consoleInput = reader.readLine(prompt);
				} catch (UserInterruptException e) {
					// Spawn a pooled-thread for handling SIGTERM interrupt
					executor.execute(() -> {
						print(Level.FINEST, "Caught SIGTERM (^C) interrupt");
						for (ICLIEvent event : registeredEvents) {
							event.userInterruptEvent();
						}
					});
					continue;
				} catch (EOFException e) {
					// Spawn a pooled-thread for handling EOF interrupt
					executor.execute(() -> {
						print(Level.FINEST, "Caught EOF (^D) interrupt");
						for (ICLIEvent event : registeredEvents) {
							event.eofInterruptEvent();
						}
					});
					continue;
				} catch (UnsupportedOperationException | IOException e) {
					break;
				}

				// If the input is null call EOF event, handle interrupt inside a pooled-thread
				if (this.consoleInput == null) {
					executor.execute(() -> {
						print(Level.FINEST, "Caught EOF (^D) interrupt");
						for (ICLIEvent event : registeredEvents) {
							event.eofInterruptEvent();
						}
					});
					continue;
				}

				String input = consoleInput.trim();
				String[] commandarray = input.split(" ");
				String command = commandarray[0];
				String[] params = Arrays.copyOfRange(commandarray, 1, commandarray.length);
				boolean valid = false;

				// Check if there is a registered command
				synchronized (registeredCommands) {
					for (final ICLICommand cmd : registeredCommands) {
						try {
							if (cmd.commandName().equalsIgnoreCase(command)) {
								// Spawn a pooled-thread for command dispatch
								executor.execute(() -> {
									cmd.invoke(instance, params);
								});
								valid = true;
							}
						} catch (RejectedExecutionException e) {
							executor.execute(() -> {
								print(Level.WARNING, "Console Executor rejected to spawn a new thread");
							});
						}
					}
				}

				// Spawn a pooled-thread for a invalid command-line handler if there is one
				if (!valid && !input.isEmpty() && invalidCommandHandler != null) {
					executor.execute(() -> {
						invalidCommandHandler.invalidInvoke(command, params);
					});
				}

			}
		}
	}

}
