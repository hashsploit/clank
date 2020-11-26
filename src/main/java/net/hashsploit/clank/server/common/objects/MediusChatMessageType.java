package net.hashsploit.clank.server.common.objects;

public enum MediusChatMessageType {

	BROADCAST(0),
	
	WHISPER(1),
	
	BROADCAST_ACROSS_ENTIRE_UNIVERSE(2),
	
	CLAN_CHAT_TYPE(3),
	
	BUDDY_CHAT_TYPE(4);
	
	private final int value;
	
	private MediusChatMessageType(int value) {
		this.value = value;
	}
	
	public final int getValue() {
		return value;
	}
	
	
}
