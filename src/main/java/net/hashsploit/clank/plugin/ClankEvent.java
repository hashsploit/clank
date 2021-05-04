package net.hashsploit.clank.plugin;

import net.hashsploit.clank.EventType;

public abstract class ClankEvent {
	
	private EventType type;
	
	public ClankEvent(EventType type) {
		this.type = type;
	}

	public EventType getType() {
		return type;
	}
	
}
