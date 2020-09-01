package net.hashsploit.clank;

import java.io.File;
import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		if (args.length != 1) {
			System.err.println("Required argument: 1 (config file)");
			return;
		}
		
		if (!new File(args[0]).isFile()) {
			System.err.println("File not found: " + args[0]);
			return;
		}
		
		final ClankConfig config = new ClankConfig(args[0]);
		new Clank(config);
		
	}
}
