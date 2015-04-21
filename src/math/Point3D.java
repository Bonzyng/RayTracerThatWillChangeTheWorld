package math;

import java.util.Scanner;

public class Point3D {
	/**
	 * Point parameters. Public for simpler and faster access.
	 */
	public double x, y, z;

	/**
	 *  Initiate point to 0.0, 0.0, 0.0 - Base of the axis
	 */
	public Point3D() {
		x = 0.0;
		y = 0.0;
		z = 0.0;
	}
	
	/**
	 * Find a point using an origin point and a vector
	 * @param p - origin point
	 * @param v - direction and distance from origin point
	 * @return new point
	 */
	public Point3D (Point3D p, Vec v) {
		this.x = p.x + v.x;
		this.y = p.y + v.y;
		this.z = p.z + v.z;
	}
	
	/**
	 * Create a point from an input string
	 * 
	 * @param p
	 */
	public Point3D(String p) {
		Scanner s = new Scanner(p);
		x = s.nextDouble();
		y = s.nextDouble();
		z = s.nextDouble();
		s.close();
	}
	
	/**
	 * Create a new point from 3 coordinates
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Create a new point from given point
	 * 
	 * @param p
	 */
	public Point3D(Point3D p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	/**
	 * Subtract two points to return the vector from a to b
	 * 
	 * @param a - first point
	 * @param b - second point
	 * @return Vector from a to b
	 */
	public static Vec getVec(Point3D a, Point3D b) {
		return new Vec(b.x - a.x, b.y - a.y, b.z - a.z);
	}
	
	/**
	 * Find the vector from this point to other point
	 * 
	 * @param other - point the vector is 'going to'
	 * @return vector between this point and the other point
	 */
	public Vec getVec(Point3D other) {
		return new Vec(other.x - this.x, other.y - this.y, other.z - this.z);
	}
	
	/**
	 * Calculate the distance between two points in space
	 * 
	 * @param a - first point
	 * @param b - second point
	 * @return distance between a and b
	 */
	public static double distance(Point3D a, Point3D b) {
		return Math.sqrt(((b.x - a.x) * (b.x - a.x)) + 
						((b.y - a.y) * (b.y - a.y)) +
						((b.z - a.z) * (b.z - a.z)));
	}
	
	/**
	 * Find distance between this point and the other point
	 * 
	 * @param other point
	 * @return distance between the two points
	 */
	public double distance(Point3D other) {
		return Math.sqrt(((other.x - this.x) * (other.x - this.x)) + 
						((other.y - this.y) * (other.y - this.y)) +
						((other.z - this.z) * (other.z - this.z)));
	}
	
	/**
	 * Check if points a and b are the same points
	 * 
	 * @param a
	 * @param b
	 * @return true/false
	 */
	public static boolean equals(Point3D a, Point3D b) {
		return ((a.x == b.x) && (a.y == b.y) && (a.z == b.z));
	}
	
	/**
	 * Check if this point is the same as given point
	 * 
	 * @param p
	 * @return true/false
	 */
	public boolean equals(Point3D p) {
		return ((this.x == p.x) && (this.y == p.y) && (this.z == p.z));
	}
	
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
}

