package ex3.surfaces;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import math.Point3D;
import math.Ray;
import math.Vec;

public class TriangleMesh extends Surface{
	
	public ArrayList<Triangle> mTriangles;

	public void init(Map<String, String> attributes) {
		super.init(attributes);
		
		mTriangles = new ArrayList<Triangle>();
		
		int i = 0;
		String triangle = "tri" + i ;
		while (attributes.containsKey(triangle)) {
			mTriangles.add(new Triangle(createTrianglesFromString((attributes.get(triangle)))));
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
		Point3D intersect = null;
		
		for (int i = 0; i < mTriangles.size(); i++) {
			intersect = mTriangles.get(i).intersect(iRay);
			if (intersect != null) {
				return intersect;
			}
		}
		
		return null;
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
		
		return triangle.getNormalAtPoint(point);
	}
	
	private boolean findTriangle(Triangle triangle, Point3D point) {
		Point3D a = triangle.mVertices.get(0);
		Point3D b = triangle.mVertices.get(1);
		Point3D c = triangle.mVertices.get(2);
		
		if (sameSide(point, a, b, c) && sameSide(point, b, a, c) && sameSide(point, c, a, b)) {
			return true;
		}
		
		return false;
	}
	
	private boolean sameSide(Point3D p1, Point3D p2, Point3D a, Point3D b) {
		Vec cp1 = Vec.crossProd(Point3D.getVec(a, b), Point3D.getVec(a, p1));
		Vec cp2 = Vec.crossProd(Point3D.getVec(a, b), Point3D.getVec(a, p2));
		
		if (Vec.dotProd(cp1, cp2) >= 0) {
			return true;
		} 
		
		return false;
	}
}
