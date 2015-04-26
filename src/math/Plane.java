package math;

import ex3.surfaces.Surface;

public class Plane {
	public Point3D mPoint;
	public Vec mNormal;
	
	public Plane(Point3D p0, Point3D p1, Point3D p2) {
		mPoint = new Point3D(p0);
		Vec v0 = Point3D.getVec(p0, p1);
		Vec v1 = Point3D.getVec(p0, p2);
		
		mNormal = Vec.crossProd(v0, v1);
		mNormal.normalize();
	}
	
	public Plane(Point3D p, Vec normal) {
		mPoint = new Point3D(p);
		
		mNormal = new Vec(normal);
		mNormal.normalize();
	}
	
	public Point3D intersect(Ray iRay, boolean behind) { // Ray given as p0 origin and v direction vector
										 // plane represented by point p and normal n
		double nDotD;
		// Check if n * v == 0. If yes, line is either on the plane (irrelevant)
		// or parallel to it and doesn't intersect
		if ((nDotD = mNormal.dotProd(iRay.mDirectionVector)) == 0) {
			return null;
		}
		
		// Check that they ray isn't coming behind the plane - check that angle x between
		// the normal and the ray direction vectors is acute (0-90 or 270-360)
		double angle = Math.acos(nDotD / (mNormal.length() * iRay.mDirectionVector.length()));
		angle = Math.toDegrees(angle);
		
		if (behind) {
			if (angle > 90 && angle < 270) {
				return null;
			}
		} else {
			if (angle < 90 || angle > 270) {
				return null;
			}
		}
		
		
		// Calculate t for which t = (p - p0) * n / v * n
		double t = mNormal.dotProd(Point3D.getVec(iRay.mOriginPoint, mPoint)) / nDotD;
		
		if (t >= Surface.EPSILON) {
			// Return p0 + tv as the point of intersection
			return new Point3D(iRay.mOriginPoint, Vec.scale(t, iRay.mDirectionVector));
		} else {
			return null;
		}
	}
	
	// Checks whether a point is on the plane or not. Compare to EPSILON to compensate
	// for computer calculation error
	public boolean checkPointOnPlane(Point3D point) {
		Vec vecFromPlanePointToOtherPoint = Point3D.getVec(mPoint, point);
		if (vecFromPlanePointToOtherPoint.equals(new Vec(0, 0, 0))) {
			return true;
		}
		vecFromPlanePointToOtherPoint.normalize();
		double dotProd = Vec.dotProd(vecFromPlanePointToOtherPoint, mNormal);
		if (dotProd <= Surface.EPSILON && dotProd >= -(Surface.EPSILON)) {
			return true;
		} else {
			return false;
		}
	}
}
