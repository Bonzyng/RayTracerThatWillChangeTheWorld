package tests;

import math.Plane;
import math.Point3D;
import math.Ray;
import math.Vec;

import org.junit.Test;

import static org.junit.Assert.*;


public class PlaneTest {
	
	@Test
	public void testPlanePoint() {
		Point3D p1 = new Point3D(1, 0, 0);
		Point3D p2 = new Point3D(3, 2, 2);
		Point3D p3 = new Point3D(0, 2, 4);
		Plane p = new Plane(p1, p2, p3);
		assertTrue(p.mPoint.equals(p1));
	}
	
	@Test
	public void testPlaneNormal() {
		Point3D p1 = new Point3D(1, 0, 0);
		Point3D p2 = new Point3D(3, 2, 2);
		Point3D p3 = new Point3D(0, 2, 4);
		Plane p = new Plane(p1, p2, p3);
		Vec n = new Vec(4, -10, 6);
		n.normalize();
		assertTrue(p.mNormal.equals(n));
	}
	
	@Test
	public void testPlaneRayIntersectionParallelRay() {
		Point3D p1 = new Point3D(1, 0, 0);
		Point3D p2 = new Point3D(3, 2, 2);
		Point3D p3 = new Point3D(0, 2, 4);
		Plane p = new Plane(p1, p2, p3);
		Ray r = new Ray(new Point3D(0, 2, 0), new Vec(-1, 2, 4));
		
		Point3D i = p.intersect(r);
		assertTrue(i == null);
	}
	
	@Test
	public void testPlaneRayIntersectionIntersectingRay() {
		Point3D p1 = new Point3D(1, 0, 0);
		Point3D p2 = new Point3D(3, 2, 2);
		Point3D p3 = new Point3D(0, 2, 4);
		Plane p = new Plane(p1, p2, p3);
		Ray r = new Ray(new Point3D(-8, 16, 46), new Vec(-1, -2, -3));
		
		Point3D i = p.intersect(r);
		assertTrue(i != null);
	}
}
