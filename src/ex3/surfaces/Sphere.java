package ex3.surfaces;

import math.Point3D;
import math.Ray;

public class Sphere extends Surface {
	private Point3D mCenter;
	private double mRadius;
	
	public Sphere(Point3D iCenter, double iRadius) {
		super();
		mCenter = iCenter;
		mRadius = iRadius;
	}

	@Override
	public Point3D intersect(Ray iRay) {
		// TODO implement ray-sphere intersection
		return null;
	}
}
