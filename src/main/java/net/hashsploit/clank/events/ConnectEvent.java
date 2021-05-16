package net.hashsploit.clank.events;

import net.hashsploit.clank.EventType;
import net.hashsploit.clank.plugin.ClankEvent;

public class ConnectEvent extends ClankEvent {
	
	private final String ipAddress;
	private final int port;
	
	public ConnectEvent(String ipAddress, int port) {
		super(EventType.CONNECT_EVENT);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	/**
	 * Incoming client connection from this IP Address.
	 * @return
	 */
	public String getIpAddress() {
		return ipAddress;
	}
	
	/**
	 * Incoming client connection from this port.
	 * @return
	 */
	public int getPort() {
		return port;
	}
	
}
