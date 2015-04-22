package ex3.surfaces;

import java.util.Map;

import math.Point3D;
import math.Ray;

public class Triangle extends ConvexPolygon {
	
	private static final String ERR_TOO_MANY_VERTICES = "Error: triangle may must "
			+ "have exactly 3 vertices (p0, p1, p2)";
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		
		if (mVertices.size() != 3) {
			throw new IllegalArgumentException(ERR_TOO_MANY_VERTICES);
		}
	}
	
	@Override
	public Point3D intersect(Ray iRay) {
		// TODO implement ray-triangle intersection
		return null;
	}
}
