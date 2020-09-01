package net.hashsploit.clank.cli;

public interface ICLIEvent {
	
	/**
	 * On user interrupt [SIGTERM] (such as ^C)
	 */
	public void userInterruptEvent();
	
	/**
	 * On End-Of-File/Line (such as ^D)
	 */
	public void eofInterruptEvent();
	
	/**
	 * Called whenever any command is invoked
	 */
	public void onReturnEvent(String line);
	
	/**
	 * Called whenever a valid command is invoked
	 */
	public void onCommandEvent(String command, String[] params, ICLICommand handler);
	
}
