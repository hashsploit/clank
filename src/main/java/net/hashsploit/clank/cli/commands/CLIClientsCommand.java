package net.hashsploit.clank.cli.commands;

import net.hashsploit.clank.EmulationMode;
import net.hashsploit.clank.Terminal;
import net.hashsploit.clank.cli.ICLICommand;

public class CLIClientsCommand implements ICLICommand {
	
	@Override
	public void invoke(Terminal term, String[] params) {
		// FIXME: broken
		//term.print(Level.INFO, "Clients: " + Clank.getInstance().getServer().getClients());
		//for (IClient client : Clank.getInstance().getServer().getClients()) {
		//	term.print(Level.INFO, String.format("- %s:%d -> %s", client.getIPAddress(), client.getPort(), client.getClientState().toString()));	
		//}
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
		for (EmulationMode m : EmulationMode.values()) {
			value |= m.getValue();
		}
		return value;
	}
	
}
