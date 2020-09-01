package net.hashsploit.clank;

import java.util.HashMap;
import java.util.HashSet;

public class EventBus {
	
	private Clank clank;
	private HashMap<EventType, HashSet<String>> events;
	
	public EventBus(Clank clank) {
		this.clank = clank;
	}
	
	public void subscribe(EventType type) {
		
	}
	
	public void unsubscribe(EventType type) {
		
	}
	
	
}
