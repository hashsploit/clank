package net.hashsploit.clank.server;

import java.util.regex.Pattern;

public enum ChatColor {
	
	// Colors
	
	/**
	 * Color: reset the color back to the default.
	 * @return
	 */
	COLOR_RESET(0x08),
	
	/**
	 * Color: blue.
	 */
	COLOR_BLUE(0x09),
	
	/**
	 * Color: green.
	 */
	COLOR_GREEN(0x0a),
	
	/**
	 * Color: pink.
	 */
	COLOR_PINK(0x0b),
	
	/**
	 * Color: white.
	 */
	COLOR_WHITE(0x0c),
	
	/**
	 * Color: black.
	 */
	COLOR_BLACK_1(0x0d),
	
	/**
	 * Color: black.
	 */
	COLOR_BLACK_2(0x0e),
	
	/**
	 * Color: black.
	 */
	COLOR_BLACK_3(0x0f),
	
	// Controller buttons
	
	/**
	 * Controller X button.
	 */
	BUTTON_X(0x10),
	
	/**
	 * Controller CIRCLE button.
	 */
	BUTTON_CIRCLE(0x11),
	
	/**
	 * Controller TRIANGLE button.
	 */
	BUTTON_TRIANGLE(0x12),
	
	/**
	 * Controller SQUARE button.
	 */
	BUTTON_SQUARE(0x13),
	
	/**
	 * Controller L1 button.
	 */
	BUTTON_L1(0x14),
	
	/**
	 * Controller R1 button.
	 */
	BUTTON_R1(0x15),
	
	/**
	 * Controller L2 button.
	 */
	BUTTON_L2(0x16),
	
	/**
	 * Controller R2 button.
	 */
	BUTTON_R2(0x17),
	
	/**
	 * Controller left-analog stick.
	 */
	ANALOG_LEFT(0x18),
	
	/**
	 * Controller right-analog stick.
	 */
	ANALOG_RIGHT(0x19),
	
	/**
	 * Controller Select button.
	 */
	BUTTON_SELECT(0x1a),
	
	// Glitched textures
	
	GLITCHED_BIG_RED_BOX(0x82),
	
	GLITCHED_LOW_IMAGE(0x84),
	
	GLITCHED_BIG_SPACE(0x85),
	
	GLITCHED_BIG_WHITE_BOLD_VERT_LINE(0x86),
	
	GLITCHED_HIGH_WHITE_GREEN_BOX(0x89),
	
	GLITCHED_CHAR(0x8c),
	
	GLITCHED_SPACE(0x8d),
	
	GLITCHED_SMALL_BLUR(0x8e),
	
	GLITCHED_HIGH_RED_BOX(0x8f),
	
	GLITCHED_BIG_TALL_RED_BOX(0x90),
	
	GLITCHED_BIG_TALL_RED_THICK_LINE(0x91),
	
	GLITCHED_SMALL_LOW_GREEN_BOX(0x92),
	
	GLITCHED_TALL_WHITE_BOX_GREEN_TOP(0x9e),
	
	GLITCHED_IMAGE(0x9f),
	
	GLITCHED_MULTI_COLOR(0xa0),
	
	GLITCHED_WIDE_GREEN_HORIZ_LINE(0xa1),
	
	GLITCHED_BIG_IMAGE(0xa2),
	
	GLITCHED_LONG_VERT_GREEN_LINE(0xa4),
	
	GLITCHED_BIG_GREEN_BOX(0xa5),
	
	GLITCHED_LOW_BLACK_AND_GREEN_BOX(0xa7),
	
	GLITCHED_FRAME_BOX(0xa8),
	
	GLITCHED_FUZZY_BOX(0xa9),
	
	GLITCHED_WIDE_HORIZ_RED_BOX(0xaa),
	
	GLITCHED_BOTTOM_BOX(0xab),
	
	GLITCHED_BIG_BOTTOM_BOX(0xb4),
	
	GLITCHED_TALL_BOX(0xb5);
	
	// 0x2A = *
	// 0x2F = /
	// 0x5F = _
	// 0x7C = |
	// 0x7E = ~
	
	public static final String CONTROL_CHARACTER = "&";
	private final byte value;

	private ChatColor(int value) {
		this.value = (byte) value;
	}

	public final byte getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value + "";
	}
	
	/**
	 * Convert plain text to byte-colored text to send to the clients.
	 * @param text
	 * @return
	 */
	public static String convertColorCodes(String text) {
		
		text = text.replaceAll(Pattern.quote("&r"), COLOR_RESET.toString());
		text = text.replaceAll(Pattern.quote("&0"), COLOR_BLACK_1.toString());
		text = text.replaceAll(Pattern.quote("&9"), COLOR_BLUE.toString());
		text = text.replaceAll(Pattern.quote("&2"), COLOR_GREEN.toString());
		text = text.replaceAll(Pattern.quote("&d"), COLOR_PINK.toString());
		text = text.replaceAll(Pattern.quote("&d"), COLOR_WHITE.toString());
		
		return text;
	}
	
	/**
	 * Strips all invalid chat characters such as colors and controller buttons.
	 * @param text
	 * @return
	 */
	public static String strip(final String text) {
		final StringBuilder sb = new StringBuilder();
		
		for (final char c : text.toCharArray()) {
			for (final ChatColor chatColor : ChatColor.values()) {
				if (c != chatColor.value && c >= 0x21 && c <= 0x7F) {
					sb.append(c);
				}
			}
		}
		
		return sb.toString();
	}
	
}
