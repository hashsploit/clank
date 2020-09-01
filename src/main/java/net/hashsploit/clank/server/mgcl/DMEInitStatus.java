package net.hashsploit.clank.server.mgcl;

/**
 * Enumeration used to identify whether or not the DME layer has been
 * initialized prior to the call to MGCLInitialize.
 */
public enum DMEInitStatus {
	
	/**
	 * The DME has been initialized prior to this function call.
	 */
	DMEInitialized(0),
	
	/**
	 * The DME has NOT been initialized prior to this function call.
	 */
	DMENotInitialized(1);
	
	private final int value;

	private DMEInitStatus(int value) {
		this.value = (short) value;
	}

	public final int getValue() {
		return value;
	}
}
