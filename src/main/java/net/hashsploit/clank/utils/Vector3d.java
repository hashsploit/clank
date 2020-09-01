package net.hashsploit.clank.utils;

public class Vector3d {
	
	private double x;
	private double y;
	private double z;
	
	public Vector3d() {
		this(0, 0, 0);
	}
	
	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Get the X component of the vector
	 * @return
	 */
	public double getX() {
		return x;
	}

	/**
	 * Set the X component of the vector
	 * @return
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Get the Y component of the vector
	 * @return
	 */
	public double getY() {
		return y;
	}

	/**
	 * Set the Y component of the vector
	 * @return
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Get the Z component of the vector
	 * @return
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Set the Z component of the vector
	 * @return
	 */
	public void setZ(double z) {
		this.z = z;
	}
	
	/**
	 * Get the length (magnitude) of this vector squared
	 * @return
	 */
	public double lengthSquared() {
		return MathHelper.get3DVectorLengthSquared(x, y, z);
	}
	
	/**
	 * Get the length (magnitude) of this vector
	 * @return
	 */
	public double length() {
		return MathHelper.get3DVectorLength(x, y, z);
	}
	
}
