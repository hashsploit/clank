package net.hashsploit.clank.cli.commands;

import java.util.logging.Level;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.Terminal;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.server.ClientState;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.MediusClient;

public class CLIPlayersCommand implements ICLICommand {
	
	@Override
	public void invoke(Terminal term, String[] params) {
		
		// TODO: if this server is the MAS, then show a table of connections/players currently authenticating and players that have successfully authenticated (db query).
		// if this server is the MLS, show a list of currently connected/online players.
		// if this server is the DME, show a list of currently active games along with the players.
		
		// No parameters
		if (params.length == 0) {
			
			if (Clank.getInstance().getServer().getClients().size() > 0) {
				
				// TODO: change this for MUIS/MAS/MLS/DME/NAT
				term.print(Level.INFO, String.format("IP Address\t| Client State\t| Username"));
				
				for (IClient c : Clank.getInstance().getServer().getClients()) {
					if (c instanceof MediusClient) {
						final MediusClient client = (MediusClient) c;
						
						final String ipAddr = client.getIPAddress();
						final int port = client.getPort();
						final String clientState = client.getClientState().name();
						final String username = (client.getClientState() == ClientState.AUTHENTICATED ? client.getPlayer().getUsername() : "");
						
						term.print(Level.INFO, String.format("%s:%d\t %s\t %s", ipAddr, port, clientState, username));
					}
				}
				
			} else {
				term.print(Level.INFO, "No clients available.");
			}

		}
		
	}
	
	@Override
	public String commandName() {
		return "players";
	}
	
	@Override
	public String commandDescription() {
		return "Print a list of all the connected players.";
	}
	
}
