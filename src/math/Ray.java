package math;

/**
 * This will work only if you will add the Vec Class from Ex2 - Make sure its working properly.
 * You will also need to implement your own Point3D class.
 */
public class Ray {

	// point of origin
	public Point3D mOriginPoint;
	// ray direction
	public Vec mDirectionVector;
	
	/**
	 * constructs a new ray
	 * @param p - point of origin
	 * @param v - ray direction
	 */

	public Ray(Point3D p, Vec v) {
		this.mOriginPoint = new Point3D(p);
		this.mDirectionVector = new Vec(v);
		mDirectionVector.normalize(); // Ensure direction vector is normalized
	}
}
