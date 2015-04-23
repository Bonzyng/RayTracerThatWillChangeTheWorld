package e3.utils;

import math.Point3D;
import ex3.surfaces.Surface;

public class Intersection {
	
	private Point3D mIntersection;
	private Surface mSurface;

	public Intersection(Point3D closestIntersection, Surface closestSurface) {
		mIntersection = closestIntersection;
		mSurface = closestSurface;
	}
	
	public Surface getSurface() {
		return mSurface;
	}
	
	public Point3D getHit() {
		return mIntersection;
	}
	
}
