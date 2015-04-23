package ex3.surfaces;

import java.util.ArrayList;
import java.util.Map;

import math.Plane;
import math.Point3D;
import math.Ray;
import math.Vec;

public class ConvexPolygon extends Surface {
	
	private static final String ERR_NOT_ENOUGH_VERTICES = "Error: Must provide "
			+ "at least 3 vertices (p0, p1, p2, ...)";
	
	public ArrayList<Point3D> mVertices;
	protected Vec mNormal;
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		mVertices = new ArrayList<Point3D>();
		
		int i = 0;
		String vertex = "p" + i;
		while (attributes.containsKey(vertex)) {
			mVertices.add(new Point3D(attributes.get(vertex)));
			i++;
			vertex = "p" + i;
		};
		
		// TODO: Check all vertices different, given in correct order, convex
		if (mVertices.size() < 3) {
			throw new IllegalArgumentException(ERR_NOT_ENOUGH_VERTICES);
		}
	}

	@Override
	public Point3D intersect(Ray iRay) {
		Plane plane = new Plane(mVertices.get(0), mVertices.get(1), mVertices.get(2));
		mNormal = plane.mNormal;
		
		Point3D intersectionPoint = plane.intersect(iRay);
		
		if (intersectionPoint == null) {
			return null;
		}
		
		// Check if point is in the polygon
		// Check for each edge (p_i, p_i+1)
		for (int i = 0; i < mVertices.size() - 1; i++) {
			
			if (!checkEdge(iRay.mOriginPoint, intersectionPoint, i, i + 1)) {
				return null;
			}
		}
		
		// Check for last edge (p_max, p0)
		if (!checkEdge(iRay.mOriginPoint, intersectionPoint, mVertices.size() - 1, 0)) {
			return null;
		}
		
		return intersectionPoint;
	}
	
	@Override
	public Vec getNormalAtPoint(Point3D point) {
		if (mNormal != null) {
			return mNormal;
		} else {
			Plane plane = new Plane(mVertices.get(0), mVertices.get(1), mVertices.get(2));
			mNormal = plane.mNormal;
		}
		
		return mNormal;
	}
	
	private boolean checkEdge(Point3D originPoint, Point3D intersectionPoint, 
			int vertex1, int vertex2) {
		Vec v1 = Point3D.getVec(originPoint, mVertices.get(vertex1));
		Vec v2 = Point3D.getVec(originPoint, mVertices.get(vertex2));
		
		Vec normal = Vec.crossProd(v2, v1);
		normal.normalize();
		
		if (Point3D.getVec(originPoint, intersectionPoint)
				.dotProd(normal) < 0) {
			return false;
		}
		
		return true;
	}
}
