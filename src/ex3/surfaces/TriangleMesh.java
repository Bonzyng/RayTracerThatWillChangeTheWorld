package ex3.surfaces;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import math.Plane;
import math.Point3D;
import math.Ray;
import math.Vec;

public class TriangleMesh extends Surface {

	public ArrayList<Triangle> mTriangles;

	public void init(Map<String, String> attributes) {
		super.init(attributes);
		
		mTriangles = new ArrayList<Triangle>();
		
		int i = 0;
		String triangle = "tri" + i ;
		while (attributes.containsKey(triangle)) {
			Triangle triangle1 = new Triangle(createTrianglesFromString((attributes.get(triangle))));
			
			mTriangles.add(triangle1);
			i++;
			triangle = "tri" + i;
		}
	}
	
	private Point3D[] createTrianglesFromString(String triangle) {
		double x;
		double y;
		double z;
		
		Point3D[] vertices = new Point3D[3];
		Scanner s = new Scanner(triangle);
		for (int i = 0; i < 3; i++) {
			x = s.nextDouble();
			y = s.nextDouble();
			z = s.nextDouble();
			
			vertices[i] = new Point3D(x, y, z);
		}
		s.close();
		
		return vertices;
	}
	
	@Override
	public Point3D intersect(Ray iRay) {
		Point3D closestIntersection = null;
		double minDistance = Double.MAX_VALUE;
		
		int numOfTriangles = mTriangles.size();
		
		for (int i = 0; i < numOfTriangles; i++) {
			Surface triangle = mTriangles.get(i);
			Point3D intersection = triangle.intersect(iRay);
			if (intersection != null) {
				double distance = intersection.distance(iRay.mOriginPoint);
				
				if (distance < minDistance && distance > EPSILON) {
					minDistance = distance;
					closestIntersection = intersection;
				}			
			}
		}		
		return closestIntersection;
	}

	@Override
	public Vec getNormalAtPoint(Point3D point) {
		Triangle triangle = null;
		
		for (int i = 0; i < mTriangles.size(); i++) {
			Triangle currTriangle = mTriangles.get(i);
			if (findTriangle(currTriangle, point)) {
				triangle = currTriangle;
				break;
			}
		}
		
		Vec normal = triangle.getNormalAtPoint(point);
		return normal;
	}
	
	private boolean findTriangle(Triangle triangle, Point3D point) {
		Point3D a = triangle.mVertices.get(0);
		Point3D b = triangle.mVertices.get(1);
		Point3D c = triangle.mVertices.get(2);
		
		Plane trianglePlane = new Plane(a, b, c);
		if (!trianglePlane.checkPointOnPlane(point)) {
			return false;
		}
		
		if (sameSide(point, a, b, c) && sameSide(point, b, a, c) && sameSide(point, c, a, b)) {
			return true;
		}
		return false;
	}
	
	private boolean sameSide(Point3D p1, Point3D p2, Point3D a, Point3D b) {
		Vec cp1 = Vec.crossProd(Point3D.getVec(a, b), Point3D.getVec(a, p1));
		cp1.normalize();
		Vec cp2 = Vec.crossProd(Point3D.getVec(a, b), Point3D.getVec(a, p2));
		cp2.normalize();
		
		if (Vec.dotProd(cp1, cp2) >= 0) {
			return true;
		} 
		
		return false;
	}
}
