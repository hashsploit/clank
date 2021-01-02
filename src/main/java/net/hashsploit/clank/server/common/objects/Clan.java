package net.hashsploit.clank.server.common.objects;

public class Clan {
	
	private int id;
	private String name;
	private String tag;
	private String message;
	private boolean colorsAllowed;
	private int leaderId;
	private int wins;
	private int losses;
	private int kills;
	private int deaths;
	private int challengeClan;
	
	public Clan(int id, String name, String tag, String message, boolean colorsAllowed, int leaderId, int wins, int losses, int kills, int deaths, int challenger) {
		this.id = id;
		this.name = name;
		this.tag = tag;
		this.message = message;
		this.colorsAllowed = colorsAllowed;
		this.leaderId = leaderId;
		this.wins = wins;
		this.losses = losses;
		this.kills = kills;
		this.deaths = deaths;
		this.challengeClan = challenger;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isColorsAllowed() {
		return colorsAllowed;
	}

	public void setColorsAllowed(boolean colorsAllowed) {
		this.colorsAllowed = colorsAllowed;
	}

	public int getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(int leaderId) {
		this.leaderId = leaderId;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getChallenger() {
		return challengeClan;
	}

	public void setChallenger(int challenger) {
		this.challengeClan = challenger;
	}
	
}
