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
		// see Rec04, p.11 for explanation to math
		
		Vec L = Point3D.getVec(iRay.mOriginPoint, mCenter);
		
		// Find distance from origin of ray (p0) to the center of the ray segment (pp') 
		// perpendicular to a ray (d) originating from the sphere center (O) (using projection)
		
		double t_m = Vec.dotProd(L, iRay.mDirectionVector);
		
		// If value is smaller than 0, point not on the ray (t must be positive!)
		if (t_m < 0) {
			return null;
		}
		
		// Shortest distance from sphere center to ray
		double d = Math.sqrt(L.lengthSquared()
				- (t_m * t_m));
		
		// If d^2 is greater than r^s (mRadius^2) than d is
		// out of the circle, and ray does not intersect
		if ((d * d) > (mRadius * mRadius)) {
			return null;
		}
		
		// Edge length between center of the ray segment and the possible intersection
		// points
		double t_h = Math.sqrt((mRadius * mRadius) - (d * d));
		
		// Intersection points of the ray with the sphere
		double p1 = t_m - t_h;
		double p2 = t_m + t_h;
		
		// Check if p1 is negative. If it is, it's behind the ray, and thus not on it.
		// If positive, it's obviously closer to the ray origin than p2.
		if (p1 > EPSILON) {
			return new Point3D(iRay.mOriginPoint, Vec.scale(p1, iRay.mDirectionVector));
		// Same check like for p1.
		} else if (p2 > EPSILON) {
			return new Point3D(iRay.mOriginPoint, Vec.scale(p2, iRay.mDirectionVector));
		// Both points behind the ray, no intersection.
		} else {
			return null;
		}
	}
	
	@Override
	public Vec getNormalAtPoint(Point3D point) {
		return Point3D.getVec(mCenter, point);
	}
}
