package net.hashsploit.clank.plugin;

import java.util.HashMap;
import java.util.HashSet;

import org.luaj.vm2.LuaFunction;

import net.hashsploit.clank.EventType;
import net.hashsploit.clank.cli.ICLICommand;

public class LuaPlugin implements IClankPlugin {
	
	private final String name;
	private final int revisionVersion;
	private final int minorVersion;
	private final int majorVersion;
	private final String description;
	private final LuaFunction initFunction;
	private final HashSet<ICLICommand> commands;
	private final HashMap<EventType, LuaFunction> subscribedEvents;
	
	public LuaPlugin(String name, String description, int majorVersion, int minorVersion, int revisionVersion, LuaFunction initFunction, HashSet<ICLICommand> commands, HashMap<EventType, LuaFunction> subscribedEvents) {
		this.name = name;
		this.description = description;
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.revisionVersion = revisionVersion;
		this.initFunction = initFunction;
		this.commands = commands;
		this.subscribedEvents = subscribedEvents;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getVersionMajor() {
		return majorVersion;
	}
	
	@Override
	public int getVersionMinor() {
		return minorVersion;
	}
	
	@Override
	public int getVersionRevision() {
		return revisionVersion;
	}

	@Override
	public String getVersion() {
		return majorVersion + "." + minorVersion + "." + revisionVersion;
	}
	
	@Override
	public void init() {
		initFunction.call();
	}

	@Override
	public void onEvent(ClankEvent event) {
		
	}
	
	public HashSet<ICLICommand> getRegisteredCommands() {
		return commands;
	}
	
	@Override
	public HashSet<EventType> getRegisteredEventTypes() {
		return new HashSet<EventType>(subscribedEvents.keySet()); 
	}
	
	/**
	 * Get all the EventTypes this plugin subscribed to.
	 * @return
	 */
	public HashSet<EventType> getSubscribedEventTypes() {
		return new HashSet<EventType>(subscribedEvents.keySet());
	}

}
