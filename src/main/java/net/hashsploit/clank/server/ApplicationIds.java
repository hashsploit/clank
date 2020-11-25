package net.hashsploit.clank.server;

public enum ApplicationIds {
	
	/**
	 * Amplitude (March 24, 2003)
	 * Medius ?.??
	 * NTSC
	 */
	AMPLITUDE_NTSC(0),
	
	/**
	 * Syphon Filter: The Omega Strain (May 4, 2004)
	 * Medius 1.08
	 * NTSC
	 */
	SYPHON_FILTER_THE_OMEGA_STRAIN_NTSC(10411),
	
	/**
	 * Ratchet & Clank: Up Your Arsenal (November 3, 2004)
	 * Medius 1.08
	 * PAL
	 */
	RATCHET_AND_CLANK_UYA_PAL(10683),

	/**
	 * Ratchet & Clank: Up Your Arsenal (November 3, 2004)
	 * Medius 1.08
	 * NTSC
	 */
	RATCHET_AND_CLANK_UYA_NTSC(10684),
	
	/**
	 * Gran Turismo 4 (December 28, 2004)
	 * Medius 1.10
	 * NTSC
	 */
	GRAN_TURISMO_4_BETA_NTSC(10782),
	
	/**
	 * Jak X: Combat Racing (October 18, 2005)
	 * Medius 1.09
	 * NTSC
	 */
	JAK_X_COMBAT_RACING_NTSC(10994),
	
	/**
	 * Ratchet: Deadlocked (October 25, 2005)
	 * Medius 1.10
	 * NTSC
	 */
	RATCHET_DEADLOCKED_NTSC(11184),
	
	/**
	 * Ratchet: Deadlocked (October 25, 2005)
	 * Medius 1.10
	 * PAL
	 */
	RATCHET_DEADLOCKED_PAL(0),
	
	/**
	 * Jak X: Combat Racing (November 4, 2005)
	 * Medius 1.09
	 * PAL
	 */
	JAK_X_COMBAT_RACING_PAL(11204);
	
	
	
	private final int value;

	private ApplicationIds(int value) {
		this.value = value;
	}

	public final int getValue() {
		return value;
	}
	
}
