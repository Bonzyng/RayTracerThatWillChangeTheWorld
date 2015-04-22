package tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import math.Point3D;
import math.Ray;
import math.Vec;

import org.junit.Test;

import ex3.surfaces.ConvexPolygon;

public class testConvexPolygon {
	
	@Test
	public void testInit() {
		HashMap<String, String> attributes = new HashMap<String, String>();
		attributes.put("p0", "1 0 0");
		attributes.put("p1", "3 2 2");
		attributes.put("p2", "0 2 4");
		ConvexPolygon cp = new ConvexPolygon();
		cp.init(attributes);
		
		assertTrue(cp.mVertices.get(0).equals(new Point3D(1, 0, 0)) &&
				cp.mVertices.get(1).equals(new Point3D(3, 2, 2)) &&
				cp.mVertices.get(2).equals(new Point3D(0, 2, 4)));
	}
	
	@Test
	public void testConvexPolygonRayIntersection() {
		HashMap<String, String> attributes = new HashMap<String, String>();
		attributes.put("p0", "1 0 0");
		attributes.put("p1", "3 2 2");
		attributes.put("p2", "0 2 4");
		ConvexPolygon cp = new ConvexPolygon();
		cp.init(attributes);
		
		Ray r = new Ray(new Point3D(2.1, 4.4, 8.6), new Vec(-1, -2, -4));
		
		Point3D i = cp.intersect(r);
		
		assertTrue(i != null);
	}
}
