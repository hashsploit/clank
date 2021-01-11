package net.hashsploit.clank.cli.commands;

import java.util.Arrays;
import java.util.logging.Level;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.Terminal;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.server.medius.MediusServer;
import net.hashsploit.clank.utils.Utils;

public class CLIBroadcastCommand implements ICLICommand {
	
	@Override
	public void invoke(Terminal term, String[] params) {
		
		if (params.length < 2) {
			printHelp(term);
			return;
		}
		
		try {
			final int severity = Integer.parseInt(params[0]);
			final String message = String.join(" ", Arrays.copyOfRange(params, 1, params.length));
			
			if (severity < 0 || severity > 255) {
				throw new IllegalArgumentException();
			}
			
			final int mask = (EmulationMode.MEDIUS_AUTHENTICATION_SERVER.value | EmulationMode.MEDIUS_LOBBY_SERVER.value);
			
			if (Utils.isInBitmask(Clank.getInstance().getConfig().getEmulationMode().value, mask)) {
				((MediusServer) Clank.getInstance().getServer()).sendSystemBroadcastMessage(severity, message);
			}
			
			term.print(Level.INFO, String.format("Sent system message '%s' to %d client(s).", message, Clank.getInstance().getServer().getClients().size()));
			
		} catch (IllegalArgumentException e) {
			printHelp(term);
			return;
		}
		
	}
	
	private void printHelp(final Terminal term) {
		term.print(Level.INFO, "Usage: " + commandName() + " <severity> <message>");
		term.print(Level.INFO, " - Where <severity> is an integer from 0 to 255.");
		term.print(Level.INFO, " - Where <message> is a string that will be sent to all clients.");
	}
	
	@Override
	public String commandName() {
		return "broadcast";
	}
	
	@Override
	public String commandDescription() {
		return "Send a broadcast message to all clients connected.";
	}
	
}
