package tests;

import math.Plane;
import math.Point3D;
import math.Ray;
import math.Vec;

import org.junit.Test;

import static org.junit.Assert.*;


public class PlaneTest {
	
	@Test
	public void testPlanePointAndNormal() {
		Point3D p = new Point3D(1, 0, 0);
		Vec n = new Vec(4, -10, 6);
		Plane plane = new Plane(p, n);
		n.normalize();
		assertTrue(plane.mNormal.equals(n) && plane.mPoint.equals(p));
	}
	
	@Test
	public void testPlane3Points() {
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
		
		Point3D i = p.intersect(r, false);
		assertTrue(i == null);
	}
	
	@Test
	public void testPlaneRayIntersection() {
		Point3D p1 = new Point3D(-0.5, 1, -2);
		Point3D p2 = new Point3D(-1, -1, -2);
		Point3D p3 = new Point3D(1, -0.3, - 2);
		Plane p = new Plane(p1, p2, p3);
		Ray r = new Ray(new Point3D(0, 0, 0), new Vec(0, 0, -1));
		
		Point3D i = p.intersect(r, false);
		assertTrue(i != null);
	}
	
	@Test
	public void testPlaneRayIntersectionBehind() {
		Point3D p1 = new Point3D(1, 0, 0);
		Point3D p2 = new Point3D(3, 2, 2);
		Point3D p3 = new Point3D(0, 2, 4);
		Plane p = new Plane(p1, p2, p3);
		Ray r = new Ray(new Point3D(2.1, 4.4, 8.6), new Vec(1, 2, 4));
		
		Point3D i = p.intersect(r, false);
		assertTrue(i == null);
	}
}
