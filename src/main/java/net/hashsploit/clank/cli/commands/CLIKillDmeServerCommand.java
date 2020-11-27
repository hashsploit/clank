package net.hashsploit.clank.cli.commands;

import java.util.logging.Logger;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.Terminal;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.server.UdpServer;
import net.hashsploit.clank.server.dme.DmeServer;

public class CLIKillDmeServerCommand implements ICLICommand {

	private static final Logger logger = Logger.getLogger(CLIKillDmeServerCommand.class.getName());
	
	@Override
	public void invoke(Terminal term, String[] params) {
		
		if (params.length == 1) {
			if (params[0].equalsIgnoreCase("u")) {
				for (final UdpServer udpServer : ((DmeServer) Clank.getInstance().getServer()).getGameServers()) {
					udpServer.stop();
				}
				logger.info(String.format("Killed DME UDP servers."));
				return;
			} else if (params[0].equalsIgnoreCase("t")) {
				logger.info(String.format("Killed DME TCP server."));
				((DmeServer) Clank.getInstance().getServer()).stop();
				return;
			}
		}
		
		logger.info(String.format("Specify 't' for TCP server or 'u' for UDP server."));
	}

	@Override
	public String commandName() {
		return "d";
	}

	@Override
	public String commandDescription() {
		return "Kill the DME Server.";
	}

}
