package ex3.surfaces;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

public class Sphere extends Surface {
	
	private static final String ERR_NO_CENTER = "Error: No center value given";
	private static final String ERR_BAD_RADIUS = "Error: radius value must be a positive double";
	private static final String ERR_NO_RADIUS = "Error: No radius value given";
	
	protected Point3D mCenter;
	protected double mRadius;
	
	public Sphere() {
	}
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		
		if (attributes.containsKey("center")) {
			mCenter = new Point3D(attributes.get("center"));
		} else {
			throw new IllegalArgumentException(ERR_NO_CENTER);
		}
		
		if (attributes.containsKey("radius")) {
			try {
				mRadius = Double.parseDouble(attributes.get("radius"));
				if (mRadius < 0) {
					throw new IllegalArgumentException(ERR_BAD_RADIUS);		
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(ERR_BAD_RADIUS);
			}			
		} else {
			throw new IllegalArgumentException(ERR_NO_RADIUS);
		}
	}

	@Override
	public Point3D intersect(Ray iRay) {
		
		Vec rayOriginToSphereCenter = Point3D.getVec(iRay.mOriginPoint, mCenter);
		double t_m = Vec.dotProd(rayOriginToSphereCenter, iRay.mDirectionVector);
		
		// ray, not line
		if (t_m < 0) {
			return null;
		}
		
		double d = Math.sqrt(rayOriginToSphereCenter.lengthSquared() - t_m);
		if (d > mRadius*mRadius) {
			return null;
		}
		
		double t_h = Math.sqrt(mRadius - d*d);
		double t1 = t_m - t_h;
		double t2 = t_m + t_h;
		
		if (Math.min(t1, t2) > 0) {
			return new Point3D(iRay.mOriginPoint, Vec.scale(Math.min(t1, t2), iRay.mDirectionVector));
		} else {
			return new Point3D(iRay.mOriginPoint, Vec.scale(Math.max(t1, t2), iRay.mDirectionVector));
		}
	}
}
