package tests;

import static org.junit.Assert.*;

import java.util.HashMap;

import math.Point3D;
import math.Vec;

import org.junit.Test;

import ex3.surfaces.TriangleMesh;

public class TriangleMeshTest {
	
	@Test
	public void testNormalAtPoint() {
		HashMap<String, String> attributes = new HashMap<String, String>();
		attributes.put("tri0", "-1 0 0  1 0 0  0 1 0");
		attributes.put("tri1", "1 0 0  0 0 1  0 1 0");
		attributes.put("tri2", "0 0 1  -1 0 0  0 1 0");
		attributes.put("tri3", "-1 0 0  1 0 0  0 0 1");
		
		TriangleMesh tm = new TriangleMesh();
		tm.init(attributes);
		
		Vec normal = tm.getNormalAtPoint(new Point3D(0, 0.5, 0));
		System.out.println(normal);
	}
}
