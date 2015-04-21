package tests;

import static org.junit.Assert.*;
import math.Point3D;
import math.Vec;

import org.junit.Test;

/**
 * Test suite for the Point3D class
 * @author Nadav
 *
 */
public class Point3DTest {

	@Test
	public void testPoint3DDoubleDoubleDouble() {
		Point3D p = new Point3D(1.0, 2.5, 3.0);
		assertTrue(p.equals(new Point3D(1.0, 2.5, 3.0)));
	}
	
	@Test
	public void testPoint3DPoint3D() {
		Point3D p1 = new Point3D(1.0, 2.5, 3.0);
		Point3D p2 = new Point3D(p1);
		assertTrue(p1.equals(p2));
	}	

	@Test
	public void testPoint3DPoint3DVec() {
		Point3D p = new Point3D(1.0, 1.0, 1.0);
		Vec v = new Vec(1.0, 1.0, 1.0);
		Point3D p1 = new Point3D(p, v);
		assertTrue(p1.equals(new Point3D(2.0, 2.0, 2.0)));
	}
	
	@Test
	public void testPoint3DStaticGetVec() {
		Point3D p1 = new Point3D(1.0, 1.0, 1.0);
		Vec v1 = new Vec(1.0, 1.0, 1.0);
		Point3D p2 = new Point3D(p1, v1);
		assertTrue(p2.equals(new Point3D(2.0, 2.0, 2.0)));
	}
	
	@Test
	public void testPoint3DgetVec() {
		Point3D p1 = new Point3D(1.0, 1.0, 1.0);
		Point3D p2 = new Point3D(2.0, 2.0, 2.0);
		Vec v1 = p1.getVec(p2);
		Vec v2 = new Vec(1.0, 1.0, 1.0);
		assertTrue(v1.equals(v2));
	}
	
	@Test
	public void testPoint3DDistance() {
		Point3D p1 = new Point3D(1.0, -1.0, 1.0);
		Point3D p2 = new Point3D(3.0, 1.0, 2.0);
		assertTrue(p1.distance(p2) == 3.0);
	}
	
	@Test
	public void testPoint3DStaticDistance () {
		Point3D p1 = new Point3D(1.0, -1.0, 1.0);
		Point3D p2 = new Point3D(3.0, 1.0, 2.0);
		assertTrue(Point3D.distance(p1, p2) == 3.0);
	}
	
	@Test
	public void testPoint3DEquals() {
		Point3D p1 = new Point3D(1.0, -1.0, 1.0);
		Point3D p2 = new Point3D(1.0, -1.0, 1.0);
		assertTrue(p1.equals(p2));
	}
	
	@Test
	public void testPoint3DStaticEquals() {
		Point3D p1 = new Point3D(1.0, -1.0, 1.0);
		Point3D p2 = new Point3D(1.0, -1.0, 1.0);
		assertTrue(Point3D.equals(p1, p2));
	}
	
	
}
