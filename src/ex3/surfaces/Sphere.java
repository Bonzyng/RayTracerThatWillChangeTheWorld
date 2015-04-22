package ex3.surfaces;

import java.util.Map;

import math.Point3D;
import math.Ray;

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
		// TODO implement ray-sphere intersection
		return null;
	}
}
