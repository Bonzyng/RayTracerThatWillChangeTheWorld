package ex3.surfaces;

import java.util.ArrayList;
import java.util.Map;

import math.Point3D;
import math.Ray;

public class ConvexPolygon extends Surface {
	
	private static final String ERR_NOT_ENOUGH_VERTICES = "Error: Must provide "
			+ "at least 3 vertices (p0, p1, p2, ...)";
	
	protected ArrayList<Point3D> mVertices;
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		
		int i = 0;
		String vertex = "p" + i;
		while (attributes.containsKey(vertex)) {
			mVertices.add(new Point3D(attributes.get(vertex)));
			i++;
		};

		if (mVertices.size() < 3) {
			throw new IllegalArgumentException(ERR_NOT_ENOUGH_VERTICES);
		}
	}

	@Override
	public Point3D intersect(Ray iRay) {
		// TODO implement disc-ray intersection
		return null;
	}
}
