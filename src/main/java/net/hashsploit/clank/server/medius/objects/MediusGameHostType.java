package net.hashsploit.clank.server.medius.objects;

import java.util.HashMap;
import java.util.Map;

public enum MediusGameHostType {

	HOST_CLIENT_SERVER(0),

	HOST_INTEGRATED_SERVER(1),

	HOST_PEER_TO_PEER(2),

	HOST_LAN_PLAY(3),

	HOST_CLIENT_SERVER_AUX_UDP(4);

	public final int value;
	private static Map<Integer, MediusGameHostType> enumMapping = new HashMap<>();

	private MediusGameHostType(int value) {
		this.value = value;
	}
	
	public static MediusGameHostType getTypeFromValue(int value) {
		return enumMapping.get(value);
	}
	
	static {
		for (MediusGameHostType ght : MediusGameHostType.values()) {
			enumMapping.put(ght.value, ght);
		}
	}

}
