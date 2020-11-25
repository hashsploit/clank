package net.hashsploit.clank.server.common.objects;

import net.hashsploit.clank.server.common.MediusConstants;

public class MediusPlayerOnlineState {
	
	private final MediusPlayerStatus connectionStatus;
	private final int lobbyWorldId;
	private final int gameWorldId;
	private final String lobbyName;
	private final String gameName;
	
	public MediusPlayerOnlineState(final MediusPlayerStatus connectionStatus, final int lobbyWorldId, final int gameWorldId, final String lobbyName, final String gameName) {
		this.connectionStatus = connectionStatus;
		this.lobbyWorldId = lobbyWorldId;
		this.gameWorldId = gameWorldId;
		this.lobbyName = lobbyName;
		this.gameName = gameName;
		
		if (lobbyName.length() > MediusConstants.WORLDNAME_MAXLEN.getValue()) {
			throw new IllegalStateException("Lobby Name > WORLDNAME_MAXLEN");
		}
		
		if (gameName.length() > MediusConstants.GAMENAME_MAXLEN.getValue()) {
			throw new IllegalStateException("Game Name > GAMENAME_MAXLEN");
		}
		
	}

	/**
	 * Player's online state.
	 * @return
	 */
	public MediusPlayerStatus getConnectionStatus() {
		return connectionStatus;
	}

	/**
	 * Lobby world Id if the state is in a chat channel,
	 * @return
	 */
	public int getLobbyWorldId() {
		return lobbyWorldId;
	}

	/**
	 * Game world Id if the state is in a game.
	 * @return
	 */
	public int getGameWorldId() {
		return gameWorldId;
	}

	/**
	 * Lobby world name.
	 * @return
	 */
	public String getLobbyName() {
		return lobbyName;
	}

	/**
	 * Game world name.
	 * @return
	 */
	public String getGameName() {
		return gameName;
	}
	
}
