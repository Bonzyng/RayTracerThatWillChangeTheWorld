package ex3.surfaces;

import math.Point3D;

public class Sphere extends Surface {
	private Point3D mCenter;
	private double mRadius;
	
	public Sphere(Point3D iCenter, double iRadius) {
		super();
		mCenter = iCenter;
		mRadius = iRadius;
	}
}
