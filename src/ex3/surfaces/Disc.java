package ex3.surfaces;

import java.util.Map;

import math.Plane;
import math.Point3D;
import math.Ray;
import math.Vec;

public class Disc extends Sphere {
	
	private static final String ERR_NO_NORMAL = "Error: No normal value given";
	
	private Vec mNormal;
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		
		if (attributes.containsKey("normal")) {
			mNormal = new Vec(attributes.get("normal"));
		} else {
			throw new IllegalArgumentException(ERR_NO_NORMAL);
		}
		
		// Ensure the normal is normalized
		mNormal.normalize();
	}
	
	@Override
	public Point3D intersect(Ray iRay, boolean behind) {
		Plane plane = new Plane(mCenter, mNormal);
		Point3D intersectionPoint;
		
		if (behind) {
			intersectionPoint = plane.intersect(iRay, true);
		} else {
			intersectionPoint = plane.intersect(iRay, false);
		}
		
		if (intersectionPoint == null) {
			return null;
		}
		
		//create the vector P-O
		Vec vecFromCenterToIntersection = Point3D.getVec(intersectionPoint, mCenter);
		
		// Checks if point of intersection is on the disc or not
		if (Vec.dotProd(vecFromCenterToIntersection, vecFromCenterToIntersection) <= (mRadius * mRadius)) {
			return intersectionPoint;
		} else {
			return null;
		}
	}
	
	@Override
	public Vec getNormalAtPoint(Point3D point) {
		return mNormal;
	}
}
