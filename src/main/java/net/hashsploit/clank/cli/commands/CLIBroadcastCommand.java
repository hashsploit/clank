package net.hashsploit.clank.cli.commands;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import net.hashsploit.clank.Clank;
import net.hashsploit.clank.Terminal;
import net.hashsploit.clank.cli.ICLICommand;
import net.hashsploit.clank.server.ChatColor;
import net.hashsploit.clank.server.DataPacket;
import net.hashsploit.clank.server.IClient;
import net.hashsploit.clank.server.MediusClient;
import net.hashsploit.clank.server.RTPacketId;
import net.hashsploit.clank.server.scert.objects.RTMsgEncodingType;
import net.hashsploit.clank.server.scert.objects.RTMsgLanguageType;
import net.hashsploit.clank.utils.Utils;

public class CLIBroadcastCommand implements ICLICommand {
	
	@Override
	public void invoke(Terminal term, String[] params) {
		
		if (params.length < 2) {
			printHelp(term);
			return;
		}
		
		try {
			int severity = Integer.parseInt(params[0]);
			String message = Arrays.toString(Arrays.copyOfRange(params, 1, params.length));
			
			if (severity < 0 || severity > 255) {
				throw new IllegalArgumentException();
			}
			
			// TODO: make this cleaner, add this functionality into the MediusClient itself.
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				baos.write((byte) severity); // Severity 255 (1 byte)
				baos.write(RTMsgEncodingType.RT_MSG_ENCODING_UTF8.getValue()); // Encoding type (1 byte)
				baos.write(RTMsgLanguageType.RT_MSG_LANGUAGE_UK_ENGLISH.getValue()); // Language type (1 byte)
				baos.write(Utils.intToBytesLittle(1)); // EndOfList boolean (4 bytes)
				baos.write(Utils.buildByteArrayFromString(ChatColor.convertColorCodes(message), message.length()));
				baos.write((byte) 0x00);
			} catch (IOException e) {}
			
			for (IClient c : Clank.getInstance().getServer().getClients()) {
				if (c instanceof MediusClient) {
					final MediusClient client = (MediusClient) c;
					client.sendMessage(new DataPacket(RTPacketId.SERVER_SYSTEM_MESSAGE, baos.toByteArray()));
				}
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
