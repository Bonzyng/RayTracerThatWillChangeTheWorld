package ex3.surfaces;

import java.util.ArrayList;
import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

public class TriangleMesh extends Surface {
	public ArrayList<Triangle> mTriangles;

	public void init(Map<String, String> attributes) {
		mTriangles = new ArrayList<Triangle>();
		
		int i = 0;
		String triangle = "tri" + i;
		while (attributes.containsKey(triangle)) {
			mTriangles.add(new Triangle(triangle));
			i++;
			triangle = "tri" + i;
		}
	}
	
	@Override
	public Point3D intersect(Ray iRay) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vec getNormalAtPoint(Point3D point) {
		// TODO Auto-generated method stub
		return null;
	}
}
