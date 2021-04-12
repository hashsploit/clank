package net.hashsploit.clank.server.medius.objects;

import java.util.HashMap;
import java.util.Map;

public enum MediusPolicyType {
	
	USAGE(0),

	PRIVACY(1);

	public final int value;
	private static Map<Integer, MediusPolicyType> enumMapping = new HashMap<>();

	private MediusPolicyType(int value) {
		this.value = value;
	}
	
	public static MediusPolicyType getTypeFromValue(int value) {
		return enumMapping.get(value);
	}
	
	static {
		for (MediusPolicyType mpt : MediusPolicyType.values()) {
			enumMapping.put(mpt.value, mpt);
		}
	}
	
}
