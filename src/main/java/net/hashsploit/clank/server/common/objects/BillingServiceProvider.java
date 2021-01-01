package net.hashsploit.clank.server.common.objects;

public enum BillingServiceProvider {
	
	SCEA("SCEA"),
	
	SCEE("SCEE"),
	
	SCEK("SCEK");
	
	public final String value;
	
	private BillingServiceProvider(String value) {
		this.value = value;
	}
	
}
