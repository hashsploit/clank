package net.hashsploit.clank.utils;

public final class MathHelper {
	
	// Prevent instantiation
	private MathHelper() {}

	/**
	 * Convert degrees to radians
	 * @param degrees
	 * @return
	 */
	public static final double degreesToRadians(final double degrees) {
		return ((degrees*Math.PI)/180);
	}
	
	/**
	 * Convert degrees to radians
	 * @param degrees
	 * @return
	 */
	public static final float degreesToRadians(final float degrees) {
		return (float) ((degrees*Math.PI)/180);
	}
	
	/**
	 * Convert radians to degrees
	 * @param radians
	 * @return
	 */
	public static final double radiansToDegrees(final double radians) {
		return ((radians*180)/Math.PI);
	}
	
	/**
	 * Convert radians to degrees
	 * @param radians
	 * @return
	 */
	public static final float radiansToDegrees(final float radians) {
		return (float) ((radians*180)/Math.PI);
	}
	
	/**
	 * Raise a number base to a power
	 * @param base
	 * @param power
	 * @return
	 */
	public static final double power(final double base, final double power) {
		if (power == 0) {
			return 1;
		} else if (power == 1) {
			return base;
		} else if (power == 2) {
			return (base * base);
		}
		
		return Math.pow(base, power);
	}
	
	/**
	 * Raise a number base to a power
	 * @param base
	 * @param power
	 * @return
	 */
	public static final float power(final float base, final float power) {
		if (power == 0) {
			return 1;
		} else if (power == 1) {
			return base;
		} else if (power == 2) {
			return (base * base);
		}
		
		return (float) Math.pow(base, power);
	}
	
	/**
	 * Get factorial of a number
	 * @param num
	 * @return
	 */
	public static final int factorial(int num) {
		int fact = 1;
		for (int i = 1; i <= num; i++) {
			fact *= i;
		}
		return fact;
	}
	
	/**
	 * Extract a integer from a string
	 * or return 0 if the string is invalid.
	 * @param num
	 * @return
	 */
	public static final int stringToInt(String num) {
		if (num == null) {
			return 0;
		}
		try {
			return Integer.parseInt(num);
		} catch (NumberFormatException | NullPointerException e) {
			return 0;
		}
	}
	
	/**
	 * Extract a double from a string
	 * or return 0 if the string is invalid.
	 * @param num
	 * @return double
	 */
	public static final double stringToDouble(String num) {
		if (num == null) {
			return 0;
		}
		try {
			return Double.parseDouble(num);
		} catch (NumberFormatException | NullPointerException e) {
			return 0;
		}
	}
	
	/**
	 * Extract a float from a string
	 * or return 0 if the string is invalid.
	 * @param num
	 * @return float
	 */
	public static final float stringToFloat(String num) {
		if (num == null) {
			return 0;
		}
		try {
			return Float.parseFloat(num);
		} catch (NumberFormatException | NullPointerException e) {
			return 0;
		}
	}
	
	/**
	 * Convert a double to a float
	 * @param num
	 * @return float
	 */
	public static final float doubleToFloat(double num) {
		return ((float) num);
	}
	
	/**
	 * Convert a float to a double
	 * @param num
	 * @return double
	 */
	public static final double floatToDouble(float num) {
		return ((double) num);
	}
	
	/**
	 * Check if a float is about equal to another float within a range of tolerance.
	 * @param a
	 * @param b
	 * @param tolerance
	 * @return
	 */
	public static final boolean isAboutEqual(float a, float b, float tolerance) {
		return Math.abs(a-b) <= Math.max(0, tolerance);
	}
	
	/**
	 * Check if a double is about equal to another double within a range of tolerance.
	 * @param a
	 * @param b
	 * @param tolerance
	 * @return
	 */
	public static final boolean isAboutEqual(double a, double b, float tolerance) {
		return Math.abs(a-b) <= Math.max(0, tolerance);
	}
	
	/**
	 * Linear interpolates an associated value for given xb.
	 * <br>Example:<br>
	 * x1=10; y1=100; x2=100; y2=1000; xb=500 Would result in 
	 * an interpolation of <br> 5000.
	 * @param x1 -Any number.
	 * @param y1 -A number associated with 'x1'.
	 * @param xb -The value to find the linear interpolated, associated value from.
	 * @param x2 -Another number
	 * @param y2 -A number associated with 'x2'.
	 * @return The linear interpolated associated number for the given 'xb'.
	 */
	public static final float interpolate(float x1, float y1, float xb, float x2, float y2) {
		return y1+((xb-x1)*(y2-y1))/(x2-x1);
	}
	
	/**
	 * Get the squared distance from x1,y1,z1 to x2,y2,z2
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return
	 */
	public static final float getDistanceSq(float x1, float y1, float z1, float x2, float y2, float z2) {
		return (float) (Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2) + Math.pow(z2-z1, 2));
	}
	
	/**
	 * Get the distance from x1,y1,z1 to x2,y2,z2 (could be slow!)
	 * @param x1
	 * @param y1
	 * @param z1
	 * @param x2
	 * @param y2
	 * @param z2
	 * @return
	 */
	public static final float getDistance(float x1, float y1, float z1, float x2, float y2, float z2) {
		return (float) Math.sqrt(getDistanceSq(x1, y1, z1, x2, y2, z2));
	}
	
	/**
	 * Get the length of a 3-dimensional vector squared
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static final double get3DVectorLengthSquared(final double x, final double y, final double z) {
		return x * x + y * y + z * z;
	}
	
	/**
	 * Get the length of a 3-dimensional vector
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public static final double get3DVectorLength(final double x, final double y, final double z) {
		return Math.sqrt(get3DVectorLength(x, y, z));
	}
	
}
