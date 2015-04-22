package ex3.surfaces;

import java.util.ArrayList;
import java.util.Map;

import math.Point3D;
import math.Ray;

public class ConvexPolygon extends Surface {
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
	}

	@Override
	public Point3D intersect(Ray iRay) {
		// TODO implement disc-ray intersection
		return null;
	}
}
