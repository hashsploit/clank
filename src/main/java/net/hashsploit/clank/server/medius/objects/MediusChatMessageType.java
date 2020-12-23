package net.hashsploit.clank.server.medius.objects;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum MediusChatMessageType {

	BROADCAST(0),
	
	WHISPER(1),
	
	BROADCAST_ACROSS_ENTIRE_UNIVERSE(2),
	
	CLAN_CHAT_TYPE(3),
	
	BUDDY_CHAT_TYPE(4);
	
	private final int value;
	
	private static final Map<Integer, MediusChatMessageType> ENUM_MAP;
	
	private MediusChatMessageType(int value) {
		this.value = value;
	}
	
	public final int getValue() {
		return value;
	}
	
    static {
        Map<Integer, MediusChatMessageType> map = new ConcurrentHashMap<Integer, MediusChatMessageType>();
        for (MediusChatMessageType instance : MediusChatMessageType.values()) {
            map.put(instance.getValue(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }
    
    public static MediusChatMessageType getByValue(int type) {
    	return ENUM_MAP.get(type);
    }
	
}
