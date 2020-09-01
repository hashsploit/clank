package net.hashsploit.clank.cli.commands;

import java.util.logging.Level;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.MediusComponent;
import net.hashsploit.clank.Terminal;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.server.Client;

public class CLIClientsCommand implements ICLICommand {
	
	@Override
	public void invoke(Terminal term, String[] params) {
		term.print(Level.INFO, "Clients: " + Clank.getInstance().getServer().getClients());
		for (Client client : Clank.getInstance().getServer().getClients()) {
			term.print(Level.INFO, String.format("- %s:%d -> %s", client.getIPAddress(), client.getPort(), client.getClientState().toString()));	
		}
	}
	
	@Override
	public String commandName() {
		return "clients";
	}
	
	@Override
	public String commandDescription() {
		return "Print clients.";
	}

	@Override
	public int enabledMediusModes() {
		int value = 0;
		for (MediusComponent m : MediusComponent.values()) {
			value |= m.getModeId();
		}
		return value;
	}
	
}
