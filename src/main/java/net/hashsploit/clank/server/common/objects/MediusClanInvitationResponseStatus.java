package net.hashsploit.clank.server.common.objects;

public enum MediusClanInvitationResponseStatus {

	CLAN_INVITATION_UNDECIDED(0),
	
	CLAN_INVITATION_ACCEPT(1),
	
	CLAN_INVITATION_DECLINE(2),
	
	CLAN_INVITATION_REVOKED(3);

	private final int value;

	private MediusClanInvitationResponseStatus(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}

}
