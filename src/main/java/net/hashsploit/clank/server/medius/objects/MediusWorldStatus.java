package net.hashsploit.clank.server.medius.objects;

public enum MediusWorldStatus {

	WORLD_INACTIVE(0),

	WORLD_STAGING(1),

	WORLD_ACTIVE(2),

	WORLD_CLOSED(3),

	WORLD_PENDING_CREATION(4),

	WORLD_PENDING_CONNECT_TO_GAME(5);

	public final int value;

	private MediusWorldStatus(int value) {
		this.value = value;
	}

}
