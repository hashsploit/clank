package net.hashsploit.clank.server.medius.objects;

public enum TextFilterType {
	
	TEXT_FILTER_PASS_FAIL(0),
	
	TEXT_FILTER_REPLACE(1);
	
	
	private final int value;

	private TextFilterType(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
