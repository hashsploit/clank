package net.hashsploit.clank.events;

import net.hashsploit.clank.EventType;
import net.hashsploit.clank.plugin.ClankEvent;

public class TickEvent extends ClankEvent {
	
	public TickEvent() {
		super(EventType.TICK_EVENT);
	}
	
}
